package com.learn.wandroid.net

import com.learn.common.okretro.BaseResponse
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

// RxJava 自定义 操作符 简单的
// 拦截 自定义操作符，目的: 包装Bean 给拆成两份  如果成功  data--》UI     如果失败  meg--》UI
abstract class APIResponse<T>() // 主构造函数
    : Observer<BaseResponse<T>>{

    private var isShow: Boolean = true

//    // 次构造
//    constructor(context: Context, isShow: Boolean = false) : this(context) {
//        this.isShow = isShow
//    }

    abstract fun success(data: T ?)

    abstract fun failure(errorMsg: String ? )

    // todo +++++++++++++++++++++++++++++++++  RxJava 相关的函数
    // 起点 分发的时候
    override fun onSubscribe(d: Disposable) {
    }

    // 上游流下了的数据
    override fun onNext(t: BaseResponse<T>) {
        if (t.data == null) {
            failure(t.message)
        } else {
            success(t.data)
        }
    }

    // 上游流下了的错误
    override fun onError(e: Throwable) {
        failure(e.message)
    }

    // 停止
    override fun onComplete() {
    }
}