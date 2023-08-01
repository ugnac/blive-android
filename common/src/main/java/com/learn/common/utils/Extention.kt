package com.learn.common.utils

import android.content.Context
import android.util.Property
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import java.io.Closeable
import java.io.IOException

fun Closeable?.closeQuietly() {
    try {
        this?.close()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
}

fun Float.sp2px(context: Context?): Float {
    context ?: return 0f
    context.resources ?: return 0f

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )
}

fun Float.dp2px(context: Context?): Float {
    context ?: return 0f
    context.resources ?: return 0f

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

fun Float.px2dp(context: Context?): Float {
    context ?: return 0f
    context.resources ?: return 0f

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this,
        context.resources.displayMetrics
    )
}

/**
 * 屏幕方向是否是横屏
 */
fun Context?.isLandscape(): Boolean {
    val dm = this?.resources?.displayMetrics ?: return false
    return dm.heightPixels < dm.widthPixels
}

fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

/**
 * 动态设置View的margin值
 */
fun generateMarginLayoutParams(
    targetView: View,
    left: Int, top: Int, right: Int, bottom: Int
): ViewGroup.MarginLayoutParams {
    val params = targetView.layoutParams
    val marginParams = params as? ViewGroup.MarginLayoutParams
        ?: ViewGroup.MarginLayoutParams(params)
    marginParams.setMargins(left, top, right, bottom)
    return marginParams
}

/**
 * View高度属性, 用于属性动画, 动态修改View的高度
 */
val VIEW_HEIGHT: Property<View, Int> =
    object : Property<View, Int>(Int::class.java, "view.height") {
        override fun get(view: View): Int {
            return view.measuredHeight
        }

        override fun set(view: View, value: Int) {
            val param = view.layoutParams.apply {
                height = value
            }
            view.layoutParams = param
        }
    }