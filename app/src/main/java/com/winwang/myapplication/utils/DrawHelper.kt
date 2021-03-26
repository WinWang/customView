package com.winwang.myapplication.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View

/**
 * 通用绘制文字居中方法
 * @param canvas    画布
 * @param left      文字左边界
 * @param right     文字右边界
 * @param top       文字上边界
 * @param bottom    文字下边界
 * @param title     文字内容
 * @param paint     画笔
 */
fun View.drawCenterText(
        canvas: Canvas?,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        title: String,
        paint: Paint
) {
    val rectF = RectF()
    rectF.set(left, top, right, bottom)
    val fontMetrics = paint.fontMetrics
    val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
    val baseline = rectF.centerY() + distance
    canvas?.drawText(title, rectF.centerX(), baseline, paint)
}


fun View.drawCenterText(canvas: Canvas?,
                        valusText: String,
                        x: Float,
                        y: Float,
                        paint: Paint
) {

    val bounds = Rect()
    paint.getTextBounds(valusText, 0, valusText.length, bounds)
    val offSetX = (bounds.right - bounds.left) / 2.toFloat()
    val offSetY = (bounds.bottom - bounds.top) / 2.toFloat()
    canvas?.drawText(valusText, x - offSetX, y + offSetY, paint)


}


