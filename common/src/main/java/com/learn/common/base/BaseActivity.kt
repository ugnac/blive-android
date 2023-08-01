package com.learn.common.base

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.learn.common.log.LogUtils

class BaseActivity: AppCompatActivity() {
    companion object {
        const val TAG = "BaseActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        LogUtils.d(TAG, "onCreate")
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onStart() {
        LogUtils.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume() {
        LogUtils.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        LogUtils.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        LogUtils.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        LogUtils.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogUtils.d(TAG, "onKeyDown")
        return super.onKeyDown(keyCode, event)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        LogUtils.d(TAG, "onConfigurationChanged")
        super.onConfigurationChanged(newConfig)
    }
}