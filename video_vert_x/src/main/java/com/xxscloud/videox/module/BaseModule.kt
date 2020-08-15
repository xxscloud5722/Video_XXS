package com.xxscloud.videox.module

import com.google.gson.GsonBuilder
import com.xxscloud.videox.config.USession
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


abstract class BaseModule {
    companion object {
        private val GSON = GsonBuilder().create()
    }

    fun addRouter(route: Route, fn: suspend (RoutingContext) -> Unit): Route {
        return route
                .coroutineHandler { c ->
                    c.response().headers().add("content-type", "application/json; charset=utf-8")
                    c.next()
                }.coroutineHandler { c ->
                    fn(c)
                }
    }

    fun addRouter(route: Route): Route {
        return route
                .coroutineHandler { c ->
                    c.response().headers().add("content-type", "application/json; charset=utf-8")
                    c.next()
                }
    }

    fun <T> getBody(content: RoutingContext, clazz: Class<T>): T {
        val result = GSON.fromJson(content.bodyAsString, clazz)
        return result ?: clazz.newInstance()
    }

    fun getBody(content: RoutingContext): JsonObject {
        return content.bodyAsJson
    }

    fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit): Route {
        return handler { ctx ->
            GlobalScope.launch(ctx.vertx().dispatcher()) {
                try {
                    fn(ctx)
                } catch (e: Exception) {
                    ctx.fail(e)
                } catch (e: Throwable) {
                    ctx.fail(e)
                }
            }
        }
    }

    fun Route.coroutineHandler(fn: suspend (RoutingContext, USession) -> Unit): Route {
        return handler { ctx ->
            GlobalScope.launch(ctx.vertx().dispatcher()) {
                try {
                    fn(ctx, USession())
                } catch (e: Exception) {
                    ctx.fail(e)
                } catch (e: Throwable) {
                    ctx.fail(e)
                }
            }
        }
    }


}