package com.learn.wandroid.data

import com.learn.wandroid.api.WandroidService
import com.learn.wandroid.data.model.LoggedInUser
import com.learn.wandroid.data.model.LoginResponse
import com.learn.wandroid.data.model.RegisterResponse
import com.learn.wandroid.data.Result
import com.learn.wandroid.net.APIClient
import com.learn.wandroid.net.APIResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // 登录
            APIClient.instance.createService(WandroidService::class.java)
                // 全部都是RxJava知识了
                .loginAction(username, password) // 起点  往下流  ”包装Bean“
                .subscribeOn(Schedulers.io()) // 给上面请求服务器的操作，分配异步线程

                .observeOn(AndroidSchedulers.mainThread()) // 给下面更新UI操作，分配main线程
//                .subscribe(object : Consumer<BaseResponse<LoginResponse>> {
//                    override fun accept(t: BaseResponse<LoginResponse>) {
//                        // 我这里是更新UI，拿到了包装Bean，实际上我不需要包装Bean
//                    }
//                })
                .subscribe(object : APIResponse<LoginResponse>() {
                    override fun success(data: LoginResponse?) {
                        TODO("Not yet implemented")
                    }

                    override fun failure(errorMsg: String?) {
                        TODO("Not yet implemented")
                    }
                })

            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout(username: String, password: String, repassword: String) {
        // TODO: revoke authentication
        try {
            // 登录
            APIClient.instance.createService(WandroidService::class.java)
                // 全部都是RxJava知识了
                .registerAction(username, password, repassword) // 起点  往下流  ”包装Bean“
                .subscribeOn(Schedulers.io()) // 给上面请求服务器的操作，分配异步线程
                .observeOn(AndroidSchedulers.mainThread()) // 给下面更新UI操作，分配main线程

                /*.subscribe(object : Consumer<LoginResponseWrapper<LoginResponse>> {
                    override fun accept(t: LoginResponseWrapper<LoginResponse>?) {
                        // 我这里是更新UI，拿到了包装Bean，实际上我不需要包装Bean
                    }
                })*/
                .subscribe(object : APIResponse<RegisterResponse>() {
                    override fun success(data: RegisterResponse?) {
                        TODO("Not yet implemented")
                    }

                    override fun failure(errorMsg: String?) {
                        TODO("Not yet implemented")
                    }
                })
        } catch (e: Throwable) {
        }
    }
}