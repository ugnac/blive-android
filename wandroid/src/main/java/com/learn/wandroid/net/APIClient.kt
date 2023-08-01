package com.learn.wandroid.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIClient {
    private object Holder {
        val INSTANCE = APIClient()
    }

    // 伴生对象
    companion object {
        val instance = Holder.INSTANCE
    }

    // WanAndroidAPI实例化这个，  XXXAPI实例化这个，   BBBAPI实例化
    fun <T> createService(apiInterface: Class<T>): T {
        // OKHttpClient请求服务器
        val okHttpClient = OkHttpClient().newBuilder()
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com")
            // 请求方  ←
            .client(okHttpClient)
            // 响应方  →
            // Response的事情  回来
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RxJava来处理
            .addConverterFactory(GsonConverterFactory.create()) // Gson 来解析 --- JavaBean
            .build()

        return retrofit.create(apiInterface);
    }
}