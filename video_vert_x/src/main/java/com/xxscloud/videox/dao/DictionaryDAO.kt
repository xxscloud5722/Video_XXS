package com.xxscloud.videox.dao

import com.google.inject.Inject
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.data.DictionaryDO
import io.vertx.sqlclient.SqlConnection

class DictionaryDAO @Inject constructor(private val sqlCore: MySQLCore) {


    suspend fun getByGId(gid: String, transaction: SqlConnection? = null): List<DictionaryDO> {
        return sqlCore.query("""
                    SELECT * FROM v_dictionary WHERE gid = ? 
                """,
                arrayListOf(gid), DictionaryDO::class.java, transaction)
    }

}