package com.xxscloud.videox.core


import com.xxscloud.videox.config.Config
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.mysqlclient.MySQLClient
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.*
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class MySQLCore {

    companion object {
        private lateinit var vertx: Vertx
        private lateinit var pool: MySQLPool
        private lateinit var sqlCore: MySQLCore
        private val log = LoggerFactory.getLogger(MySQLCore::class.java)
        fun init(vertx: Vertx): MySQLCore {

            val host = Config.parseEnv(System.getProperty("mysql.host")) ?: ""
            val port = (Config.parseEnv(System.getProperty("mysql.port")) ?: "3306").toInt()
            val database = Config.parseEnv(System.getProperty("mysql.database")) ?: ""
            val user = Config.parseEnv(System.getProperty("mysql.user")) ?: ""
            val password = Config.parseEnv(System.getProperty("mysql.password")) ?: ""
            val connectOptions = MySQLConnectOptions(
                    JsonObject()
                            .put("port", port)
                            .put("host", host)
                            .put("database", database)
                            .put("user", user)
                            .put("password", password)
                            .put("charset", "utf8mb4")
                            .put("collation", "utf8mb4_general_ci")
            )

            val poolOptions = PoolOptions(JsonObject().put("maxSize", 20).put("maxWaitQueueSize", 40))

            pool = MySQLPool.pool(vertx, connectOptions, poolOptions)
            log.info("MySQL Loading complete Host: $host Port: $port Database: $database")
            sqlCore = MySQLCore()
            this.vertx = vertx
            return sqlCore
        }

        fun getCore(): MySQLCore {
            return sqlCore
        }

        suspend fun transaction(fn: suspend (SqlConnection?) -> Unit) {
            val connection = awaitResult<SqlConnection> { pool.getConnection(it) }
            val transaction = connection.begin()
            try {
                fn(connection)
                awaitResult<Void> { transaction.commit(it) }
            } catch (e: Exception) {
                transaction.rollback()
                throw e
            } catch (e: Throwable) {
                transaction.rollback()
                throw e
            } finally {
                connection.close()
                transaction.close()
            }
        }
    }

    private fun toDbName(name: String): String {
        val value = StringBuilder()
        name.forEach {
            if (it in 'A'..'Z') {
                value.append("_").append(it.toLowerCase())
            } else {
                value.append(it)
            }
        }
        return value.toString()
    }

    private fun <T> toBean(row: Row, clazz: Class<T>): T? {
        val constructor = clazz.getConstructor()
        val data = constructor.newInstance()
        clazz.methods.forEach { m ->
            if (!m.name.startsWith("set")) {
                return@forEach
            }
            val field = m.name[3].toLowerCase() + m.name.substring(4)
            //字段名称
            var index = row.getColumnIndex(field)
            //小写
            if (index < 0) {
                index = row.getColumnIndex(field.toLowerCase())
            }
            //驼峰转下划线
            if (index < 0) {
                index = row.getColumnIndex(toDbName(field))
            }
            if (index <= -1) {
                return@forEach
            }
            //获取结果, 如果是null 什么都不处理
            val r = getValue(m.parameterTypes[0], row, index)
            r?.let {
                m.invoke(data, r)
            }
        }
        return data
    }


    private fun <T> getValue(clazz: Class<T>, row: Row, index: Int): T? {
        return when (clazz) {
            Date::class.java -> Date.from(row.getLocalDateTime(index).atZone(ZoneId.systemDefault()).toInstant()) as T?
            Int::class.java -> row.getInteger(index) as T?
            Double::class.java -> row.getDouble(index) as T?
            Float::class.java -> row.getFloat(index) as T?
            BigDecimal::class.java -> row.getBigDecimal(index) as T?
            String::class.java -> row.getValue(index)?.toString() as T?
            Boolean::class.java -> row.getBoolean(index) as T?
            else -> row.get(clazz, index)
        }
    }

    private suspend fun <T> execution(sqlConnection: SqlConnection?, fn: suspend (SqlConnection) -> T?): T? {
        val connection = sqlConnection ?: awaitResult { pool.getConnection(it) }
        try {
            return fn(connection)
        } finally {
            if (sqlConnection == null) {
                connection.close()
            }
        }
    }

    private suspend fun <T> executionResultList(sqlConnection: SqlConnection?, fn: suspend (SqlConnection) -> List<T>): List<T> {
        val connection = sqlConnection ?: awaitResult { pool.getConnection(it) }
        try {
            return fn(connection)
        } finally {
            if (sqlConnection == null) {
                connection.close()
            }
        }
    }


    suspend fun <T> queryResult(sql: String, tuple: List<Any>, clazz: Class<T>, transaction: SqlConnection?): T? {
        val executeTuple = Tuple.tuple()
        tuple.forEach {
            executeTuple.addValue(it)
        }
        log.debug("queryResult : $sql")
        return execution(transaction) { connection ->
            val result = awaitResult<RowSet<Row>> {
                if (tuple.isNotEmpty()) {
                    connection.preparedQuery(sql).execute(executeTuple, it)
                } else {
                    connection.preparedQuery(sql).execute(it)
                }
            }
            if (result.size() > 0) {
                result.firstOrNull { row ->
                    return@execution getValue(clazz, row, 0)
                }
            }
            return@execution null
        }
    }

    suspend fun <T> queryResult(sql: String, tuple: List<Any>, clazz: Class<T>): T? {
        return this.queryResult(sql, tuple, clazz, null)
    }

    suspend fun <T> queryResult(sql: String, clazz: Class<T>, transaction: SqlConnection?): T? {
        return this.queryResult(sql, ArrayList(), clazz, transaction)
    }

    suspend fun <T> queryResult(sql: String, clazz: Class<T>): T? {
        return this.queryResult(sql, ArrayList(), clazz, null)
    }


    suspend fun <T> queryFirst(sql: String, tuple: List<Any>, clazz: Class<T>, transaction: SqlConnection?): T? {
        val executeTuple = Tuple.tuple()
        tuple.forEach {
            executeTuple.addValue(it)
        }
        log.debug("queryFirst : $sql")
        return execution(transaction) { connection ->
            val result = awaitResult<RowSet<Row>> {
                if (tuple.isNotEmpty()) {
                    connection.preparedQuery("SELECT * FROM (${sql}) t LIMIT 1").execute(executeTuple, it)
                } else {
                    connection.preparedQuery("SELECT * FROM (${sql}) t LIMIT 1").execute(it)
                }
            }
            if (result.size() > 0) {
                result.firstOrNull {
                    return@execution toBean(it, clazz)
                }
            }
            return@execution null
        }
    }

    suspend fun <T> queryFirst(sql: String, tuple: List<Any>, clazz: Class<T>): T? {
        return this.queryFirst(sql, tuple, clazz, null)
    }

    suspend fun <T> queryFirst(sql: String, clazz: Class<T>, transaction: SqlConnection?): T? {
        return this.queryFirst(sql, ArrayList(), clazz, transaction)
    }

    suspend fun <T> queryFirst(sql: String, clazz: Class<T>): T? {
        return this.queryFirst(sql, ArrayList(), clazz, null)
    }


    suspend fun <T> query(sql: String, tuple: List<Any>, clazz: Class<T>, transaction: SqlConnection?): List<T> {
        val executeTuple = Tuple.tuple()
        tuple.forEach {
            executeTuple.addValue(it)
        }
        log.debug("query : $sql")
        return executionResultList(transaction) { connection ->
            val result = awaitResult<RowSet<Row>> {
                if (tuple.isNotEmpty()) {
                    connection.preparedQuery(sql).execute(executeTuple, it)
                } else {
                    connection.preparedQuery(sql).execute(it)
                }
            }
            if (result.size() > 0) {
                val resultList = ArrayList<T>()
                result.forEach {
                    val value = toBean(it, clazz)
                    value?.let {
                        resultList.add(value)
                    }
                }
                return@executionResultList resultList
            }
            return@executionResultList ArrayList<T>()
        }
    }

    suspend fun <T> query(sql: String, tuple: List<Any>, clazz: Class<T>): List<T> {
        return this.query(sql, tuple, clazz, null)
    }

    suspend fun <T> query(sql: String, clazz: Class<T>, transaction: SqlConnection?): List<T> {
        return this.query(sql, ArrayList(), clazz, transaction)
    }

    suspend fun <T> query(sql: String, clazz: Class<T>): List<T> {
        return this.query(sql, ArrayList(), clazz, null)
    }


    suspend fun insert(sql: String, tuple: ArrayList<Any>, transaction: SqlConnection?): Boolean {
        val executeTuple = Tuple.tuple()
        tuple.forEach {
            executeTuple.addValue(it)
        }
        log.debug("insert : $sql")
        return execution(transaction) { connection ->
            val result = awaitResult<RowSet<Row>> {
                if (tuple.isNotEmpty()) {
                    connection.preparedQuery(sql).execute(executeTuple, it)
                } else {
                    connection.preparedQuery(sql).execute(it)
                }
            }
            if (result.rowCount() > 0) {
                return@execution true
            }
            return@execution false
        } ?: false
    }

    suspend fun insert(sql: String, tuple: ArrayList<Any>): Boolean {
        return insert(sql, tuple, null)
    }

    suspend fun insert(sql: String, transaction: SqlConnection?): Boolean {
        return insert(sql, ArrayList(), transaction)
    }

    suspend fun insert(sql: String): Boolean {
        return insert(sql, ArrayList())
    }


    suspend fun insertLastInsert(sql: String, tuple: ArrayList<Any>, transaction: SqlConnection?): Long {
        val executeTuple = Tuple.tuple()
        tuple.forEach {
            executeTuple.addValue(it)
        }
        log.debug("insertLastInsert : $sql")
        return execution(transaction) { connection ->
            val result = awaitResult<RowSet<Row>> {
                if (tuple.isNotEmpty()) {
                    connection.preparedQuery(sql).execute(executeTuple, it)
                } else {
                    connection.preparedQuery(sql).execute(it)
                }
            }
            if (result.rowCount() > 0) {
                return@execution result.property(MySQLClient.LAST_INSERTED_ID)
            }
            return@execution -1
        } as Long
    }

    suspend fun insertLastInsert(sql: String, tuple: ArrayList<Any>): Long {
        return insertLastInsert(sql, tuple, null)
    }

    suspend fun insertLastInsert(sql: String, transaction: SqlConnection?): Long {
        return insertLastInsert(sql, ArrayList(), transaction)
    }

    suspend fun insertLastInsert(sql: String): Long {
        return insertLastInsert(sql, ArrayList())
    }


    suspend fun update(sql: String, tuple: List<Any>, transaction: SqlConnection?): Boolean {
        val executeTuple = Tuple.tuple()
        tuple.forEach {
            executeTuple.addValue(it)
        }
        log.debug("update : $sql")
        return execution(transaction) { connection ->
            val result = awaitResult<RowSet<Row>> {
                if (tuple.isNotEmpty()) {
                    connection.preparedQuery(sql).execute(executeTuple, it)
                } else {
                    connection.preparedQuery(sql).execute(it)
                }
            }
            if (result.rowCount() > 0) {
                return@execution true
            }

            return@execution false
        } ?: false
    }

    suspend fun update(sql: String, tuple: List<Any>): Boolean {
        return update(sql, tuple)
    }

    suspend fun update(sql: String): Boolean {
        return update(sql, ArrayList())
    }

    suspend fun update(sql: String, transaction: SqlConnection?): Boolean {
        return update(sql, ArrayList(), transaction)
    }


    suspend fun delete(sql: String, tuple: List<Any>, transaction: SqlConnection?): Boolean {
        val executeTuple = Tuple.tuple()
        tuple.forEach {
            executeTuple.addValue(it)
        }
        log.debug("delete : $sql")
        return execution(transaction) { connection ->
            val result = awaitResult<RowSet<Row>> {
                if (tuple.isNotEmpty()) {
                    connection.preparedQuery(sql).execute(executeTuple, it)
                } else {
                    connection.preparedQuery(sql).execute(it)
                }
            }
            if (result.rowCount() > 0) {
                return@execution true
            }
            return@execution false
        } ?: false

    }

    suspend fun delete(sql: String, tuple: List<Any>): Boolean {
        return delete(sql, tuple, null)
    }

    suspend fun delete(sql: String): Boolean {
        return delete(sql, ArrayList())
    }

    suspend fun delete(sql: String, transaction: SqlConnection?): Boolean {
        return delete(sql, ArrayList(), transaction)
    }


}