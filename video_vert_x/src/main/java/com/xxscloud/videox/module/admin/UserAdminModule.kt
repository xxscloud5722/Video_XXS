package com.xxscloud.videox.module.admin

import com.google.inject.Inject
import com.xxscloud.videox.config.ApiResponse
import com.xxscloud.videox.module.BaseModule
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class UserAdminModule @Inject constructor(router: Router) : BaseModule() {
    init {
        addRouter(router.post("/admin/user/login")).coroutineHandler(this::login)
    }

    private suspend fun login(content: RoutingContext) {
        content.response().end(ApiResponse.success().toString())
    }
}