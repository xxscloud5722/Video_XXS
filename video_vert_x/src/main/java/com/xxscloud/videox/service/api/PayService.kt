package com.xxscloud.videox.service.api

import com.google.inject.Inject
import com.xxscloud.videox.dao.DictionaryDAO
import com.xxscloud.videox.data.DictionaryDO


class PayService @Inject constructor(private val dictionaryDAO: DictionaryDAO) {

    suspend fun getPayMethod(): List<DictionaryDO> {
        return dictionaryDAO.getByGId("pay.method")
    }
}