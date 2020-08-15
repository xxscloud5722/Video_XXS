package com.xxscloud.videox.data

data class UserDO(
        var id: String = "",
        var nickName: String = "",
        var avatarUrl: String = "",
        var account: String = "",
        var password: String = "",
        var description: String = "",
        var token: String = "",
        var level: String = "",
        var levelName: String = "",
        var status: Int = 0,
)