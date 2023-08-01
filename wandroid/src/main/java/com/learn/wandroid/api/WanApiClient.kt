package com.learn.wandroid.api

import com.learn.common.log.LogUtils
import com.learn.common.okretro.BaseResponse
import com.learn.common.okretro.DataCallback
import com.learn.wandroid.api.WandroidService
import com.learn.wandroid.ui.home.bean.ArticleList
import com.learn.wandroid.ui.home.bean.Banner
import retrofit2.Call

class WanApiClient {
    init {
        apiService = ServiceGenerator.createService(WandroidService::class.java)
    }

    private fun <T> enqueue(call: Call<BaseResponse<T>>, callback: DataCallback<T>?) {
        if (callback == null) {
            LogUtils.e(TAG, "callback is null")
            return
        }
        call.enqueue(callback)
    }

    /**
     * 首页文章列表
     *
     * @param callback Callback.
     */
    fun listHomeArticles(page: Int, callback: DataCallback<ArticleList>?) {
        enqueue(apiService.listHomeArticles(page), callback)
    }

    fun requestBanner(callback: DataCallback<List<Banner>>?) {
        enqueue(apiService.requestBanner(), callback)
    }

    companion object {
        private const val TAG = "WandroidApis"

        private var apiClient: WanApiClient? = null

        private lateinit var apiService: WandroidService

        val instance: WanApiClient?
            get() {
                if (apiClient == null) {
                    apiClient = WanApiClient()
                }
                return apiClient
            }
    }
}