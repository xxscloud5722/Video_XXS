package com.xxscloud.videox.dao

import com.google.inject.Inject
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.data.VideoDO
import com.xxscloud.videox.data.VideoDTO
import io.vertx.sqlclient.SqlConnection


class VideoDAO @Inject constructor(private val sqlCore: MySQLCore) {

    suspend fun getById(id: String, transaction: SqlConnection? = null): VideoDO? {
        return sqlCore.queryFirst("""SELECT * FROM v_video WHERE id = ? """,
                arrayListOf(id), VideoDO::class.java, transaction)
    }

    suspend fun getDetailById(id: String, transaction: SqlConnection? = null): VideoDTO? {
        return sqlCore.queryFirst("""
            SELECT vv.*, vvc.name AS "clarity" FROM v_video vv
             LEFT JOIN v_video_clarity vvc ON vvc.id = vv.clarity_id
            WHERE vv.id = ?
        """, arrayListOf(id), VideoDTO::class.java, transaction)
    }

    suspend fun getRecommendVideoList(id: String, transaction: SqlConnection? = null): List<VideoDTO> {
        return sqlCore.query("""
            SELECT * FROM v_video 
            WHERE category_id = (SELECT category_id FROM v_video WHERE id = ? LIMIT 1)
                AND id != ?
            ORDER BY RAND() LIMIT 8
        """, arrayListOf(id, id), VideoDTO::class.java, transaction)
    }
}