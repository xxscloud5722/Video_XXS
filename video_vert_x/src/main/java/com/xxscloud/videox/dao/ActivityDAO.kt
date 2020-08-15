package com.xxscloud.videox.dao

import com.google.inject.Inject
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.data.ActivityItemChildrenDTO
import com.xxscloud.videox.data.ActivityItemDTO
import com.xxscloud.videox.data.ActivityItemVideoDTO
import io.vertx.sqlclient.SqlConnection

class ActivityDAO @Inject constructor(private val sqlCore: MySQLCore) {
    suspend fun getActivityItemByActivityId(id: String, transaction: SqlConnection? = null): List<ActivityItemDTO> {
        return sqlCore.query("""
           SELECT * FROM v_activity_item WHERE activity_id = ? ORDER BY sort DESC
        """, arrayListOf(id), ActivityItemDTO::class.java, transaction)
    }

    suspend fun getActivityItemChildrenByItemId(id: String, transaction: SqlConnection? = null): List<ActivityItemChildrenDTO> {
        return sqlCore.query("""
           SELECT * FROM v_activity_item_children  vaic
            WHERE vaic.activity_item_id = ?
        """, arrayListOf(id), ActivityItemChildrenDTO::class.java, transaction)
    }

    suspend fun getActivityItemVideoByItemChildrenId(id: String, transaction: SqlConnection? = null): List<ActivityItemVideoDTO> {
        return sqlCore.query("""
           SELECT vv.*, vvc.name AS "category", vvc2.name AS "clarity" FROM v_activity_item_video vaiv
           LEFT JOIN v_video vv ON vv.id = vaiv.video_id
           LEFT JOIN v_video_category vvc ON vvc.id = vv.category_id
           LEFT JOIN v_video_clarity vvc2 ON vvc2.id = vv.clarity_id
           WHERE vaiv.activity_item_children = ?
        """, arrayListOf(id), ActivityItemVideoDTO::class.java, transaction)
    }
}