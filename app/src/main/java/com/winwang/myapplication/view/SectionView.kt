package com.winwang.myapplication.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R
import com.winwang.myapplication.bean.StockSectionBean
import com.winwang.myapplication.utils.drawCenterTextXY

/**
 *Created by WinWang on 2021/3/25
 *Description->股票指标的市值分布
 */
class SectionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0  //整个控件宽度
    private var mHeight = 0 //整个控件高度
    private var topAndBottomPadding = 15f
    private var leftAndRightPadding = 20f
    private var sectionHeight = 0f
    private var gap = 5f
    private var textSize = 25f
    private var bottomTextSize = 25f
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG) //文字画笔
    private val paintSection = Paint(Paint.ANTI_ALIAS_FLAG) //section画笔
    private val paintBack = Paint(Paint.ANTI_ALIAS_FLAG) //section画笔
    private var sectionData: StockSectionBean? = null
    private var textInColor = 0 //里面文字的颜色
    private var textOutColor = 0 //外面文字的颜色

    init {
        initView(context, attrs)

        //初始化背景画笔
        paintBack.style = Paint.Style.FILL
        paintBack.color = ContextCompat.getColor(context, R.color.COLOR_F5F5F5)

        //初始化section画笔
        paintSection.style = Paint.Style.FILL

        //初始化文字画笔
        paintText.style = Paint.Style.FILL
        paintText.color = ContextCompat.getColor(context, R.color.WHITE)
        paintText.textSize = textSize

    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SectionView)
        textSize = obtainStyledAttributes.getDimension(R.styleable.SectionView_sectionInTextSize, 25f)
        bottomTextSize = obtainStyledAttributes.getDimension(R.styleable.SectionView_sectionOutTextSize, 25f)
        textInColor = obtainStyledAttributes.getColor(R.styleable.SectionView_sectionInTextColor, ContextCompat.getColor(context, R.color.WHITE))
        textOutColor = obtainStyledAttributes.getColor(R.styleable.SectionView_sectionOutTextColor, ContextCompat.getColor(context, R.color.BLACK))
        gap = obtainStyledAttributes.getFloat(R.styleable.SectionView_gapWidth, 5f)
        obtainStyledAttributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        sectionHeight = mHeight / 2f
    }

    fun setSectionData(data: StockSectionBean) {
        sectionData = data
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            sectionData?.let {
                drawSctionBack(this, it)
            }
        }
    }

    /**
     * 绘制背景
     */
    private fun drawSctionBack(canvas: Canvas, it: StockSectionBean) {
        //绘制背景灰色
        val rectF = RectF(0f, 0f, width.toFloat(), sectionHeight)
        canvas.drawRoundRect(rectF, sectionHeight / 2, sectionHeight / 2, paintBack)

        val leftSection = leftAndRightPadding                       //里面矩形包裹的左边
        val topSection = topAndBottomPadding                        //里面矩形包裹的上边
        val rightSection = width - leftAndRightPadding              //里面矩形包裹的右边
        val bottomSection = sectionHeight - topAndBottomPadding     //里面矩形包裹的下边

        //去除指定的path-裁剪圆角
        val pathSection = Path()
        val sectionRect = RectF(leftSection, topSection, rightSection, bottomSection)
        pathSection.addRoundRect(sectionRect, sectionRect.height() / 2, sectionRect.height() / 2, Path.Direction.CW)


        val totalValue = it.firstLevel + it.secondLevel + it.thirdLevel
        val percentOne = it.firstLevel / totalValue * (sectionRect.width() - gap * 2)
        val percentTwo = it.secondLevel / totalValue * (sectionRect.width() - gap * 2)
        val percentThree = it.thirdLevel / totalValue * (sectionRect.width() - gap * 2)
        paintText.color = textOutColor
        paintText.textSize = bottomTextSize
        //绘制左边
        val rectFOne = RectF(leftSection, topSection, percentOne + leftSection, bottomSection)
        val startColor1 = ContextCompat.getColor(context, R.color.COLOR_F9F464)
        val endColor1 = ContextCompat.getColor(context, R.color.COLOR_F9CB42)
        val linearGradient1 = LinearGradient(leftSection, topSection, percentOne + leftSection, topSection, startColor1, endColor1, Shader.TileMode.CLAMP)
        paintSection.shader = linearGradient1
//        canvas.drawRect(rectFOne, paintSection) //绘制内部渐变色柱
        //绘制文字
        val middleOne = leftSection + rectFOne.width() / 2
        val middleHeight = sectionHeight / 2
        val middleHeightBottom = sectionHeight + sectionHeight / 2 //底部描述文字的位置
//        drawCenterText(canvas, "${it.firstLevel}", middleOne, middleHeight, paintText) //绘制柱子里面的数据1
        drawCenterTextXY(canvas, it.firstName, middleOne, middleHeightBottom, paintText)

        //绘制中间
        val rectFTwo = RectF(leftSection + percentOne + gap, topSection, leftSection + percentOne + gap + percentTwo, bottomSection)
        val startColor2 = ContextCompat.getColor(context, R.color.COLOR_FE8738)
        val endColor2 = ContextCompat.getColor(context, R.color.COLOR_E94608)
        val linearGradient2 = LinearGradient(leftSection + percentOne + gap, topSection, leftSection + percentOne + percentTwo + gap, topSection, startColor2, endColor2, Shader.TileMode.CLAMP)
        paintSection.shader = linearGradient2
//        canvas.drawRect(rectFTwo, paintSection)  //绘制内部渐变色柱
        //绘制文字
        val middleTwo = leftSection + rectFOne.width() + gap + rectFTwo.width() / 2
//        drawCenterText(canvas, "${it.secondLevel}", middleTwo, middleHeight, paintText) //绘制柱子里面的数据2
        drawCenterTextXY(canvas, it.secondName, middleTwo, middleHeightBottom, paintText)

        //绘制右边
        val rectFThree = RectF(leftSection + percentOne + percentTwo + gap * 2, topSection, leftSection + percentOne + percentTwo + percentThree + gap * 2, bottomSection)
        val startColor3 = ContextCompat.getColor(context, R.color.COLOR_D83F34)
        val endColor3 = ContextCompat.getColor(context, R.color.COLOR_B30C0C)
        val linearGradient3 = LinearGradient(leftSection + percentOne + percentTwo + gap * 2, topSection, leftSection + percentOne + percentTwo + percentThree + gap * 2, topSection, startColor3, endColor3, Shader.TileMode.CLAMP)
        paintSection.shader = linearGradient3
//        canvas.drawRect(rectFThree, paintSection) //绘制内部渐变色柱
        //绘制文字
        val middleThree = leftSection + rectFOne.width() + gap * 2 + rectFTwo.width() + rectFThree.width() / 2
//        drawCenterText(canvas, "${it.thirdLevel}", middleThree, middleHeight, paintText)  //绘制柱子里面的数据3
        drawCenterTextXY(canvas, it.thirdName, middleThree, middleHeightBottom, paintText)

        canvas.clipPath(pathSection) //裁剪
        paintText.color = textInColor  //设置文字颜色
        paintText.textSize = textSize  //设置文字字体大小
        paintSection.shader = linearGradient1
        canvas.drawRect(rectFOne, paintSection)
        drawCenterTextXY(canvas, "${it.firstLevel}", middleOne, middleHeight, paintText) //绘制柱子里面的数据1
        paintSection.shader = linearGradient2
        canvas.drawRect(rectFTwo, paintSection)
        drawCenterTextXY(canvas, "${it.secondLevel}", middleTwo, middleHeight, paintText) //绘制柱子里面的数据2
        paintSection.shader = linearGradient3
        canvas.drawRect(rectFThree, paintSection)
        drawCenterTextXY(canvas, "${it.thirdLevel}", middleThree, middleHeight, paintText)  //绘制柱子里面的数据3
    }


}