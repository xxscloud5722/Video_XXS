package com.xxscloud.videox


import io.vertx.core.Vertx
import org.slf4j.LoggerFactory
import java.util.*


class Application {
    companion object {
        private val VERT_X: Vertx = Vertx.vertx()
        private val log = LoggerFactory.getLogger(Application::class.java)

        @JvmStatic
        fun main(vararg args: String) {
            loadConfig()
            VERT_X.deployVerticle(WebApplication())
        }

        private fun loadConfig() {
            val active = (System.getProperties()["active"] ?: System.getenv("active") ?: "").toString()
            val configPath = "/application${if (active.isEmpty()) "" else "-$active"}.properties";
            val properties = Properties()
            properties.load(VERT_X.javaClass.getResourceAsStream(configPath))
            log.info("Config: $properties")
            properties.forEach {
                System.setProperty(it.key.toString(), it.value.toString())
            }
        }
    }
}