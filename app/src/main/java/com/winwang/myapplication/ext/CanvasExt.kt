package com.winwang.myapplication.ext

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint


val drawCenterTextRectF = RectF()

/**
 * 绘制居中文字
 * [text]  要绘制的文字
 * [rectF] 绘制文字的范围
 * [paint] 画笔
 */
fun Canvas.drawCenterText(text: String, rectF: RectF, paint: TextPaint) {
    val fontMetrics: Paint.FontMetrics = paint.fontMetrics
    val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
    val baseline: Float = rectF.centerY() + distance
    drawText(text, rectF.centerX(), baseline, paint)
}

fun Canvas.drawCenterText(text: String, left: Float, top: Float, right: Float, bottom: Float, paint: TextPaint) {
    drawCenterTextRectF.setEmpty()
    drawCenterTextRectF.set(left, top, right, bottom)
    drawCenterText(text, drawCenterTextRectF, paint)
}
