package com.learn.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.learn.common.log.LogUtils

open class BaseFragment : Fragment() {
    companion object {
        const val TAG = "BaseFragment"
    }

    override fun onAttach(context: Context) {
        LogUtils.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtils.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LogUtils.d(TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogUtils.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        LogUtils.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        LogUtils.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        LogUtils.d(TAG, "onDetach")
        super.onDetach()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}