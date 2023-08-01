package com.learn.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val TAG = "FlowLayout"

    // 每个item的横向间距
    private val horizontalSpacing = 16

    private val verticalSpacing = 8

    // 记录所有的行，一行一行的存储，用于layout
    private val allLines: MutableList<List<View>> = ArrayList()

    // 记录每一行的行高，用于layout
    private var lineHeights: MutableList<Int> = ArrayList()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        clearMeasureParams() //内存 抖动

        //先度量孩子
        val childCount = childCount
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        // ViewGroup 解析父亲给我的宽度
        val selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        val selfHeight = MeasureSpec.getSize(heightMeasureSpec)

        // 保存一行中的所有的view
        var lineViews: MutableList<View> = ArrayList()
        var lineWidthUsed = 0
        var lineHeight = 0
        var parentNeededWidth = 0
        var parentNeededHeight = 0

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val childLp = childView.layoutParams
            if (childView.visibility != GONE) {
                // 将layoutParams转变成为 measureSpec
                val childWidthMeasureSpec = getChildMeasureSpec(
                    widthMeasureSpec, paddingLeft + paddingRight, childLp.width
                )
                val childHeightMeasureSpec = getChildMeasureSpec(
                    heightMeasureSpec, paddingTop + paddingBottom, childLp.height
                )
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)

                // 获取子view的度量宽高
                val childMeasuredWidth = childView.measuredWidth
                val childMeasuredHeight = childView.measuredHeight

                // 如果需要换行
                if (childMeasuredWidth + lineWidthUsed + horizontalSpacing > selfWidth) {
                    allLines.add(lineViews)
                    lineHeights.add(lineHeight)
                    parentNeededHeight += lineHeight + verticalSpacing
                    parentNeededWidth =
                        Math.max(parentNeededWidth, lineWidthUsed + horizontalSpacing)
                    lineViews = java.util.ArrayList()
                    lineWidthUsed = 0
                    lineHeight = 0
                }
                lineViews.add(childView)
                lineWidthUsed += childMeasuredWidth + horizontalSpacing
                lineHeight = Math.max(lineHeight, childMeasuredHeight)
                if (i == childCount - 1) {
                    allLines.add(lineViews)
                    lineHeights.add(lineHeight)
                    parentNeededHeight += lineHeight + verticalSpacing
                    parentNeededWidth =
                        Math.max(parentNeededWidth, lineWidthUsed + horizontalSpacing)
                }
            }
        }

        // 再度量自己, 保存
        // 根据子View的度量结果，来重新度量自己ViewGroup
        // 作为一个ViewGroup，它自己也是一个View,它的大小也需要根据它的父亲给它提供的宽高来度量
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val realWidth = if (widthMode == MeasureSpec.EXACTLY) selfWidth else parentNeededWidth
        val realHeight = if (heightMode == MeasureSpec.EXACTLY) selfHeight else parentNeededHeight
        setMeasuredDimension(realWidth, realHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val lineCount = allLines.size

        var curL = paddingLeft
        var curT = paddingTop

        for (i in 0 until lineCount) {
            val lineViews = allLines[i]
            val lineHeight = lineHeights[i]
            for (j in lineViews.indices) {
                val view = lineViews[j]
                val left = curL
                val top = curT

//                int right = left + view.getWidth();
//                int bottom = top + view.getHeight();
                val right = left + view.measuredWidth
                val bottom = top + view.measuredHeight
                view.layout(left, top, right, bottom)
                curL = right + horizontalSpacing
            }
            curT += lineHeight + verticalSpacing
            curL = paddingLeft
        }
    }

    private fun clearMeasureParams() {
        allLines.clear()
        lineHeights.clear()
    }
}