package com.winwang.myapplication.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R
import com.winwang.myapplication.bean.EvaluteBarchartBean
import com.winwang.myapplication.utils.drawCenterTextX
import com.winwang.myapplication.utils.drawCenterTextXY

/**
 *Created by WinWang on 2021/3/26
 *Description->
 */
class RoundBarChartView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0f
    private var mHeight = 0f
    private val paintBar = Paint(Paint.ANTI_ALIAS_FLAG)  //柱状图画笔
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG) //文字画笔
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG) //线条画笔
    private lateinit var chartData: List<EvaluteBarchartBean>
    private var barWidth = 20f //柱状图宽度
    private var lineWidth = 2f //线条的宽度
    private var barColor = 0 //柱状图颜色
    private var barTextColor = 0 //柱状图文字颜色
    private var barLineColor = 0 //柱状图线条颜色
    private var barAxisTextColor = 0 //柱状图坐标轴颜色
    private var barTextSize = 15f //柱状图文字大小
    private var axisTextSize = 15f //坐标轴文字大小
    private var bottomHeight = 30f; //底部文字的高度
    private var topGap = 80f
    private var barMaxValue = 0 //柱状图最大值

    init {
        initView(context, attrs)

        //初始化柱状图画笔
        paintBar.style = Paint.Style.FILL
        paintBar.color = barColor
        paintBar.textSize = barTextSize

        //初始化线条画笔
        paintLine.style = Paint.Style.STROKE
        paintLine.color = barLineColor

        //初始化文字画笔
        paintText.color = barTextColor
        paintText.textSize = barTextSize

    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.RoundBarChartView)
        barColor = obtainStyledAttributes.getColor(R.styleable.RoundBarChartView_roundBarColor, ContextCompat.getColor(context, R.color.COLOR_FDAD1E))
        barTextColor = obtainStyledAttributes.getColor(R.styleable.RoundBarChartView_roundBarTextColor, ContextCompat.getColor(context, R.color.COLOR_CD9115))
        barLineColor = obtainStyledAttributes.getColor(R.styleable.RoundBarChartView_roundBarlineColor, ContextCompat.getColor(context, R.color.BLACK))
        barAxisTextColor = obtainStyledAttributes.getColor(R.styleable.RoundBarChartView_roundBarAxisTextColor, ContextCompat.getColor(context, R.color.BLACK))
        barTextSize = obtainStyledAttributes.getDimension(R.styleable.RoundBarChartView_roundBarTextSize, 15f)
        axisTextSize = obtainStyledAttributes.getDimension(R.styleable.RoundBarChartView_roundBarAxisTextSize, 15f)
        bottomHeight = obtainStyledAttributes.getDimension(R.styleable.RoundBarChartView_roundBottomHeight, 30f)
        barWidth = obtainStyledAttributes.getDimension(R.styleable.RoundBarChartView_roundBarWidth, 30f)
        obtainStyledAttributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
    }

    /**
     * 设置表格数据
     */
    fun setChartData(data: List<EvaluteBarchartBean>) {
        chartData = data
        if (data.isNotEmpty()) {
            data.forEach {
                barMaxValue = barMaxValue.coerceAtLeast(it.value)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawChart(this)
        }
    }

    private fun drawChart(canvas: Canvas) {
        if (chartData.isNotEmpty()) {
            val hGap = width.toFloat() / chartData.size   //横向的间隔
            //绘制底部横线
            var startX = hGap / 2   //起始点X
            var startY = height - bottomHeight  //起始点Y
            //绘制底部横线
            canvas.drawLine(0f, startY, width.toFloat(), startY, paintLine)
            //Y轴的均分数据
            val percentValue = (startY - topGap) / barMaxValue
            chartData.forEachIndexed { index, evaluteBarchartBean ->
                val xPosition = startX + index * hGap
                val stopY = startY - evaluteBarchartBean.value * percentValue
                val rectF = RectF(xPosition - barWidth / 2, startY, xPosition + barWidth / 2, stopY)
                //绘制柱子
                canvas.drawRect(rectF, paintBar)
                //绘制半圆帽子
                val rectFRound = RectF(xPosition - barWidth / 2, stopY - barWidth / 2, xPosition + barWidth / 2, stopY + barWidth / 2 + 1)
                canvas.drawArc(rectFRound, -180f, 180f, true, paintBar)

                //绘制柱状图数据
                paintText.color = barTextColor
                paintText.textSize = barTextSize
                drawCenterTextX(canvas, "${evaluteBarchartBean.value}", xPosition, stopY - barWidth / 2, paintText)
                //绘制坐标轴文字
                paintText.color = barAxisTextColor
                paintText.textSize = axisTextSize
                drawCenterTextXY(canvas, evaluteBarchartBean.desc, xPosition, startY + bottomHeight / 2, paintText)
            }
        }
    }


}