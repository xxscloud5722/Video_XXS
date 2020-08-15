package com.xxscloud.videox.core

import com.xxscloud.videox.config.Config
import com.xxscloud.videox.config.USession
import com.xxscloud.videox.data.UserDO
import com.xxscloud.videox.exception.CoreException
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import io.vertx.ext.web.Route
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TokenProvider {
    companion object {
        private val TOKEN_PROVIDER = TokenProvider()
        private val F = arrayListOf("/user/login")

        fun Route.authenticateHandler(role: String): Route {
            return handler { ctx ->
                GlobalScope.launch(ctx.vertx().dispatcher()) {
                    val user = awaitResult<User> { TOKEN_PROVIDER.authenticate(null, it) }
                    if (user.principal().getJsonArray("role").contains(role)) {
                        ctx.next()
                    } else {
                        ctx.fail(401, CoreException("401\nUnauthorized"))
                    }
                }
            }
        }

        fun Route.checkToken(): Route {
            return handler { ctx ->
                GlobalScope.launch(ctx.vertx().dispatcher()) {
                    val token = ctx.request().headers()["token"]
                    //如果没有token直接返回
                    if (token.isNullOrEmpty()) {
                        ctx.next()
                        return@launch
                    }

                    //如果过滤的地址
                    if (F.contains(ctx.request().path())) {
                        return@launch
                    }

                    //如果有token 则去数据库查询
                    val tokens = token.split("_")
                    if (tokens.size <= 1) {
                        ctx.fail(500, CoreException("401\ntoken error"))
                        return@launch
                    }

                    //查询redis
                    if (RedisCore.getCore().exists(tokens[1])) {
                        ctx.next()
                        RedisCore.getCore().expire(tokens[1], Config.R_T_30M)
                        return@launch
                    }

                    //查询数据库
                    val session = USession()
                    val user = MySQLCore.getCore().queryFirst("""
                        SELECT * FROM v_user WHERE token = ?
                    """, arrayListOf(tokens[1]), UserDO::class.java)
                    user?.let {
                        session.id = user.id
                        session.account = user.account
                        session.token = user.token
                        RedisCore.getCore().setex(tokens[1], Config.R_T_30M, Json.encode(session))
                    }

                    if (session.id.isNotEmpty()) {
                        ctx.next()
                        return@launch
                    }
                    ctx.fail(500, CoreException("401\ntoken error"))
                    return@launch
                }
            }
        }

    }


    class TokenProvider : AuthProvider {
        override fun authenticate(authInfo: JsonObject?, resultHandler: Handler<AsyncResult<User>>?) {
            resultHandler?.handle(Future.succeededFuture(TokenUser()))
        }

        class TokenUser : User {
            override fun clearCache(): User {
                return this
            }

            override fun setAuthProvider(authProvider: AuthProvider?) {
            }

            override fun isAuthorized(authority: String?, resultHandler: Handler<AsyncResult<Boolean>>?): User {
                return this
            }

            override fun principal(): JsonObject {
                val role = JsonArray()
                role.add("DEFAULT")

                val result = JsonObject()
                result.put("role", role)
                return result
            }
        }
    }
}