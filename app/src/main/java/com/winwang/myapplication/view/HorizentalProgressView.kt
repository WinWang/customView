package com.winwang.myapplication.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R

/**
 *Created by WinWang on 2021/4/9
 *Description->
 */
class HorizentalProgressView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintBack = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG)
    private var startColor = 0
    private var endColor = 0
    private var backColor = 0
    private var padding = 10f //进度条内边距
    private var progress = 40 //进度条

    init {
        initView(context, attrs)
        paintBack.style = Paint.Style.FILL
        paintProgress.style = Paint.Style.FILL
        paintBack.color = backColor


    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.HorizentalProgressView)
        padding = obtainStyledAttributes.getDimension(R.styleable.HorizentalProgressView_HPVpadding, 5f)
        startColor = obtainStyledAttributes.getColor(R.styleable.HorizentalProgressView_HPVstartColor, ContextCompat.getColor(context, R.color.COLOR_FEAA37))
        endColor = obtainStyledAttributes.getColor(R.styleable.HorizentalProgressView_HPVstartColor, ContextCompat.getColor(context, R.color.COLOR_B30C0C))
        backColor = obtainStyledAttributes.getColor(R.styleable.HorizentalProgressView_HPVstartColor, ContextCompat.getColor(context, R.color.COLOR_F5F5F5))
        obtainStyledAttributes.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawProgressAndBack(this)
        }
    }

    private fun drawProgressAndBack(canvas: Canvas) {
        /**
         * 绘制进度条背景色
         */
        val backPath = Path()
        backPath.moveTo(0f, 0f)
        backPath.lineTo(width - height / 2f, 0f)
        backPath.arcTo(width - height.toFloat(), 0f, width.toFloat(), height.toFloat(), -90f, 180f, false)
        backPath.lineTo(0f, height.toFloat())
        backPath.close()
        canvas.drawPath(backPath, paintBack)

        /**
         * 绘制进度条-获取进度条
         */
        val linearGradient = LinearGradient(0f, 0f, width.toFloat(), 0f, startColor, endColor, Shader.TileMode.CLAMP)
        paintProgress.shader = linearGradient
        val percentValue = progress / 100f
        //获取进度绘制区间
        val percentTotalWidth = width - padding * 2f
        val percentTotalHeight = height - padding * 2f
        val percentWidth = (percentTotalWidth - percentTotalHeight / 2) * percentValue //进度的真实宽度
        val progressPath = Path()
        progressPath.moveTo(padding, padding)
        progressPath.lineTo(padding + percentWidth, padding)
        progressPath.arcTo(padding + percentWidth - percentTotalHeight / 2, padding, padding + percentWidth + percentTotalHeight / 2, height - padding, -90f, 180f, false)
        progressPath.lineTo(padding, height.toFloat() - padding)
        progressPath.close()
        canvas.drawPath(progressPath, paintProgress)
    }


}