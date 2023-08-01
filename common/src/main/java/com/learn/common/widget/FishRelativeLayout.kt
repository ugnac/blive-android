package com.learn.common.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlin.math.atan2
import kotlin.math.sqrt

class FishRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {
    private var paint: Paint

    private var fishImage: ImageView

    private var fishDrawable: FishDrawable

    private var touchX = 0f

    private var touchY = 0f

    private var ripple = 0f
        set(value) {
            // 透明度的变化 100 - 0
            alpha = (100 * (1 - value)).toInt()
            field = value
        }

    private var alpha = 0

    init {
        // ViewGroup 默认 不执行 onDraw
        setWillNotDraw(false)

        paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = 8f
        }

        fishDrawable = FishDrawable()
        fishImage = ImageView(context).apply {
            val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setLayoutParams(layoutParams)
            setBackgroundColor(Color.CYAN)
            setImageDrawable(fishDrawable)
        }
        addView(fishImage)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.alpha = alpha
        canvas?.drawCircle(touchX, touchY, ripple * 150, paint)

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y

        paint.alpha = 100
        val objectAnimator =
            ObjectAnimator.ofFloat(this, "ripple", 0f, 1f)
                .setDuration(1000)
        objectAnimator.start()

        makeTrail()

        return super.onTouchEvent(event)
    }

    private fun makeTrail() {

        // 鱼的重心：相对ImageView坐标
        val fishRelativeMiddle: PointF = fishDrawable.middlePoint

        // 鱼的重心：绝对坐标 --- 起始点
        val fishMiddle =
            PointF(fishImage.x + fishRelativeMiddle.x, fishImage.y + fishRelativeMiddle.y)
        // 鱼头圆心的坐标 -- 控制点1
        val fishHead = PointF(
            fishImage.x + fishDrawable.headPoint.x,
            fishImage.y + fishDrawable.headPoint.y
        )
        // 点击坐标 -- 结束点
        val touch = PointF(touchX, touchY)
        val angle = includeAngle(fishMiddle, fishHead, touch) / 2
        val delta = includeAngle(fishMiddle, PointF(fishMiddle.x + 1, fishMiddle.y), fishHead)

        // 控制点2 的坐标
        val controlPoint = fishDrawable.calculatePoint(
            fishMiddle,
            fishDrawable.HEAD_RADIUS * 1.6f, angle + delta
        )
        val path = Path()
        path.moveTo(fishMiddle.x - fishRelativeMiddle.x, fishMiddle.y - fishRelativeMiddle.y)
        path.cubicTo(
            fishHead.x - fishRelativeMiddle.x, fishHead.y - fishRelativeMiddle.y,
            controlPoint.x - fishRelativeMiddle.x, controlPoint.y - fishRelativeMiddle.y,
            touchX - fishRelativeMiddle.x, touchY - fishRelativeMiddle.y
        )
        val objectAnimator = ObjectAnimator.ofFloat(fishImage, "x", "y", path)
        objectAnimator.duration = 2000
        objectAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                fishDrawable.frequence = 1f
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                fishDrawable.frequence = 3f
            }
        })
        val pathMeasure = PathMeasure(path, false)
        val tan = FloatArray(2)
        objectAnimator.addUpdateListener { animation ->
            // 执行了整个周期的百分之多少
            val fraction = animation.animatedFraction
            pathMeasure.getPosTan(pathMeasure.length * fraction, null, tan)
            val angle =
                Math.toDegrees(atan2(-tan[1].toDouble(), tan[0].toDouble())).toFloat()
            fishDrawable.fishMainAngle = angle
        }
        objectAnimator.start()
    }

    private fun includeAngle(O: PointF, A: PointF, B: PointF): Float {
        // cosAOB
        // OA*OB=(Ax-Ox)(Bx-Ox)+(Ay-Oy)*(By-Oy)
        val AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y)
        val OALength =
            sqrt(((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y)).toDouble()).toFloat()
        // OB 的长度
        val OBLength =
            sqrt(((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y)).toDouble()).toFloat()
        val cosAOB = AOB / (OALength * OBLength)

        // 反余弦
        val angleAOB = Math.toDegrees(Math.acos(cosAOB.toDouble())).toFloat()

        // AB连线与X的夹角的tan值 - OB与x轴的夹角的tan值
        val direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x)
        return if (direction == 0f) {
            if (AOB >= 0) {
                0f
            } else {
                180f
            }
        } else {
            if (direction > 0) {
                -angleAOB
            } else {
                angleAOB
            }
        }
    }
}