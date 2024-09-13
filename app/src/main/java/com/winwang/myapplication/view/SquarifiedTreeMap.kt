package com.winwang.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

data class Rectangle(val name: String, val ratio: Float, val color: Int, var rect: RectF = RectF())

class SquarifiedTreemapView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var rectangles: List<Rectangle> = emptyList()
    private var onClickListener: ((Rectangle) -> Unit)? = null
    private val paint = Paint()

    fun setRectangles(rectangles: List<Rectangle>) {
        this.rectangles = squarify(rectangles, width * height.toFloat(), 0f, 0f, width.toFloat(), height.toFloat())
        this.rectangles.forEach {
            val rect = it.rect
            Log.d(">>>>>>", "${rect.left}>>>${rect.top}>>>${rect.right}>>>${rect.bottom}")
        }
        invalidate()
    }

    fun setOnClickListener(listener: (Rectangle) -> Unit) {
        onClickListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (rectangle in rectangles) {
            paint.color = rectangle.color
            canvas.drawRect(rectangle.rect, paint)
            // TODO: Draw the name and ratio inside the rectangle
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val clickedRectangle = rectangles.find { it.rect.contains(event.x, event.y) }
            if (clickedRectangle != null) {
                onClickListener?.invoke(clickedRectangle)
            }
        }

        return true
    }

    private fun squarify(rectangles: List<Rectangle>, totalArea: Float, x: Float, y: Float, width: Float, height: Float): List<Rectangle> {
        if (rectangles.isEmpty()) {
            return emptyList()
        }

        val head = rectangles.first()
        val tail = rectangles.drop(1)

        val headArea = totalArea * head.ratio
        val dx = sqrt(headArea.toDouble()).toFloat()
        val dy = headArea / dx

        val headRect = if (width >= height) {
            RectF(x, y, x + dx, y + dy)
        } else {
            RectF(x, y, x + dy, y + dx)
        }
        val updatedHead = head.copy(rect = headRect)

        val remainingArea = totalArea - headArea
        val remainingRect = if (width >= height) {
            RectF(x + dx, y, width - dx, height)
        } else {
            RectF(x, y + dx, width, height - dy)
        }

        return listOf(updatedHead) + squarify(tail, remainingArea, remainingRect.left, remainingRect.top, remainingRect.width(), remainingRect.height())
    }
}