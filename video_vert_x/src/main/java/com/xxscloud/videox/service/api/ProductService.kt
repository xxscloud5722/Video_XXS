package com.xxscloud.videox.service.api

import com.google.inject.Inject
import com.xxscloud.videox.dao.DictionaryDAO
import com.xxscloud.videox.dao.ProductDAO
import com.xxscloud.videox.data.ProductDO

class ProductService @Inject constructor(private val productDAO: ProductDAO){



    suspend fun getList(): List<ProductDO> {
        return productDAO.getAllList()
    }
}