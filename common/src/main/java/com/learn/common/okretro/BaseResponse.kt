package com.learn.common.okretro

data class BaseResponse<T>(val data: T, val code: Int, val message: String){
    fun isSuccess() = code == 0
}
