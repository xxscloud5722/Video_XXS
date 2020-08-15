package com.xxscloud.videox.service.api

import com.google.inject.Inject
import com.xxscloud.videox.dao.ActivityDAO
import com.xxscloud.videox.data.ActivityItemChildrenDTO
import com.xxscloud.videox.data.ActivityItemDTO
import com.xxscloud.videox.data.ActivityItemVideoDTO


class IndexService @Inject constructor(private val activityDAO: ActivityDAO) {
    suspend fun getActivityItemList(id: String): List<ActivityItemDTO> {
        return activityDAO.getActivityItemByActivityId(id)
    }

    suspend fun getActivityItemVideoList(id: String): List<ActivityItemChildrenDTO> {
        val result = activityDAO.getActivityItemChildrenByItemId(id)
        result.forEach {
            it.videoList = activityDAO.getActivityItemVideoByItemChildrenId(it.id)
        }
        return result
    }
}