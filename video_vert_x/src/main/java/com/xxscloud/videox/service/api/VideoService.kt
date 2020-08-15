package com.xxscloud.videox.service.api

import com.google.inject.Inject
import com.xxscloud.videox.dao.CommentDAO
import com.xxscloud.videox.dao.UserDAO
import com.xxscloud.videox.dao.VideoDAO
import com.xxscloud.videox.data.CommentDTO
import com.xxscloud.videox.data.VideoDTO

class VideoService @Inject constructor(private val videoDAO: VideoDAO, private val commentDAO: CommentDAO) {

    suspend fun getDetail(id: String): VideoDTO? {
        val video = videoDAO.getDetailById(id)
        video?.commentList = commentDAO.getCommentByVideoId(id)
        video?.recommendList = videoDAO.getRecommendVideoList(id)
        return video
    }

    suspend fun getCommentList(id: String): List<CommentDTO> {
        return commentDAO.getCommentByVideoId(id)
    }
}