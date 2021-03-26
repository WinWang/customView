package com.winwang.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R
import com.winwang.myapplication.bean.PieBlockBean
import kotlinx.coroutines.Job

/**
 *Created by WinWang on 2021/3/25
 *Description->圆环，通过扇形绘制的方法实现，不是通过画笔Stroke实现
 */
class CirclePieView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG) //画笔
    private var mWidth = 0  //整个控件宽度
    private var mHeight = 0 //整个控件高度
    private lateinit var chartList: List<PieBlockBean>
    private var totalValue = 0f
    private var transCircleRadius = 0f //透明圆圈半径
    private var whiteCircleRadius = 0f //白色圆圈半径
    private var colorArray = arrayOf(
            R.color.COLOR_CIRCLE_1, R.color.COLOR_CIRCLE_2,
            R.color.COLOR_CIRCLE_3, R.color.COLOR_CIRCLE_4,
            R.color.COLOR_CIRCLE_5, R.color.COLOR_CIRCLE_6,
            R.color.COLOR_CIRCLE_7, R.color.COLOR_CIRCLE_8)
    private val colorList = ArrayList<Int>()
    private var whiteTransColor = 0
    private var whiteColor = 0
    lateinit var launch: Job

    init {
        paint.style = Paint.Style.FILL
        colorArray.forEach {
            colorList.add(ContextCompat.getColor(context, it))
        }
        whiteColor = ContextCompat.getColor(context, R.color.WHITE)
        whiteTransColor = ContextCompat.getColor(context, R.color.COLOR_WHITE_TRANS)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        transCircleRadius = mWidth / 2 / 10 * 7f
        whiteCircleRadius = transCircleRadius - 30
    }


    /**
     * 设置图表数据
     */
    fun setChartData(dataList: List<PieBlockBean>) {
        if (dataList.isNotEmpty()) {
            chartList = dataList
            totalValue = 0f
            dataList.forEach { item ->
                totalValue += item.percent
            }
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            if (chartList.isNotEmpty()) {
                drawBackArc(this)
                drawCircle(this)
            }
        }
    }

    /**
     * 绘制半透明的白色
     */
    private fun drawCircle(canvas: Canvas) {
        paint.color = whiteTransColor
        canvas.drawCircle(width / 2f, height / 2f, transCircleRadius, paint)
        paint.color = whiteColor
        canvas.drawCircle(width / 2f, height / 2f, whiteCircleRadius, paint)

    }

    /**
     * 绘制背景的圆弧扇形区域
     */
    private fun drawBackArc(canvas: Canvas) {
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        var startAngel = -90f
        val percentAngel = 360 / totalValue
        chartList.forEachIndexed { index, item ->
            val angle = percentAngel * item.percent
            paint.color = colorList[index]
            canvas.drawArc(rect, startAngel, angle, true, paint)
            startAngel += angle
        }
    }


}