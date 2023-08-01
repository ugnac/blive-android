package com.learn.common.widget

import android.content.Context
import android.graphics.*
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.learn.common.R

class PercentBarTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val DEFAULT_COLOR_BG = ContextCompat.getColor(context, R.color.theme_color_common_bg)
    private val DEFAULT_COLOR_FORE = ContextCompat.getColor(context, R.color.theme_color_pink)

    private val mPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    // 不要在onDetachedFromWindow中手动回收, 会导致复用出错
    private var mRectF: RectF? = null
    private var mRectFFg: RectF? = null
    private var mMode: Xfermode? = null
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null

    private var mDrawRatio: Float = 0.toFloat()
    private var mCornerBg: Float = 0.toFloat()
    private var mCornerFg: Float = 0.toFloat()

    private var rWidth: Int = 0

    @ColorInt
    var progressBackgroundColor: Int = 0
    @ColorInt
    var progressColor: Int = 0

    var currentProgress: Long = 0
    var maxProgress: Long = 0

    init {
        includeFontPadding = false
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PercentBarTextView)
            progressBackgroundColor = ta.getColor(R.styleable.PercentBarTextView_pb_color_bg, DEFAULT_COLOR_BG)
            progressColor = ta.getColor(R.styleable.PercentBarTextView_pb_color_fg, DEFAULT_COLOR_FORE)
            mCornerBg = ta.getDimension(R.styleable.PercentBarTextView_pb_corner_bg, 2f)
            mCornerFg = ta.getDimension(R.styleable.PercentBarTextView_pb_corner_fg, 2f)
            ta.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (measuredHeight > 0 && measuredWidth > 0) {
            val rLeft = paddingLeft
            val rRight = measuredWidth - paddingRight
            val rTop = paddingTop
            val rBottom = measuredHeight - paddingBottom
            val rHeight = rBottom - rTop
            rWidth = rRight - rLeft

            mRectF = RectF(rLeft.toFloat(), rTop.toFloat(), rRight.toFloat(), rBottom.toFloat())//背景区域
            mRectFFg = RectF(rLeft.toFloat(), rTop.toFloat(), rWidth * mDrawRatio, rBottom.toFloat())//前景区域
            mBitmap = Bitmap.createBitmap(rWidth, rHeight, Bitmap.Config.ARGB_8888)
            mCanvas = Canvas(mBitmap!!)
            mMode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        }
    }

    //画一个完整的百分比视图
    override fun onDraw(canvas: Canvas) {
        mPaint.apply {
            color = progressBackgroundColor
            mRectF?.let { mCanvas?.drawRoundRect(it, mCornerBg, mCornerBg, this) }//画背景圆角矩形区域

            color = progressColor
            xfermode = mMode
            mRectFFg?.right = rWidth * mDrawRatio
            mRectFFg?.let { mCanvas?.drawRoundRect(it, mCornerFg, mCornerFg, this) }//画百分比区域
            xfermode = null
        }

        //将合成后的视图画在当前View上
        mBitmap?.let { mRectF?.let { rectF -> canvas.drawBitmap(it, null, rectF, mPaint) } }
        setTextColor(Color.WHITE)
        super.onDraw(canvas)
    }

    /**
     * 为保证各种临界情况，建议使用这个方法；
     *
     * @param current 当前值
     * @param max     最大值
     */
    fun setDrawRatio(current: Long, max: Long) {
        currentProgress = current
        maxProgress = max
        mDrawRatio = if (current <= 0) 0f else if (max <= 0) 1f
        else {
            val ratio = (current.toDouble() / max).toFloat()
            if (ratio >= 1) 1f else ratio
        }
    }

    /**
     * 注意调用的地方，除数不为0的情况，将int强转为float即可；
     *
     * @param ratio
     */
    fun setDrawRatio(@FloatRange(from = 0.0, to = 1.0) ratio: Float) {
        mDrawRatio = when {
            ratio <= 0 -> 0f
            ratio >= 1 -> 1f
            else -> ratio
        }
    }
}