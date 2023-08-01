package com.learn.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.learn.common.utils.dp2px

class BannerIndicator @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes), ViewPager.OnPageChangeListener {
    private var pageState = ViewPager.SCROLL_STATE_IDLE

    private var pageCount = 0
        set(value) {
            if (value != pageCount) {
                field = value
            }
            requestLayout()
        }

    private var currentIndex = 0

    private val radius = 8f.dp2px(context)

    private val margin = 8f.dp2px(context)

    private var backgroundPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.BLUE
    }

    private var foregroundPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            return size
        }

        val width =
            (paddingLeft + paddingRight + 2 * radius * pageCount + margin * (pageCount - 1)).toInt()
        return if (mode == Int.MIN_VALUE) width.coerceAtMost(size) else width
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            return size
        }

        val height = (radius * 2 + paddingTop + paddingBottom).toInt()
        return if (mode == Int.MIN_VALUE) height.coerceAtMost(size) else height
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (pageCount == 0) {
            return
        }

        var loop = 0
        val total = pageCount
        val cy = paddingTop + radius
        while (loop < total) {
            val cx = getCX(loop)
            if (backgroundPaint.alpha > 0) {
                canvas?.drawCircle(cx, cy, radius, backgroundPaint)
            }
            loop++
        }

        // 取余
        val currentIndex = this.currentIndex.rem(total)
        canvas?.drawCircle(getCX(currentIndex), cy, radius, foregroundPaint)
    }

    /**
     * 获取圆的中心点x坐标
     *
     * @param index 第几个
     * @return Centre x.
     */
    private fun getCX(index: Int): Float {
        return paddingLeft + index * (2 * radius + margin) + radius
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        currentIndex = position
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {
        pageState = state
    }
}