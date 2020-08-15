package com.xxscloud.videox

import com.google.inject.*
import com.xxscloud.videox.config.ApiResponse
import com.xxscloud.videox.core.MySQLCore
import com.xxscloud.videox.core.RedisCore
import com.xxscloud.videox.core.TokenProvider
import com.xxscloud.videox.core.TokenProvider.Companion.authenticateHandler
import com.xxscloud.videox.core.TokenProvider.Companion.checkToken
import com.xxscloud.videox.exception.*
import com.xxscloud.videox.module.admin.UserAdminModule
import com.xxscloud.videox.module.api.*
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.slf4j.LoggerFactory


class WebApplication : CoroutineVerticle() {
    private val log = LoggerFactory.getLogger(WebApplication::class.java)
    override suspend fun start() {
        super.start()

        //初始化Redis
        RedisCore.init(vertx)
        //初始化MySQL
        MySQLCore.init(vertx)

        //初始化IOC
        val injector: Injector = Guice.createInjector(object : AbstractModule() {
            override fun configure() {

            }

            @Provides
            @Singleton
            fun getRouter(): Router {
                return Router.router(vertx)
            }

            @Provides
            @Singleton
            fun getMySQL(): MySQLCore {
                return MySQLCore()
            }

            @Provides
            @Singleton
            fun getRedis(): RedisCore {
                return RedisCore()
            }
        })


        val router = injector.getInstance(Router::class.java)
        //token认证
        router.route("/*").handler {
            it.response().headers().add("Access-Control-Allow-Origin", "*")
            it.response().headers().add("Access-Control-Allow-Credentials", "true")
            it.response().headers().add("Access-Control-Allow-Methods", "*")
            it.response().headers().add("Access-Control-Allow-Headers", "x-requested-with,content-type,key,token")
            it.next()
        }.handler(BodyHandler.create()).checkToken()
        //options预处理
        router.options("/*").handler {
            it.response().end()
        }
        //身份认证
        router.route("/admin/*").authenticateHandler("DEFAULT")
        //全局异常
        router.route("/*").failureHandler { content ->
            content.response().headers().add("content-type", "application/json; charset=utf-8")

            val errorMessage: String?
            when (content.failure()) {
                is ParameterException -> {
                    errorMessage = content.failure().localizedMessage
                }
                is ServiceException, is ThirdpartyException, is CoreException, is EventException -> {
                    errorMessage = content.failure().localizedMessage
                    log.error(content.request().path(), content.failure())
                }
                else -> {
                    errorMessage = null
                    log.error(content.request().path(), content.failure())
                }
            }

            val message = (errorMessage ?: "糟糕，服务器飞到火星去了").split("\n")
            content.response().statusCode = 200
            content.response().end((if (message.size > 1)
                ApiResponse.error(message[0], message[1])
            else ApiResponse.error("500", message[0])).toString())
            content.next()
        }


        //模块
        injector.getInstance(TokenProvider::class.java)
        injector.getInstance(UserModule::class.java)
        injector.getInstance(ProductModule::class.java)
        injector.getInstance(VideoModule::class.java)
        injector.getInstance(PayModule::class.java)
        injector.getInstance(UserAdminModule::class.java)
        injector.getInstance(IndexModule::class.java)


        val server = vertx.createHttpServer()
        System.getProperty("server.port")?.let {
            server.requestHandler(router).listen(it.toDouble().toInt())
            log.info("Server initialization completed Port：${it.toInt()}")
        }
    }
}




