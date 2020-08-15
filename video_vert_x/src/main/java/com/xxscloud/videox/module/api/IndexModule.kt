package com.xxscloud.videox.module.api

import com.google.inject.Inject
import com.xxscloud.videox.config.ApiResponse
import com.xxscloud.videox.exception.ParameterException
import com.xxscloud.videox.module.BaseModule
import com.xxscloud.videox.service.api.IndexService
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.TimeoutHandler

class IndexModule @Inject constructor(router: Router, private val indexService: IndexService) : BaseModule() {
    init {
        addRouter(router.post("/index/getActivityItemList")).handler(TimeoutHandler.create(5000)).coroutineHandler(this::getActivityItemList)
        addRouter(router.post("/index/getActivityItemVideoList")).handler(TimeoutHandler.create(5000)).coroutineHandler(this::getActivityItemVideoList)
    }

    private suspend fun getActivityItemList(content: RoutingContext) {
        content.response().end(ApiResponse.success(indexService.getActivityItemList("1")).toString())
    }

    private suspend fun getActivityItemVideoList(content: RoutingContext) {
        val args = getBody(content)
        if (args.getString("id").isNullOrEmpty()) {
            throw ParameterException("参数异常")
        }
        content.response().end(ApiResponse.success(indexService.getActivityItemVideoList(args.getString("id"))).toString())
    }
}