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
fun View.drawCenterTextXY(
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


/**
 * xy方向都依据绘制点居中
 */
fun View.drawCenterTextXY(canvas: Canvas?,
                          valusText: String,
                          x: Float,
                          y: Float,
                          paint: Paint
) {

    val bounds = Rect()
    paint.getTextBounds(valusText, 0, valusText.length, bounds)
    val offSetX = (bounds.right - bounds.left) / 2f
    val offSetY = (bounds.bottom - bounds.top) / 2f
    canvas?.drawText(valusText, x - offSetX, y + offSetY, paint)


}

/**
 * 只是针对X轴方向的居中绘制
 */
fun View.drawCenterTextX(canvas: Canvas?,
                         valusText: String,
                         x: Float,
                         y: Float,
                         paint: Paint
) {
    val bounds = Rect()
    paint.getTextBounds(valusText, 0, valusText.length, bounds)
    val offSetX = (bounds.right - bounds.left) / 2f
    canvas?.drawText(valusText, x - offSetX, y, paint)


}



