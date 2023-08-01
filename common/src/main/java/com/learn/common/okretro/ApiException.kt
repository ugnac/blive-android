package com.learn.common.okretro

class ApiException(private val code: Int, private val msg: String)
    : Exception(msg) {
    fun canRetry(): Boolean {
        val code = code
        return code != EC_SERVICE_UNAVAILABLE
                && code != EC_ACCOUNT_IS_NOT_LOGIN
                && code != EC_NOT_FOUND
                && code != EC_ACCESS_DENIED
    }

    companion object {
        const val EC_ACCOUNT_IS_NOT_LOGIN = -101
        const val EC_ACCESS_DENIED = -403
        const val EC_NOT_FOUND = -404
        const val EC_SERVICE_UNAVAILABLE = -503
    }
}