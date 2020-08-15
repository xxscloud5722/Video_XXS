package com.xxscloud.videox.data

import java.util.*

data class ActivityItemDTO(
        var id: String = "",
        var parentId: String = "",
        var activityId: String = "",
        var name: String = "",
        var level: Int = 0,
        var createTime: Date? = null
)