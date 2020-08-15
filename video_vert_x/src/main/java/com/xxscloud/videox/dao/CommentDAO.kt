package com.xxscloud.videox.dao

import com.google.inject.Inject
import com.google.inject.Singleton
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.data.CommentDTO
import io.vertx.sqlclient.SqlConnection

@Singleton
class CommentDAO @Inject constructor(private val sqlCore: MySQLCore) {


    suspend fun getCommentByVideoId(videoId: String, transaction: SqlConnection? = null): List<CommentDTO> {
        return sqlCore.query("""
            SELECT vc.*, vu.avatar_url, vu.nick_name, vu.level_name FROM v_comment vc
            LEFT JOIN v_user vu ON vc.user_id = vu.id 
            WHERE video_id = ?
        """, arrayListOf(videoId), CommentDTO::class.java, transaction)
    }

    suspend fun getCommentByUserId(userId: String, transaction: SqlConnection? = null): List<CommentDTO> {
        return sqlCore.query("""
            SELECT vc.*, vu.avatar_url, vu.nick_name, vu.level_name, vv.title AS "citeTitle" FROM v_comment vc
            LEFT JOIN v_user vu ON vc.user_id = vu.id 
            LEFT JOIN v_video vv ON vv.id = vc.video_id
            WHERE user_id = ?
        """, arrayListOf(userId), CommentDTO::class.java, transaction)
    }
}