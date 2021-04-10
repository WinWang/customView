package com.winwang.myapplication.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R

/**
 *Created by WinWang on 2021/4/8
 *Description->
 */
class VerticalProgressView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var padding = 10f  //进度条左右内边距
    private var gap = 20f //进度条和文字之间的距离
    private var startColor = 0
    private var endColor = 0
    private var grayColor = 0
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG) //进度条画笔
    private val barPaintBack = Paint(Paint.ANTI_ALIAS_FLAG) //进度条画笔
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 40f //进度
    private var axisTextSize = 40f
    private var progressTextSize = 40f
    private var axisTextColor = 0
    private var progressTextColor = 0
    private val topLevel = "100"
    private val lowLevel = "0"

    init {
        initView(context, attrs)
        startColor = ContextCompat.getColor(context, R.color.COLOR_F9F464)
        endColor = ContextCompat.getColor(context, R.color.COLOR_E94608)
        grayColor = ContextCompat.getColor(context, R.color.COLOR_F5F5F5)

        //初始化进度条画笔
        barPaint.style = Paint.Style.FILL
        barPaintBack.style = Paint.Style.FILL
        textPaint.style = Paint.Style.STROKE
        textPaint.textSize = axisTextSize
        barPaintBack.color = grayColor

    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.VerticalProgressView)
        progressTextSize = obtainStyledAttributes.getDimension(R.styleable.VerticalProgressView_VPVProgressTextSize, 25f)
        axisTextSize = obtainStyledAttributes.getDimension(R.styleable.VerticalProgressView_VPVAxisTextSize, 25f)
        axisTextColor = obtainStyledAttributes.getColor(R.styleable.VerticalProgressView_VPVAxisTextColor, ContextCompat.getColor(context, R.color.BLACK))
        progressTextColor = obtainStyledAttributes.getColor(R.styleable.VerticalProgressView_VPVProgressTextColor, ContextCompat.getColor(context, R.color.BLACK))
        gap = obtainStyledAttributes.getDimension(R.styleable.VerticalProgressView_VPVgapWidth, 5f)
        padding = obtainStyledAttributes.getDimension(R.styleable.VerticalProgressView_VPVpadding, 5f)
        obtainStyledAttributes.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawProgress(it)
        }
    }

    fun setVerticalProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }


    private fun drawProgress(canvas: Canvas) {
        val barWidth = width / 2  //进度条宽度
        /**
         * 绘制背景
         */
        barPaint.color = grayColor
        //背景条的灰度长方形
        val rect = RectF(0f, barWidth / 2.toFloat(), barWidth.toFloat(), height.toFloat())
        val percentValue = rect.height() / 100
        //半弧形的圆矩形
        val rectArc = RectF(0f, 0f, barWidth.toFloat(), barWidth.toFloat() + 1)
        canvas.drawRect(rect, barPaintBack)
        canvas.drawArc(rectArc, -180f, 180f, true, barPaintBack)

        /**
         * 绘制进度
         */
        val progressValue = percentValue * progress
        val rectInner = RectF(padding, height.toFloat() - progressValue, barWidth.toFloat() - padding, height.toFloat())
        val linearGradient = LinearGradient(0f, height.toFloat(), 0f, height.toFloat() - progressValue, startColor, endColor, Shader.TileMode.CLAMP)
        val rectInnerArc = RectF(padding, height.toFloat() - progressValue - rectInner.width() / 2, barWidth.toFloat() - padding, height.toFloat() - progressValue + rectInner.width() / 2 + 1)
        barPaint.shader = linearGradient
        canvas.drawRect(rectInner, barPaint)
        canvas.drawArc(rectInnerArc, -180f, 180f, true, barPaint)

        /**
         * 绘制文字
         */
        textPaint.textSize = axisTextSize
        textPaint.color = axisTextColor
        //绘制顶部
        canvas.drawText(topLevel, barWidth.toFloat() + gap, 40f, textPaint)
        //绘制底部
        canvas.drawText(lowLevel, barWidth.toFloat() + gap, height.toFloat(), textPaint)
        //绘制动态范围文字
        var dynamicY = height.toFloat() - progressValue
        if (progress < 5) {
            dynamicY = height.toFloat() - percentValue * 5
        } else if (progress > 95) {
            dynamicY = height.toFloat() - percentValue * 95
        }
        textPaint.textSize = progressTextSize
        textPaint.color = progressTextColor
        canvas.drawText("$progress", barWidth.toFloat() + gap, dynamicY, textPaint)


    }


}