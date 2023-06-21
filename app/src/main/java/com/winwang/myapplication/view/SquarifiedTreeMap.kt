package com.winwang.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by WinWang on 2023/6/20
 * Description:
 **/
class SquarifiedTreeMap(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var items: List<Item> = emptyList()
    private var colors: List<Int> = emptyList()
    private var totalValue: Double = 0.0
    private var maxDepth: Int = 0
    private var itemClickListener: ((Item) -> Unit)? = null

    init {
        // 初始化
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 绘制矩形树图
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val nodes = squarify(items, rect, totalValue)

        for (i in items.indices) {
            val node = nodes[i]
            val color = if (i < colors.size) colors[i] else Color.GRAY
            drawNode(canvas, node, color)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // 获取点击的矩形
                val clickedItem = getItemAtPosition(event.x, event.y)
                if (clickedItem != null) {
                    itemClickListener?.invoke(clickedItem)
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getItemAtPosition(x: Float, y: Float): Item? {
        // 获取点击的矩形
        for (i in items.indices) {
            val node = getNodeByIndex(i)
            if (node.rect.contains(x, y)) {
                return items[i]
            }
        }
        return null
    }

    private fun getNodeByIndex(index: Int): Node {
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        return squarify(items, rect, totalValue)[index]
    }

    fun setItems(items: List<Item>) {
        this.items = items
        totalValue = items.sumOf { it.value.toDouble() }
        maxDepth = items.maxBy { it.depth }?.depth ?: 0
        invalidate()
    }

    fun setColors(colors: List<Int>) {
        this.colors = colors
        invalidate()
    }

    fun setItemClickListener(listener: ((Item) -> Unit)?) {
        this.itemClickListener = listener
    }

    private fun drawNode(canvas: Canvas?, node: Node, color: Int) {
        if (canvas == null) return

        val rect = node.rect
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            this.color = color
        }
        canvas.drawRect(rect, paint)

        if (node.children.isNotEmpty()) {
            for (child in node.children) {
                drawNode(canvas, child, color)
            }
        }
    }

    private fun squarify(items: List<Item>, rect: RectF, totalValue: Double): List<Node> {
        val nodes = mutableListOf<Node>()

        if (items.isEmpty()) {
            return nodes
        }

        val length = Math.min(rect.width(), rect.height())
        val ratio = length / totalValue
        var sum = 0.0
        var rowStartIndex = 0
        var rowItems = mutableListOf<Item>()

        for (i in items.indices) {
            val item = items[i]
            val itemLength = item.value * ratio
            if (sum + itemLength <= length) {
                rowItems.add(item)
                sum += itemLength
            } else {
                val rowRect = getRowRect(rowItems, rect, sum)
                val rowNodes = layoutRow(rowItems, rowRect)
                nodes.addAll(rowNodes)

                rowItems.clear()
                rowItems.add(item)
                sum = itemLength
                rowStartIndex = i
            }
        }

        if (rowItems.isNotEmpty()) {
            val rowRect = getRowRect(rowItems, rect, sum)
            val rowNodes = layoutRow(rowItems, rowRect)
            nodes.addAll(rowNodes)
        }

        return nodes
    }

    private fun getRowRect(rowItems: List<Item>, rect: RectF, sum: Double): RectF {
        val x = rect.left
        val y = rect.top
        val width = if (rect.width() > rect.height()) sum.toFloat() / rect.height() else sum.toFloat() / rect.width()
        val height = if (rect.width() > rect.height()) rect.height() else rect.width()

        return RectF(x, y, x + width, y + height)
    }

    private fun layoutRow(rowItems: List<Item>, rowRect: RectF): List<Node> {
        val nodes = mutableListOf<Node>()

        if (rowItems.isEmpty()) {
            return nodes
        }

        var x = rowRect.left
        var y = rowRect.top
        var width = 0f
        var height = 0f

        for (item in rowItems) {
            val ratio = item.value.toDouble() / rowItems.sumOf { it.value }
            if (rowRect.width() > rowRect.height()) {
                width = rowRect.height().toFloat() * ratio.toFloat()
                height = rowRect.height()
            } else {
                width = rowRect.width()
                height = rowRect.width() * ratio.toFloat()
            }
            nodes.add(Node(RectF(x, y, x + width, y + height), item.depth))
            if (rowRect.width() > rowRect.height()) {
                x += width
            } else {
                y += height
            }
        }

        return nodes
    }
}

data class Item(val value: Int, val depth: Int)

data class Node(val rect: RectF, val depth: Int, val children: List<Node> = emptyList())







