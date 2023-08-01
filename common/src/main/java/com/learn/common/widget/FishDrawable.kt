package com.learn.common.widget

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class FishDrawable : Drawable() {
    private val OTHER_ALPHA = 110

    private val BODY_ALPHA = 160

    // 鱼的重心
    var middlePoint: PointF
        get
        set

    // 鱼的主要朝向角度
    var fishMainAngle = 90f

    // 绘制鱼头的半径
    val HEAD_RADIUS = 50f

    // 鱼身长度
    private val BODY_LENGTH = HEAD_RADIUS * 3.2f

    // 寻找鱼鳍起始点坐标的线长
    private val FIND_FINS_LENGTH = 0.9f * HEAD_RADIUS

    // 鱼鳍的长度
    private val FINS_LENGTH = 1.3f * HEAD_RADIUS

    // 大圆的半径
    private val BIG_CIRCLE_RADIUS = 0.7f * HEAD_RADIUS

    // 中圆的半径
    private val MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS

    // 小圆半径
    private val SMALL_CIRCLE_RADIUS = 0.4f * MIDDLE_CIRCLE_RADIUS

    // --寻找尾部中圆圆心的线长
    private val FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6f + 1)

    // --寻找尾部小圆圆心的线长
    private val FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f)

    // --寻找大三角形底边中心点的线长
    private val FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f

    private var path: Path = Path()

    private var paint: Paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        isDither = true
        setARGB(OTHER_ALPHA, 244, 92, 71)
    }

    private var currentValue = 0f

    lateinit var headPoint: PointF

    var frequence = 1f
        set(value) {
            field = value
        }

    init {
        middlePoint = PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS)

        // 1.2*n = 整数 1.5 *n = 整数 ==》 公倍数
        ValueAnimator.ofFloat(0f, 3600f).apply {
            duration = (15 * 1000).toLong() // 动画周期
            repeatMode = ValueAnimator.RESTART // 重复的模式：重新开始
            repeatCount = ValueAnimator.INFINITE // 重复的次数
            interpolator = LinearInterpolator()
            addUpdateListener { animator ->
                currentValue = animator.animatedValue as Float
                invalidateSelf()
            }
        }.start()
    }

    override fun draw(canvas: Canvas) {
        // sin(currentValue*2) ==> 0-3600 1s, 10次，20次
        val fishAngle =
            (fishMainAngle + sin(Math.toRadians(currentValue * 1.2 * frequence)) * 10).toFloat()

        // 鱼头的圆心坐标
        headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle)
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, paint)

        // 画右鱼鳍
        val rightFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle - 110)
        makeFins(canvas, rightFinsPoint, fishAngle, true)

        // 画左鱼鳍
        val leftFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle + 110)
        makeFins(canvas, leftFinsPoint, fishAngle, false)

        val bodyBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle - 180)

        // 画节肢1
        val middleCenterPoint = makeSegment(
            canvas, bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, MIDDLE_CIRCLE_RADIUS,
            FIND_MIDDLE_CIRCLE_LENGTH, fishAngle, true
        )

        // 画节肢2
        makeSegment(
            canvas, middleCenterPoint!!, MIDDLE_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS,
            FIND_SMALL_CIRCLE_LENGTH, fishAngle, false
        )

        val findEdgeLength =
            abs(sin(Math.toRadians(currentValue * 1.5 * frequence)) * BIG_CIRCLE_RADIUS)
                .toFloat()

        // 尾巴
        makeTriangel(canvas, middleCenterPoint, FIND_TRIANGLE_LENGTH, findEdgeLength, fishAngle)
        makeTriangel(
            canvas, middleCenterPoint, FIND_TRIANGLE_LENGTH - 10,
            findEdgeLength - 20, fishAngle
        )

        // 身体
        makeBody(canvas, headPoint, bodyBottomCenterPoint, fishAngle)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return (8.38f * HEAD_RADIUS).toInt()
    }

    override fun getIntrinsicHeight(): Int {
        return (8.38f * HEAD_RADIUS).toInt()
    }

    private fun makeBody(
        canvas: Canvas,
        headPoint: PointF,
        bodyBottomCenterPoint: PointF,
        fishAngle: Float
    ) {
        // 身体的四个点求出来
        val topLeftPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle + 90)
        val topRightPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle - 90)
        val bottomLeftPoint = calculatePoint(
            bodyBottomCenterPoint, BIG_CIRCLE_RADIUS,
            fishAngle + 90
        )
        val bottomRightPoint = calculatePoint(
            bodyBottomCenterPoint, BIG_CIRCLE_RADIUS,
            fishAngle - 90
        )

        // 二阶贝塞尔曲线的控制点 --- 决定鱼的胖瘦
        val controlLeft = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130)
        val controlRight = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130)

        // 绘制
        path.apply {
            reset()
            moveTo(topLeftPoint.x, topLeftPoint.y)
            quadTo(controlLeft.x, controlLeft.y, bottomLeftPoint.x, bottomLeftPoint.y)
            lineTo(bottomRightPoint.x, bottomRightPoint.y)
            quadTo(controlRight.x, controlRight.y, topRightPoint.x, topRightPoint.y)
        }
        paint.alpha = BODY_ALPHA
        canvas.drawPath(path, paint)
    }

    private fun makeTriangel(
        canvas: Canvas, startPoint: PointF, findCenterLength: Float,
        findEdgeLength: Float, fishAngle: Float
    ) {
        val triangelAngle =
            (fishAngle + Math.sin(Math.toRadians(currentValue * 1.5 * frequence)) * 35).toFloat()

        // 三角形底边的中心坐标
        val centerPoint = calculatePoint(startPoint, findCenterLength, triangelAngle - 180)
        // 三角形底边两点
        val leftPoint = calculatePoint(centerPoint, findEdgeLength, triangelAngle + 90)
        val rightPoint = calculatePoint(centerPoint, findEdgeLength, triangelAngle - 90)
        path!!.reset()
        path!!.moveTo(startPoint.x, startPoint.y)
        path!!.lineTo(leftPoint.x, leftPoint.y)
        path!!.lineTo(rightPoint.x, rightPoint.y)
        canvas.drawPath(path!!, paint)
    }

    private fun makeSegment(
        canvas: Canvas, bottomCenterPoint: PointF, bigRadius: Float, smallRadius: Float,
        findSmallCircleLength: Float, fishAngle: Float, hasBigCircle: Boolean
    ): PointF? {
        val segmentAngle: Float
        segmentAngle = if (hasBigCircle) {
            // 节肢1
            (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5 * frequence)) * 15).toFloat()
        } else {
            // 节肢2
            (fishAngle + Math.sin(Math.toRadians(currentValue * 1.5 * frequence)) * 35).toFloat()
        }


        // 梯形上底圆的圆心
        val upperCenterPoint = calculatePoint(
            bottomCenterPoint, findSmallCircleLength,
            segmentAngle - 180
        )
        // 梯形的四个点
        val bottomLeftPoint = calculatePoint(bottomCenterPoint, bigRadius, segmentAngle + 90)
        val bottomRightPoint = calculatePoint(bottomCenterPoint, bigRadius, segmentAngle - 90)
        val upperLeftPoint = calculatePoint(upperCenterPoint, smallRadius, segmentAngle + 90)
        val upperRightPoint = calculatePoint(upperCenterPoint, smallRadius, segmentAngle - 90)
        if (hasBigCircle) {
            // 画大圆 --- 只在节肢1 上才绘画
            canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, bigRadius, paint)
        }
        // 画小圆
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, smallRadius, paint)

        // 画梯形
        path!!.reset()
        path!!.moveTo(upperLeftPoint.x, upperLeftPoint.y)
        path!!.lineTo(upperRightPoint.x, upperRightPoint.y)
        path!!.lineTo(bottomRightPoint.x, bottomRightPoint.y)
        path!!.lineTo(bottomLeftPoint.x, bottomLeftPoint.y)
        canvas.drawPath(path!!, paint)
        return upperCenterPoint
    }

    /**
     * 画鱼鳍
     *
     * @param startPoint 起始坐标
     * @param isRight    是否是右鱼鳍
     */
    private fun makeFins(canvas: Canvas, startPoint: PointF, fishAngle: Float, isRight: Boolean) {
        val controlAngle = 115f

        // 鱼鳍的终点 --- 二阶贝塞尔曲线的终点
        val endPoint = calculatePoint(startPoint, FINS_LENGTH, fishAngle - 180)
        // 控制点
        val controlPoint = calculatePoint(
            startPoint, FINS_LENGTH * 1.8f,
            if (isRight) fishAngle - controlAngle else fishAngle + controlAngle
        )
        // 绘制
        path!!.reset()
        // 将画笔移动到起始点
        path!!.moveTo(startPoint.x, startPoint.y)
        // 二阶贝塞尔曲线
        path!!.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y)
        canvas.drawPath(path!!, paint)
    }

    /**
     * @param startPoint 起始点坐标
     * @param length     要求的点到起始点的直线距离 -- 线长
     * @param angle      鱼当前的朝向角度
     * @return
     */
    fun calculatePoint(startPoint: PointF, length: Float, angle: Float): PointF {
        val deltaX = (cos(Math.toRadians(angle.toDouble())) * length).toFloat()
        val deltaY = (sin(Math.toRadians((angle - 180).toDouble())) * length).toFloat()
        return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
    }
}