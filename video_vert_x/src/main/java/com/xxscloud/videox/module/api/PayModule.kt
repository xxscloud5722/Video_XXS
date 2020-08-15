package com.xxscloud.videox.module.api

import com.google.inject.Inject
import com.xxscloud.videox.config.ApiResponse
import com.xxscloud.videox.module.BaseModule
import com.xxscloud.videox.service.api.PayService
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.TimeoutHandler


class PayModule @Inject constructor(router: Router, private val payService: PayService) : BaseModule() {


    init {
        addRouter(router.post("/pay/getPayMethod")).handler(TimeoutHandler.create(5000)).coroutineHandler(this::getPayMethod)
    }

    private suspend fun getPayMethod(content: RoutingContext) {
        content.response().end(ApiResponse.success(payService.getPayMethod()).toString())
    }

}