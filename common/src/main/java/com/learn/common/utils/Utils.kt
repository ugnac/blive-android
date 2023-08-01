package com.learn.common.utils

import android.os.Build

object Utils {
    /**
     * @return `true` if the device is [Build.VERSION_CODES.LOLLIPOP] or later
     */
    val isLOrLater: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    /**
     * @return `true` if the device is [Build.VERSION_CODES.LOLLIPOP_MR1] or later
     */
    val isLMR1OrLater: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

    /**
     * @return `true` if the device is [Build.VERSION_CODES.M] or later
     */
    val isMOrLater: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /**
     * @return `true` if the device is [Build.VERSION_CODES.O] or later
     */
    val isOOrLater: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * @return {@code true} if the device is {@link Build.VERSION_CODES#P} or later
     */
    val isPOrLater: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}