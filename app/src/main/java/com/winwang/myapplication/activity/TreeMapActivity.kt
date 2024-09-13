package com.winwang.myapplication.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.winwang.myapplication.R
import com.winwang.myapplication.view.Rectangle
import java.lang.Double.max


class TreeMapActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_map)

        val rectangles = listOf(4.0, 2.0, 3.0, 1.0, 5.0)
        val containerWidth = 800.0
        val containerHeight = 600.0

        val layout = squarify(rectangles, containerWidth, containerHeight)
        for (rectangle in layout) {
            println("Rectangle: x=${rectangle.x}, y=${rectangle.y}, width=${rectangle.width}, height=${rectangle.height}")
        }

    }
    data class Rectangle(val x: Double, val y: Double, val width: Double, val height: Double)

    fun squarify(rectangles: List<Double>, containerWidth: Double, containerHeight: Double): List<Rectangle> {
        val normalizedRectangles = normalizeSizes(rectangles, containerWidth, containerHeight)
        val root = Rectangle(0.0, 0.0, containerWidth, containerHeight)
        return layoutRectangles(normalizedRectangles, root)
    }

    private fun normalizeSizes(rectangles: List<Double>, containerWidth: Double, containerHeight: Double): List<Double> {
        val totalArea = rectangles.sum()
        return rectangles.map { it * (containerWidth * containerHeight) / totalArea }
    }

    private fun layoutRectangles(rectangles: List<Double>, container: Rectangle): List<Rectangle> {
        val layout = mutableListOf<Rectangle>()
        val stack = mutableListOf(Pair(rectangles, container))

        while (stack.isNotEmpty()) {
            val (currentRectangles, currentContainer) = stack.removeAt(stack.size - 1)

            val aspectRatio = currentContainer.width / currentContainer.height
            val totalArea = currentRectangles.sum()
            val rowWidth = totalArea / currentContainer.height

            var accWidth = 0.0
            var rowHeight = 0.0
            val rowRectangles = mutableListOf<Double>()

            for (rect in currentRectangles) {
                val rectWidth = rect / rowWidth
                if (accWidth + rectWidth <= 1 / aspectRatio) {
                    rowRectangles.add(rect)
                    accWidth += rectWidth
                    rowHeight = maxOf(rowHeight, rectWidth)
                } else {
                    val scale = minOf(currentContainer.width / rowHeight, currentContainer.height * rowWidth / totalArea)
                    val scaledRowRectangles = rowRectangles.map { it * scale }
                    val rowContainer = if (aspectRatio > 1) {
                        Rectangle(currentContainer.x, currentContainer.y, currentContainer.width * rowHeight, currentContainer.height * rowWidth / totalArea)
                    } else {
                        Rectangle(currentContainer.x, currentContainer.y, currentContainer.width * rowWidth / totalArea, currentContainer.height * rowHeight)
                    }
                    layout.addAll(layoutRow(scaledRowRectangles, rowContainer))
                    if (accWidth + rectWidth > 1) {
                        stack.add(Pair(currentRectangles.drop(rowRectangles.size), Rectangle(currentContainer.x + rowContainer.width, currentContainer.y, currentContainer.width - rowContainer.width, currentContainer.height)))
                    } else {
                        stack.add(Pair(currentRectangles.drop(rowRectangles.size), Rectangle(currentContainer.x, currentContainer.y + rowContainer.height, currentContainer.width, currentContainer.height - rowContainer.height)))
                    }
                    break
                }
            }
        }

        return layout
    }

    private fun layoutRow(rectangles: List<Double>, container: Rectangle): List<Rectangle> {
        val layout = mutableListOf<Rectangle>()
        val totalArea = rectangles.sum()
        val aspectRatio = container.width / container.height
        var x = container.x
        var y = container.y
        var remainingWidth = container.width

        for (rect in rectangles) {
            val rectWidth = minOf(rect / container.height * container.width, remainingWidth)
            val rectHeight = if (aspectRatio > 1) rectWidth else rect / rectWidth * container.height
            layout.add(Rectangle(x, y, rectWidth, rectHeight))
            x += rectWidth
            remainingWidth -= rectWidth
        }

        return layout
    }



}

