package com.xxscloud.videox.dao

import com.google.inject.Inject
import com.google.inject.Singleton
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.data.UserDO
import io.vertx.sqlclient.SqlConnection


@Singleton
class UserDAO @Inject constructor(private val sqlCore: MySQLCore) {


    suspend fun getByAccount(account: String, transaction: SqlConnection? = null): UserDO? {
        return sqlCore.queryFirst("""SELECT * FROM v_user WHERE account = ? """,
                arrayListOf(account), UserDO::class.java, transaction)
    }

    suspend fun registered(account: String, password: String, transaction: SqlConnection? = null): Boolean {
        return sqlCore.insert("""
            INSERT INTO `v_user`(`account`, `password`, `token`)
            VALUES (?, ?, UUID());
            """, arrayListOf<Any>(account, password), transaction)
    }

    suspend fun updateToken(id: String, token: String, transaction: SqlConnection? = null): Boolean {
        return sqlCore.update("UPDATE v_user SET token = ? WHERE id = ?", arrayListOf(token, id), transaction)
    }

    suspend fun getById(id: String, transaction: SqlConnection? = null): UserDO? {
        return sqlCore.queryFirst("""SELECT * FROM v_user WHERE id = ? """,
                arrayListOf(id), UserDO::class.java, transaction)
    }
}