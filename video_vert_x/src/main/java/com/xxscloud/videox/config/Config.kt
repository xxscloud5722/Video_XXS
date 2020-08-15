package com.xxscloud.videox.config


import com.xxscloud.videox.core.SnowFlake
import java.util.*

class Config {
    companion object {
        const val R_N_ACTIVITY = "ACTIVITY:"
        const val R_N_REPORT = "REPORT:"
        const val R_N_REGISTERED = "REGISTERED:"
        const val R_N_TERMINAL_OPEN = "TERMINAL:OPEN:"
        const val R_N_TERMINAL = "TERMINAL:"
        const val R_N_USER_INFO = "TERMINAL:"
        const val R_N_PAY = "PAY:"
        const val R_N_API_LOGIN = "LOGIN:"
        const val R_T_30M = 30 * 60 * 1000L
        const val R_T_180M = 180 * 60

        const val R_N_WX_USER_INFO = "WX:USER_INFO:"
        const val R_N_WX_CREATE_QR = "WX:CREATE_QR:"


        fun getRandomCode(): String {
            return String.format("%04d", Random().nextInt(9999))
        }

        fun getWarehouseUserId(): String {
            return "WU" + SnowFlake.getId()
        }

        fun getReportId(): String {
            return "R" + SnowFlake.getId()
        }

        fun getCustomerId(): String {
            return "CU" + SnowFlake.getId()
        }

        fun getResourcesId(): String {
            return "RS" + SnowFlake.getId()
        }

        fun getOrderNo(): String {
            return "D" + SnowFlake.getId()
        }

        fun getRefundOrderNo(): String {
            return "R" + SnowFlake.getId()
        }

        fun getPayFlowNo(): String {
            return "pay" + SnowFlake.getId()
        }

        fun getFundsFlow(): String {
            return "F" + SnowFlake.getId()
        }

        fun getWithdrawFlow(): String {
            return "W" + SnowFlake.getId()
        }

        fun parseEnv(value: String?): String? {
            if (value.isNullOrEmpty()) {
                return value
            }
            if (value.startsWith("$")) {
                return System.getenv(value.replace("$", "").replace("{", "").replace("}", ""))
            }
            return value
        }
    }
}