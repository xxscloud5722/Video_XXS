package com.xxscloud.videox.module.api


import com.google.inject.Inject
import com.xxscloud.videox.config.ApiResponse
import com.xxscloud.videox.data.UserDO
import com.xxscloud.videox.exception.ParameterException
import com.xxscloud.videox.module.BaseModule
import com.xxscloud.videox.service.api.UserService
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.TimeoutHandler


class UserModule @Inject constructor(router: Router, private val userService: UserService) : BaseModule() {


    init {
        addRouter(router.post("/user/login")).handler(TimeoutHandler.create(5000)).coroutineHandler(::login)
        addRouter(router.post("/user/getUserInfo")).handler(TimeoutHandler.create(5000)).coroutineHandler(::getUserInfo)
    }

    private suspend fun login(content: RoutingContext) {
        val user = getBody(content, UserDO::class.java)
        if (user.account.isEmpty() || user.password.isEmpty()) {
            throw ParameterException("参数异常")
        }
        content.response().end(ApiResponse.success(userService.login(user.account, user.password)).toString())
    }

    private suspend fun getUserInfo(content: RoutingContext) {
        val user = getBody(content, UserDO::class.java)
        if (user.id.isEmpty()) {
            throw ParameterException("参数异常")
        }
        content.response().end(ApiResponse.success(userService.getUserInfo(user.id)).toString())
    }

}