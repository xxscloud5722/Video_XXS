package com.xxscloud.videox.dao

import com.google.inject.Inject
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.data.ProductDO
import io.vertx.sqlclient.SqlConnection

class ProductDAO @Inject constructor(private val sqlCore: MySQLCore) {


    suspend fun getAllList(transaction: SqlConnection? = null): List<ProductDO> {
        return sqlCore.query("""
           SELECT * FROM v_product
        """, ProductDO::class.java,transaction)
    }
}