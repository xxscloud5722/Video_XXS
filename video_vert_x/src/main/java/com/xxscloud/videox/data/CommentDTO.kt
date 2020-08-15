package com.xxscloud.videox.data

import java.util.*

data class CommentDTO(
        var id: String = "",
        var replyId: String = "",
        var videoId: String = "",
        var userId: String = "",
        var content: String = "",
        var createTime: Date? = null,


        var citeTitle: String = "",
        var avatarUrl: String = "",
        var nickName: String = "",
        var levelName: String = "",
)