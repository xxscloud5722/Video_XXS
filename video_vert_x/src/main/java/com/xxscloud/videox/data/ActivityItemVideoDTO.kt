package com.xxscloud.videox.data

import java.math.BigDecimal
import java.util.*

data class ActivityItemVideoDTO(
        var id: String = "",
        var cover: String = "",
        var title: String = "",
        var description: String = "",
        var url: String = "",
        var previewUrl: String = "",
        var categoryId: String = "",
        var category: String = "",
        var watchCount: Int = 0,
        var clarityId: String = "",
        var clarity: String = "",
        var duration: BigDecimal = BigDecimal.ONE,
        var virtualDuration: BigDecimal = BigDecimal.ONE,
        var createTime: Date? = null

)
