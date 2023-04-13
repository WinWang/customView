package com.winwang.myapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.winwang.myapplication.R
import com.winwang.myapplication.utils.drawCenterTextXYRect

/**
 * Created by WinWang on 2022/11/30
 * Description:
 **/
open class HorizontalTreeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var leftFillColor = 0
    private var leftFadeColor = 0
    private var rightFillColor = 0
    private var rightFadeColor = 0
    private var centerColor = 0
    private var leftLineColor = 0
    private var rightLineColor = 0
    private var leftAlphaBackColor = 0
    private var rightAlphaBackColor = 0
    private var whiteColor = ContextCompat.getColor(context, R.color.colorWhite)
    private var showTree = true  //是否绘制分支树
    private var showCenter = true //是否绘制中间部分
    private var skewWidth = 10f//斜切的角度宽
    private var gapWidth = 10f
    private var leftRatio = 0.0F
    private var centerRatio = 0.0F
    private var rightRatio = 0.0F
    private var sectionHeight = 0.0f
    private var rectPadding = 20//文字矩形内边距
    private var leftText1 = "苹果品种"
    private var leftText2 = "棉花"
    private var rightText1 = "线材"
    private var rightText2 = "螺纹钢"
    private var topBottomPadding = 5f //上下预留边距
    private var linePercent = 5 //绘制分支竖直线占比
    private var leftLineRatioPercent = 3 //左边第二条线占比linePercent比例
    private var rightLineRatioPercent = 4 //右边第二条线占比linePercent比例

    private val paint: TextPaint by lazy {
        TextPaint(Paint.ANTI_ALIAS_FLAG)
    }

    private var roundCornerPath: Path? = null


    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.HorizontalTreeView)
        leftFillColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVleftColorFill,
                ContextCompat.getColor(context, R.color.color_F54F4F)
            ) //左边实体色
        leftFadeColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVleftColorFade,
                ContextCompat.getColor(context, R.color.color_FFAFAF)
            ) //左边弱色
        centerColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVcenterColor,
                ContextCompat.getColor(context, R.color.color_C9C9C9)
            ) //中间色
        rightFillColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVrightColorFill,
                ContextCompat.getColor(context, R.color.color_3CCD8F)
            ) //右边实体色
        rightFadeColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVrightColorFade,
                ContextCompat.getColor(context, R.color.color_63F191)
            ) //右边弱色
        leftLineColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVrightColorFade,
                ContextCompat.getColor(context, R.color.color_EF3434)
            ) //左边线条颜色
        rightLineColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVrightColorFade,
                ContextCompat.getColor(context, R.color.color_00BA6A)
            ) //右边线条颜色
        leftAlphaBackColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVrightColorFade,
                ContextCompat.getColor(context, R.color.color_FFF0F0)
            ) //左边半透明背景
        rightAlphaBackColor =
            obtainStyledAttributes.getColor(
                R.styleable.HorizontalTreeView_HTVrightColorFade,
                ContextCompat.getColor(context, R.color.color_E3FFF3)
            ) //右边半透明背景
        sectionHeight = obtainStyledAttributes.getDimension(R.styleable.HorizontalTreeView_HTVsectionHeight, 30f)//中间部分的高度
        gapWidth = obtainStyledAttributes.getDimension(R.styleable.HorizontalTreeView_HTVgapWidth, 10f)//中间部分的高度
        skewWidth = obtainStyledAttributes.getDimension(R.styleable.HorizontalTreeView_HTVskewWidth, 10f)//中间部分的高度

        showTree = obtainStyledAttributes.getBoolean(R.styleable.HorizontalTreeView_HTVshowTree, true)//是否展示旁边分支
        showCenter = obtainStyledAttributes.getBoolean(R.styleable.HorizontalTreeView_HTVshowCenter, true)//是否展示旁边分支

        obtainStyledAttributes.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    open fun setCategoryTag(leftTag1: String? = "", leftTag2: String? = "", rightTag1: String? = "", rightTag2: String? = "") {
        if (!TextUtils.isEmpty(leftTag1)) {
            leftText1 = leftTag1 ?: ""
        }
        if (!TextUtils.isEmpty(leftTag2)) {
            leftText2 = leftTag2 ?: ""
        }
        if (!TextUtils.isEmpty(rightTag1)) {
            rightText1 = rightTag1 ?: ""
        }
        if (!TextUtils.isEmpty(rightTag2)) {
            rightText2 = rightTag2 ?: ""
        }
        invalidate()
    }

    open fun setTreeViewData(leftRatio: Float, centerRatio: Float, rightRatio: Float) {
        val totalRatio = leftRatio + centerRatio + rightRatio
        this.leftRatio = if (leftRatio / totalRatio < 0.01f) 0.01f else leftRatio / totalRatio
        this.centerRatio = centerRatio / totalRatio
        if ((this.leftRatio + this.centerRatio) > 0.95) {
            this.leftRatio = this.leftRatio - 0.025f
            this.centerRatio = this.centerRatio - 0.025f
            this.rightRatio = 0.05f
        } else {
            this.rightRatio = rightRatio / totalRatio
        }
        invalidate()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //确定中间条状的View的矩形区域
        canvas?.save()
        val topY = height / 2.toFloat() - sectionHeight / 2
        val bottomY = height / 2.toFloat() + sectionHeight / 2
        roundCornerPath = Path()
        val rectF = RectF(0f, topY, width.toFloat(), bottomY)
        roundCornerPath?.addRoundRect(rectF, (height / 2).toFloat(), (height / 2).toFloat(), Path.Direction.CW)
        roundCornerPath?.let { canvas?.clipPath(it) }

        //绘制横向进度条
        val treeViewWidth = width - gapWidth * 2
        val leftPath = Path()  //左边path
        val centerPath = Path()//中间path
        val rightPath = Path() //右边path

        //绘制左边path
        leftPath.moveTo(0f, topY)
        leftPath.lineTo(leftRatio * treeViewWidth + skewWidth, topY)
        leftPath.lineTo(leftRatio * treeViewWidth, bottomY)
        leftPath.lineTo(0f, bottomY)
        leftPath.close()
        //绘制中间区域path
        if (showCenter) {
            centerPath.moveTo(leftRatio * treeViewWidth + gapWidth + skewWidth, topY)
            centerPath.lineTo(leftRatio * treeViewWidth + gapWidth + skewWidth + centerRatio * treeViewWidth + skewWidth, topY)
            centerPath.lineTo(leftRatio * treeViewWidth + gapWidth + skewWidth + centerRatio * treeViewWidth, bottomY)
            centerPath.lineTo(leftRatio * treeViewWidth + gapWidth, bottomY)
            centerPath.close()
        }
        //绘制右边区域path
        rightPath.moveTo(
            leftRatio * treeViewWidth + gapWidth + skewWidth + if (showCenter) centerRatio * treeViewWidth + gapWidth + skewWidth else 0f,
            topY
        )
        rightPath.lineTo(width.toFloat(), topY)
        rightPath.lineTo(width.toFloat(), bottomY)
        rightPath.lineTo(leftRatio * treeViewWidth + gapWidth + if (showCenter) skewWidth + centerRatio * treeViewWidth + gapWidth else 0f, bottomY)
        rightPath.close()

        //左边变色shader
        paint.style = Paint.Style.FILL
        val leftGradient = LinearGradient(0f, topY, leftRatio * treeViewWidth + gapWidth, topY, leftFillColor, leftFadeColor, Shader.TileMode.CLAMP)
        paint.shader = leftGradient
        canvas?.drawPath(leftPath, paint)
        paint.shader = null
        paint.color = centerColor
        canvas?.drawPath(centerPath, paint)
        //右边变色shader
        val rightGradient = LinearGradient(
            leftRatio * treeViewWidth + gapWidth * 2 + centerRatio * treeViewWidth + gapWidth * 2,
            topY,
            width.toFloat(),
            topY,
            rightFadeColor,
            rightFillColor,
            Shader.TileMode.CLAMP
        )
        paint.shader = rightGradient
        canvas?.drawPath(rightPath, paint)
        paint.shader = null
        canvas?.restore()

        if (!showTree) {
            return
        }

        //绘制线条左边
        paint.color = leftLineColor
        canvas?.drawLine(
            (leftRatio * treeViewWidth + gapWidth) / linePercent,
            bottomY,
            (leftRatio * treeViewWidth + gapWidth) / linePercent,
            height - topBottomPadding,
            paint
        )
        canvas?.drawLine(
            (leftRatio * treeViewWidth + gapWidth) / linePercent * leftLineRatioPercent,
            topY,
            (leftRatio * treeViewWidth + gapWidth) / linePercent * leftLineRatioPercent,
            topBottomPadding,
            paint
        )
        //绘制线条右边
        paint.color = rightLineColor
        canvas?.drawLine(
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent,
            bottomY,
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent,
            height - topBottomPadding,
            paint
        )
        canvas?.drawLine(
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent * rightLineRatioPercent,
            topY,
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent * rightLineRatioPercent,
            topBottomPadding,
            paint
        )
        paint.textSize = 25f
        val measureText1 = paint.measureText(leftText1)
        val measureText2 = paint.measureText(leftText2)
        val measureText3 = paint.measureText(rightText1)
        val measureText4 = paint.measureText(rightText2)
        //绘制左边文字
        paint.style = Paint.Style.STROKE
        paint.color = leftLineColor
        val leftText1Rect = RectF(
            (leftRatio * treeViewWidth + gapWidth) / linePercent,
            height - 50 - topBottomPadding,
            (leftRatio * treeViewWidth + gapWidth) / linePercent + measureText1 + rectPadding,
            height - topBottomPadding,
        )
        val leftText2Rect = RectF(
            (leftRatio * treeViewWidth + gapWidth) / linePercent * leftLineRatioPercent,
            topBottomPadding,
            (leftRatio * treeViewWidth + gapWidth) / linePercent * leftLineRatioPercent + measureText2 + rectPadding,
            50f + topBottomPadding
        )
        canvas?.drawRoundRect(leftText2Rect, 5f, 5f, paint)
        canvas?.drawRoundRect(leftText1Rect, 5f, 5f, paint)
        paint.style = Paint.Style.FILL
        canvas?.drawRoundRect(leftText1Rect, 5f, 5f, paint)
        paint.color = leftAlphaBackColor
        canvas?.drawRoundRect(leftText2Rect, 5f, 5f, paint)
        canvas?.run {
            paint.color = leftLineColor
            drawCenterTextXYRect(this, leftText2, leftText2Rect, paint)
            paint.color = whiteColor
            drawCenterTextXYRect(this, leftText1, leftText1Rect, paint)
        }
        //绘制右边文字
        paint.style = Paint.Style.STROKE
        paint.color = rightLineColor
        val rightText1Rect = RectF(
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent - measureText3 - rectPadding,
            height - 50 - topBottomPadding,
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent,
            height - topBottomPadding,
        )
        val rightText2Rect = RectF(
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent * rightLineRatioPercent - measureText4 - rectPadding,
            topBottomPadding,
            leftRatio * treeViewWidth + gapWidth + centerRatio * treeViewWidth + gapWidth + rightRatio * treeViewWidth / linePercent * rightLineRatioPercent,
            50f + topBottomPadding,
        )
        canvas?.drawRoundRect(rightText2Rect, 5f, 5f, paint)
        canvas?.drawRoundRect(rightText1Rect, 5f, 5f, paint)
        paint.style = Paint.Style.FILL
        canvas?.drawRoundRect(rightText2Rect, 5f, 5f, paint)
        paint.color = rightAlphaBackColor
        canvas?.drawRoundRect(rightText1Rect, 5f, 5f, paint)
        canvas?.run {
            paint.color = rightLineColor
            drawCenterTextXYRect(this, rightText1, rightText1Rect, paint)
            paint.color = whiteColor
            drawCenterTextXYRect(this, rightText2, rightText2Rect, paint)
        }
    }
}