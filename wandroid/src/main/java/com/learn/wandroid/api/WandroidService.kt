package com.learn.wandroid.api

import com.learn.common.okretro.BaseResponse
import com.learn.wandroid.data.model.LoginResponse
import com.learn.wandroid.data.model.RegisterResponse
import com.learn.wandroid.ui.home.bean.ArticleList
import com.learn.wandroid.ui.home.bean.Banner
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// 客户端API 可以访问 服务器的API
interface WandroidService {
    @GET("/article/list/{page}/json")
    fun listHomeArticles(@Path("page") page: Int): Call<BaseResponse<ArticleList>>

    @GET("/banner/json")
    fun requestBanner(): Call<BaseResponse<List<Banner>>>

    /**
     * 登录API
     * username=Derry-vip&password=123456
     */
    @POST("/user/login")
    @FormUrlEncoded
    fun loginAction(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<BaseResponse<LoginResponse>> // 返回值

    /**
     * 注册的API
     */
    @POST("/user/register")
    @FormUrlEncoded
    fun registerAction(@Field("username") username: String,
                       @Field("password") password: String,
                       @Field("repassword") repassword: String)
            : Observable<BaseResponse<RegisterResponse>> // 返回值
}