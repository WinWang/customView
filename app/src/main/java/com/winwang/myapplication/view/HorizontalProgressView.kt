package com.winwang.myapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R
import com.winwang.myapplication.ext.drawCenterText

/**
 * Created by WinWang on 2022/10/24
 * Description:自定义下载进度条
 **/
class HorizontalProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 进度
     */
    var progress = 0.0F
        set(value) {
            field = value
            postInvalidate()
        }

    /**
     * 进度条最大值
     */
    private var maxProgress = 100.0F

    /**
     * 起始颜色值
     */
    private var startColor = 0

    /**
     * 结束颜色
     */
    private var endColor = 0

    /**
     * 进度条背景颜色
     */
    private var backColor = 0

    /**
     * 文字的颜色
     */
    private var textColor = 0

    /**
     * 字体大小
     */
    private var textSizes = 15.0F


    /**
     * 文字画笔
     */
    private val textPaint by lazy {
        TextPaint().apply {
            textSize = textSizes
            style = Paint.Style.FILL
            color = textColor
        }
    }

    /**
     * 背景画笔
     */
    private val backPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
        }
    }


    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressView)
        startColor =
            obtainStyledAttributes.getColor(R.styleable.HorizontalProgressView_HPVstartColor1, ContextCompat.getColor(context, R.color.color_FFA200))
        endColor =
            obtainStyledAttributes.getColor(R.styleable.HorizontalProgressView_HPVendColor1, ContextCompat.getColor(context, R.color.color_FF6900))
        backColor =
            obtainStyledAttributes.getColor(R.styleable.HorizontalProgressView_HPVbackColor1, ContextCompat.getColor(context, R.color.color_F4F4F4))
        progress = obtainStyledAttributes.getFloat(R.styleable.HorizontalProgressView_HPVProgress, 0.0F)
        maxProgress = obtainStyledAttributes.getFloat(R.styleable.HorizontalProgressView_HPVMaxProgress, 100.0F)
        textColor = obtainStyledAttributes.getColor(R.styleable.HorizontalProgressView_HPVTextColor1, ContextCompat.getColor(context, R.color.colorWhite))
        textSizes = obtainStyledAttributes.getDimension(R.styleable.HorizontalProgressView_HPVTextSize, 15f)
        obtainStyledAttributes.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            //绘制背景
            backPaint.color = backColor
            backPaint.shader = null
            val backRectF = RectF(0.0F, 0.0F, width.toFloat(), height.toFloat())
            drawRoundRect(backRectF, height / 2.0f, height / 2.0f, backPaint)

            //绘制进度背景条
            val progressRatio = progress / maxProgress
            val progressWidth = if (progress > 10) width.toFloat() * progressRatio else width.toFloat() * 0.1F
            val linearGradient = LinearGradient(0f, 0f, width.toFloat(), 0f, startColor, endColor, Shader.TileMode.CLAMP)
            backPaint.shader = linearGradient
            val progressRectF = RectF(0.0F, 0.0F, progressWidth, height.toFloat())
            drawRoundRect(progressRectF, height / 2.0f, height / 2.0f, backPaint)

            //绘制进度条文字
            val progressText = "${progress}%"
            val measureTextWidth = textPaint.measureText(progressText)
            val startPositionX = progressWidth - measureTextWidth * 2 - 20
            val textRect = RectF(startPositionX, 0F, progressWidth, height.toFloat())
            drawCenterText(progressText, textRect, textPaint)

        }
    }


}