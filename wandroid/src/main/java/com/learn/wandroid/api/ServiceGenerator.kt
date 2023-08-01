package com.learn.wandroid.api

import com.learn.common.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    private var httpConfig = HttpClientConfig()

    @Volatile
    private var httpClient: OkHttpClient? = null

    @Volatile
    private var _retrofit: Retrofit? = null

    fun <T> createService(cls: Class<T>): T {
        return retrofit.create(cls)
    }

    private val retrofit: Retrofit
        get() {
            if (_retrofit == null) {
                synchronized(ServiceGenerator::class.java) {
                    if (_retrofit == null) {
                        _retrofit = Retrofit.Builder()
                            .baseUrl(Const.BASE_URI_WANANDROID)
                            .client(newOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                    }
                }
            }
            return _retrofit!!
        }

    private fun newOkHttpClient(): OkHttpClient {
        if (httpClient == null) {
            synchronized(ServiceGenerator::class.java) {
                if (httpClient == null) {
                    val newBuilder = OkHttpClient.Builder().apply {
                        connectTimeout(httpConfig.connectTimeout, TimeUnit.MILLISECONDS)
                        readTimeout(httpConfig.readTimeout, TimeUnit.MILLISECONDS)
                        writeTimeout(httpConfig.writeTimeout, TimeUnit.MILLISECONDS)
                        interceptors().addAll(httpConfig.interceptors)
                        networkInterceptors().addAll(httpConfig.networkInterceptors)
                    }
                    httpClient = newBuilder.build()
                }
            }
        }
        return httpClient!!
    }

    /**
     * OkHttpClient 配置项
     */
    class HttpClientConfig {
        val connectTimeout: Long = 6000
        val readTimeout: Long = 6000
        val writeTimeout: Long = 6000
        val interceptors: List<Interceptor> = ArrayList(5)
        val networkInterceptors: List<Interceptor> = ArrayList(5)
    }
}