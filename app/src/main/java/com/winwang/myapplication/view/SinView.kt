package com.winwang.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.winwang.myapplication.R
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *Created by WinWang on 2020/12/20
 *Description->
 */
class SinView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), LifecycleObserver {

    companion object {
        const val TAG = "SinView"
    }

    private var mWidth = 0f
    private var mHeight = 0f
    private var mRadius = 0f
    private var mAngle = 10f
    private val solidLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.colorWhite)
    }

    private val fillCirclePaint = Paint().apply {
        style = Paint.Style.FILL
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.colorWhite)
    }

    private val vectorLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        strokeWidth = 5f
    }

    private val dashLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        color = ContextCompat.getColor(context, R.color.colorYellow)
        strokeWidth = 5f
    }

    private var wavePath: Path = Path()


    lateinit var launch: Job

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.d(TAG, "onFinishInflate>>>")
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow>>>")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure>>>")
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout>>>")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "onSizeChanged>>>")
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = if (w < h / 2) w / 2.toFloat() else h / 4.toFloat()
        mRadius -= 20f

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw>>>")
        canvas?.apply {
            drawAxis(this)
            drawDashCircle(this)
            drawVector(this)
            drawProjector(this)
            drawSinPath(this)
        }
    }

    private fun drawSinPath(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            val sampleCount = 50
            val dy = mHeight / 2 / sampleCount //y轴方向的每一个的高度
            wavePath.reset()
            wavePath.moveTo(mRadius * cos(mAngle.toRadius()), 0f)
            repeat(sampleCount) {
                //计算x和y轴的每一次的坐标轴
                var x = mRadius * cos(-it * 0.15f + mAngle.toRadius())
                var y = -dy * it
                wavePath.quadTo(x.toFloat(), y, x.toFloat(), y)
            }
            drawPath(wavePath, vectorLinePaint)
        }

    }

    private fun drawProjector(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawCircle(mRadius * cos(mAngle.toRadius()), 0f, 20f, fillCirclePaint)
        }

        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(mRadius * cos(mAngle.toRadius()), 0f, 20f, fillCirclePaint)
        }

        //绘制虚线和实线
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            val x = mRadius * cos(mAngle.toRadius())
            val y = mRadius * sin(mAngle.toRadius())
            withTranslation(x, -y) {
                drawLine(0f, 0f, 0f, y, solidLinePaint)
                drawLine(0f, 0f, 0f, -mHeight / 4 + y, dashLinePaint)
            }
        }

    }


    private fun drawDashCircle(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawCircle(0f, 0f, mRadius, dashLinePaint)
        }
    }

    private fun drawAxis(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 2) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
            drawLine(0f, -mHeight / 2, 0f, mHeight / 2, solidLinePaint)
        }

        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            drawLine(-mWidth / 2, 0f, mWidth / 2, 0f, solidLinePaint)
        }
    }

    private fun drawVector(canvas: Canvas) {
        canvas.withTranslation(mWidth / 2, mHeight / 4 * 3) {
            withRotation(-mAngle) { //通过画布旋转以后，绘制图形不需要通过三角函数计算角度了，方便绘制，
                drawLine(0f, 0f, mRadius, 0f, vectorLinePaint)
            }
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow>>>")
    }

    /**
     * 开始旋转
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startRotating() {
        launch = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(100)
                mAngle += 5
                invalidate()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseRotating() {
        launch.cancel()
    }


    private fun Float.toRadius() = this / 180 * PI.toFloat()


}