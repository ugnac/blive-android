package com.learn.common.okretro

import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response

abstract class DataCallback<T> : BaseCallback<BaseResponse<T>>() {
    abstract fun onDataSuccess(data: T?)

    override fun onSuccess(body: BaseResponse<T>?) {
        throw UnsupportedOperationException()
    }

    override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
        if (isCancel) {
            return
        }

        if (!response.isSuccessful) {
            onFailure(call, HttpException(response))
            return
        }

        val body: BaseResponse<T>? = response.body()
        if (body == null) {
            onDataSuccess(null)
        } else if (!body.isSuccess()) {
            onFailure(call, ApiException(body.code, body.message))
        } else {
            onDataSuccess(body.data)
        }
    }
}