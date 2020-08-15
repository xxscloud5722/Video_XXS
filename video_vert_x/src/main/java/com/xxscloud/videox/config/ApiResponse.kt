package com.xxscloud.videox.config


import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.math.BigDecimal


data class ApiResponse(
        val success: Boolean,
        val data: Any? = null,
        val code: String? = null,
        val message: String? = null,
        val other: Any? = null
) {


    class DoubleTypeAdapter : TypeAdapter<Double>() {
        override fun write(out: JsonWriter, value: Double?) {
            if (value == null) {
                out.nullValue()
                return
            }
            out.value(value)
        }

        override fun read(value: JsonReader): Double? {
            if (value.peek() == JsonToken.NULL) {
                return null
            }
            return value.nextDouble()
        }

    }


    class BigDecimalTypeAdapter : TypeAdapter<BigDecimal>() {
        override fun write(out: JsonWriter, value: BigDecimal?) {
            if (value == null) {
                out.nullValue()
                return
            }
            out.value(value.setScale(2, BigDecimal.ROUND_DOWN))
        }

        override fun read(value: JsonReader): BigDecimal? {
            if (value.peek() == JsonToken.NULL) {
                return null
            }
            return BigDecimal.valueOf(value.nextDouble())
        }

    }

    class StringTypeAdapter : TypeAdapter<String>() {
        override fun write(out: JsonWriter, value: String?) {
            if (value.isNullOrEmpty()) {
                out.nullValue()
                return
            }
            out.value(value)
        }

        override fun read(value: JsonReader): String? {
            if (value.peek() == JsonToken.NULL) {
                return null
            }
            return value.nextString()
        }

    }


    companion object {
        private val GSON = GsonBuilder()
                .registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
                .registerTypeAdapter(BigDecimal::class.java, BigDecimalTypeAdapter())
                .registerTypeAdapter(String::class.java, StringTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create()


        fun error(message: String): ApiResponse {
            return ApiResponse(false, null, "9999", message)
        }

        fun error(code: String, message: String?): ApiResponse {
            return ApiResponse(false, null, code, message)
        }

        fun success(): ApiResponse {
            return ApiResponse(true, null, "200", null)
        }

        fun success(data: Any?): ApiResponse {
            return ApiResponse(true, data, "200", null)
        }

        fun success(data: Any?, other: Any?): ApiResponse {
            return ApiResponse(true, data, "200", null, other)
        }

        fun success(data: Any?, message: String?): ApiResponse {
            return ApiResponse(true, data, "200", message)
        }
    }

    override fun toString(): String {
        return GSON.toJson(this)
    }

}