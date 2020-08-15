package com.xxscloud.videox.data

import java.math.BigDecimal
import java.util.*

data class ProductDO(
        var id: String = "",
        var title: String = "",
        var price: BigDecimal = BigDecimal.ONE,
        var originalPrice: BigDecimal = BigDecimal.ONE,
        var tag: String = "",
        var level: Int = 0,
        var levelName: String = "",
        var createTime: Date? = null
)