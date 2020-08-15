package com.xxscloud.videox.module.api

import com.google.inject.Inject
import com.xxscloud.videox.config.ApiResponse
import com.xxscloud.videox.module.BaseModule
import com.xxscloud.videox.data.VideoDO
import com.xxscloud.videox.exception.ParameterException
import com.xxscloud.videox.service.api.VideoService
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.TimeoutHandler

class VideoModule @Inject constructor(router: Router, private val videoService: VideoService) : BaseModule() {

    init {

        addRouter(router.post("/video/getDetail")).handler(TimeoutHandler.create(5000)).coroutineHandler(this::getDetail)
        addRouter(router.post("/video/getCommentList")).handler(TimeoutHandler.create(5000)).coroutineHandler(this::getCommentList)

    }

    private suspend fun getDetail(content: RoutingContext) {
        val video = getBody(content, VideoDO::class.java)
        if (video.id.isEmpty()) {
            throw ParameterException("参数异常")
        }
        content.response().end(ApiResponse.success(videoService.getDetail(video.id)).toString())
    }

    private suspend fun getCommentList(content: RoutingContext) {
        val video = getBody(content, VideoDO::class.java)
        if (video.id.isEmpty()) {
            throw ParameterException("参数异常")
        }
        content.response().end(ApiResponse.success(videoService.getCommentList(video.id)).toString())
    }
}