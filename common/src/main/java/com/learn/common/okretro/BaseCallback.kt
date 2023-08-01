package com.learn.common.okretro

import android.util.Log
import com.learn.common.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseCallback<T> : Callback<T> {
    abstract fun onSuccess(body: T?)

    abstract fun onError(th: Throwable?)

    val isCancel: Boolean
        get() = false

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (isCancel) {
            return
        }
        if (!response.isSuccessful) {
            onFailure(call, HttpException(response))
            return
        }
        onSuccess(response.body())
    }

    override fun onFailure(call: Call<T>, th: Throwable) {
        if (isCancel) {
            return
        }
        if (Config.isDebuggable) {
            if (call != null) {
                Log.w("onFailure", call.request().url().toString() + " " + th.message)
            } else {
                Log.w("onFailure", "", th)
            }
        }
        onError(th)
    }
}