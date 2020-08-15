package com.xxscloud.videox.data

import java.util.*

data class DictionaryDO(
        var id: String = "",
        var gid: String = "",
        var name: String = "",
        var value: String = "",
        var sort: String = "",
        var text: String = "",
        var status: Int = 0,
        var display: Int = 0,
        var createTime: Date? = null
)