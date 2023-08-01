package com.learn.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import com.learn.common.R
import com.learn.common.utils.Utils
import com.learn.common.utils.dp2px

open class RoundFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var path: Path? = null

    var radius: Float = 60f.dp2px(context)
        set(value) {
            field = value
            setLayer()
        }

    init {
        val attributes = context.obtainStyledAttributes(
            attrs,
            intArrayOf(R.attr.cornerRadius),
            defStyleAttr,
            0
        )
        val cornerRadius = attributes.getDimension(0, 0f)
        attributes.recycle()
        radius = cornerRadius
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!Utils.isLOrLater) {
            if (path == null && radius != 0f) {
                val rectF = RectF().apply { set(0f, 0f, w.toFloat(), h.toFloat()) }
                path = Path().apply { addRoundRect(rectF, radius, radius, Path.Direction.CW) }
            }
        }
    }

    override fun draw(canvas: Canvas?) {
        if (!Utils.isLOrLater) {
            path?.apply { canvas?.clipPath(this) }
        }
        super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun setLayer() {
        val notDraw: Boolean = radius <= 0
        setWillNotDraw(notDraw)
        val buildVersion = Build.VERSION.SDK_INT
        if (buildVersion > Build.VERSION_CODES.LOLLIPOP) {
            if (notDraw) {
                clipToOutline = false
            } else {
                clipToOutline = true
                outlineProvider = OutlineProvider()
            }
        } else if (buildVersion < Build.VERSION_CODES.HONEYCOMB
            || buildVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2
        ) {

        } else {
            if (!notDraw and (layerType != LAYER_TYPE_SOFTWARE)) {
                setLayerType(LAYER_TYPE_SOFTWARE, null)
            } else {
                setLayerType(LAYER_TYPE_HARDWARE, null)
            }
        }
    }

    inner class OutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
}