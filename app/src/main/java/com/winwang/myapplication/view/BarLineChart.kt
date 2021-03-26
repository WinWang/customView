package com.winwang.myapplication.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R
import com.winwang.myapplication.bean.BarLineBean
import com.winwang.myapplication.utils.drawCenterTextXY
import kotlin.math.roundToInt


/**
 *Created by WinWang on 2021/3/24
 *Description->柱状图和折线图组合得柱子
 */
class BarLineChart @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var textAxisPaint: Paint
    private lateinit var textBarPaint: Paint
    private lateinit var barPaint: Paint
    private lateinit var linePaint: Paint
    private lateinit var lineBorderPaint: Paint
    private lateinit var dashLinePaint: Paint
    private lateinit var circlePaint: Paint
    private var barDefaultSize = 20f //柱状图默认宽度
    private var defaultBarTextSize = 20f //默认柱状图文字大小
    private var mBarWidth = 0f   //柱状图的高度
    private var mCircleRadius = 7f //圆形半径
    private var mCircleRadiusOut = 11f //圆形半径
    private var mWidth = 0  //整个控件宽度
    private var mHeight = 0 //整个控件高度
    private var mChartHeight = 0   //图标的高度
    private var borderWidth = 2f   //变宽的宽度
    private var lineWidth = 2f     //线条的宽度
    private var mMaxValue = 0f  //图表最大值
    private var mMinValue = 0f  //图表最小值
    private var gap = 0f //  纵轴的每个数字代表的间隙
    private var topAndBottomPadding = 30f //上下内边距
    private var leftAndRightPadding = 60f //左右的默认padding
    private lateinit var chartList: List<BarLineBean>
    private var colorLine = 0
    private var colorCircle = 0

    init {
        initPaint(context, attrs)
    }

    private fun initPaint(context: Context, attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.BarLineChart)
        val colorBar = obtainStyledAttributes.getColor(R.styleable.BarLineChart_barColor, ContextCompat.getColor(context, R.color.COLOR_EFDEDD)) //柱状图颜色
        colorLine = obtainStyledAttributes.getColor(R.styleable.BarLineChart_lineColor, ContextCompat.getColor(context, R.color.COLOR_FDA31E)) //折线图颜色
        colorCircle = obtainStyledAttributes.getColor(R.styleable.BarLineChart_circleColor, ContextCompat.getColor(context, R.color.COLOR_FFE8C6)) //折线图颜色
        val colorBarText = obtainStyledAttributes.getColor(R.styleable.BarLineChart_barTextColor, ContextCompat.getColor(context, R.color.COLOR_AD6630)) //柱壮图文字图颜色
        val colorAxisText = obtainStyledAttributes.getColor(R.styleable.BarLineChart_axisTextColor, ContextCompat.getColor(context, R.color.COLOR_666666)) //柱壮图文字图颜色
        val barTextSize = obtainStyledAttributes.getDimension(R.styleable.BarLineChart_barTextSize, defaultBarTextSize)
        val axisTextSize = obtainStyledAttributes.getDimension(R.styleable.BarLineChart_axisTextSize, defaultBarTextSize)
        mBarWidth = obtainStyledAttributes.getFloat(R.styleable.BarLineChart_barWidth, barDefaultSize) //柱壮图宽度
        obtainStyledAttributes.recycle()
        //初始化坐标轴文字画笔
        textAxisPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textAxisPaint.style = Paint.Style.FILL
        textAxisPaint.color = colorAxisText
        textAxisPaint.textSize = axisTextSize

        //初始化柱状图画笔
        textBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textBarPaint.style = Paint.Style.FILL
        textBarPaint.color = colorBarText
        textBarPaint.textSize = barTextSize

        //初始化柱状图画笔
        barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        barPaint.style = Paint.Style.FILL
        barPaint.color = colorBar
        barPaint.strokeWidth = mBarWidth

        //初始化折线图画笔
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = lineWidth
        linePaint.color = colorLine

        //初始化边框画笔
        lineBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        lineBorderPaint.color = ContextCompat.getColor(context, R.color.COLOR_DFDFDF)
        lineBorderPaint.style = Paint.Style.STROKE
        lineBorderPaint.strokeWidth = borderWidth

        //初始化虚线画笔
        dashLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dashLinePaint.color = ContextCompat.getColor(context, R.color.COLOR_DFDFDF)
        dashLinePaint.style = Paint.Style.STROKE
        val effects: PathEffect = DashPathEffect(floatArrayOf(4f, 4f), 0f)
        dashLinePaint.pathEffect = effects

        //初始化小圆圈画笔
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint.style = Paint.Style.FILL
        circlePaint.color = colorLine

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }


    fun setBarLineData(chartList: List<BarLineBean>) {
        this.chartList = chartList
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            initData()
            drawBorder(it)
            drawBarAndLine(it)
        }

    }


    private fun initData() {
        if (chartList.isNotEmpty()) {
            mChartHeight = (mHeight - topAndBottomPadding).roundToInt()
            mMaxValue = 0f //重置值
            chartList.forEach {
                mMaxValue = it.barValue.coerceAtLeast(mMaxValue)
            }
            gap = (mChartHeight - topAndBottomPadding * 2) / mMaxValue  // 控制纵轴间隔
        }
    }

    /**
     * 绘制柱状图
     */
    private fun drawBarAndLine(canvas: Canvas) {
        if (chartList.isNotEmpty()) {
            val path = Path()
            val hGap = (mWidth - leftAndRightPadding * 2) / (chartList.size - 1)  //横向的间距
            chartList.forEachIndexed { index, it ->
                //绘制柱子
                canvas.drawLine(leftAndRightPadding + hGap * index, mChartHeight.toFloat(), leftAndRightPadding + hGap * index, mChartHeight - gap * it.barValue, barPaint)
                //绘制底部的文字
                drawCenterTextXY(canvas, it.time, leftAndRightPadding + hGap * index, mChartHeight.toFloat() + 15, textAxisPaint)
                //绘制柱状图的数字
                drawCenterTextXY(canvas, it.barValue.toString(), leftAndRightPadding + hGap * index, mChartHeight - gap * it.barValue - 20, textBarPaint)
                //绘制折线的圆点
                circlePaint.color = colorCircle
                canvas.drawCircle(leftAndRightPadding + hGap * index, mChartHeight - gap * it.lineValue, mCircleRadiusOut, circlePaint)
                circlePaint.color = colorLine
                canvas.drawCircle(leftAndRightPadding + hGap * index, mChartHeight - gap * it.lineValue, mCircleRadius, circlePaint)
                if (index == 0) {
                    path.moveTo(leftAndRightPadding + hGap * index, mChartHeight - gap * it.lineValue)
                } else {
                    path.lineTo(leftAndRightPadding + hGap * index, mChartHeight - gap * it.lineValue)
                }
            }
            //绘制折线
            canvas.drawPath(path, linePaint)
        }

    }

    /**
     * 绘制图表的边框数据
     */
    private fun drawBorder(canvas: Canvas) {
        val rect = Rect(0, 0, mWidth, mChartHeight)
        canvas.drawRect(rect, lineBorderPaint)
        val gap = mChartHeight / 7f
        (1..6).forEach {
            canvas.drawLine(0f, gap * it, mWidth.toFloat(), gap * it, dashLinePaint)
        }


    }


}