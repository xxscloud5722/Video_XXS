package com.xxscloud.videox.module.api

import com.google.inject.Inject
import com.xxscloud.videox.config.ApiResponse
import com.xxscloud.videox.module.BaseModule
import com.xxscloud.videox.service.api.ProductService
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.TimeoutHandler

class ProductModule @Inject constructor(router: Router,  private val productService: ProductService)  : BaseModule() {


    init {
        addRouter(router.post("/product/getList")).handler(TimeoutHandler.create(5000)).coroutineHandler(this::getList)
    }

    private suspend fun getList(content: RoutingContext) {
        content.response().end(ApiResponse.success(productService.getList()).toString())
    }
}