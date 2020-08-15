package com.xxscloud.videox.data

data class ActivityItemChildrenDTO(
        var id: String = "",
        var activityItemId: String = "",
        var title: String = "",
        var link: String = "",
        var linkName: String = "",
        var videoList: List<ActivityItemVideoDTO>? = null
)