package com.xxscloud.videox.config

data class USession(
        var id: String = "",
        var account: String = "",
        var token: String = "",
        var userType: String = "",
        var agent: String = "",
        var ip: String = "",
        var role: List<String> = ArrayList(),
        var permissions: List<String> = ArrayList(),
        var weChatInfo: WeChatInfo = WeChatInfo(),
) {

    data class WeChatInfo(
            var openId: String = "",
            var nickName: String = "",
            var avatarUrl: String = ""
    )
}