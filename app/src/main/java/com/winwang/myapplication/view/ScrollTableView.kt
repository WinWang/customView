package com.winwang.myapplication.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.OverScroller
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.winwang.myapplication.R
import com.winwang.myapplication.bean.TableDataWrapper
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 * Created by WinWang on 2022/8/4
 * Description:横向滚动tableview
 **/
@RequiresApi(Build.VERSION_CODES.M)
class ScrollTableView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

    /**
     * 图表数据类
     */
    var tableDatas: TableDataWrapper? = null
        set(value) {
            rightItemCount = value?.tableDataList?.get(0)?.rightData?.size ?: 0
            verticalItemCount = value?.tableDataList?.size ?: 0
            field = value
            postInvalidate()
        }


    private var downX: Float = 0f

    private var downY: Float = 0f

    /**
     * 顶部标题栏的高度
     */
    private var topTitleHeight = 100


    /**
     * 左边分配的宽度
     */
    private var leftWidth = 0f

    private var itemWidth = 0f

    private var touchRight = false

    private var rightStartX = 0f

    /**
     * 竖直方向滚动的距离记录
     */
    private var verticalStartY = 100f

    /**
     * 边边阴影
     */
    private var shadownWidth = 20

    /**
     * 渐变阴影
     */
    private var shadownGridient: LinearGradient? = null


    /**
     * 上次竖直方向滚动记录--需要和标题的高度统一
     */
    private var lastVerticalStartY = 100f

    private var defaultRightStartX = 0f

    /**
     * 单个条目的高度
     */
    private var itemHeight = 100


    /**
     * 右边可滚动范围个数
     */
    private var rightItemCount: Int = 0

    /**
     * 纵向条目个数
     */
    private var verticalItemCount: Int = 0


    /**
     * 滚动方向设定 -1未设定方向  1横向   2竖向
     */
    private var scrollTag = -1

    /**
     * 手指抬起事件响应  -1代表未触发点击事件
     */
    private var upYPosition = -1f

    /**
     * 分区段渲染起始指标
     */
    var startIndex: Int = -1


    /**
     * 分区段渲染结束指标
     */
    var endIndex: Int = -1


    /**
     ************************* 滚动速度获取对象
     */
    private var mVelocityTracker = VelocityTracker.obtain()
    private var mMaxFlingVelocity = 0

    //触发fling的速度
    private var mMinFlingVelocity = 0
    /**
     ************************* 滚动速度获取对象
     */


    /**
     * 文字画笔
     */
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 1f
        textSize = 30f
        color = Color.parseColor("#000000")
    }

    /**
     *线条画笔
     */
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 1f
        color = Color.parseColor("#000000")
    }

    /**
     *阴影画笔
     */
    private val leftShadownPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 1f
        color = Color.parseColor("#000000")
    }

    /**
     *线条画笔
     */
    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 1f
        color = Color.parseColor("#ffffff")
    }

    /**
     *线条画笔
     */
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 1f
        color = Color.parseColor("#30ff0000")
    }

    /**
     * 动画执行监听器--竖直方向的
     */
    private val verticalValueAnimator = ValueAnimator().apply {
        duration = 1000L
        addUpdateListener {
            val animatedValue = it.animatedValue as Float
            printValue("动画监听Value>>>>>>>$animatedValue")
            verticalStartY += animatedValue
            if (verticalStartY > topTitleHeight) {
                verticalStartY = topTitleHeight.toFloat()
            }
            if (itemHeight * verticalItemCount + topTitleHeight > measuredHeight) {//超出屏幕范围才需要限制滚动距离
                if (verticalStartY + itemHeight * verticalItemCount < measuredHeight) {
                    verticalStartY = (measuredHeight - itemHeight * verticalItemCount).toFloat()
                }
            } else {
                verticalStartY = topTitleHeight.toFloat()
            }
            lastVerticalStartY = verticalStartY
            invalidate()
        }
    }

    /**
     * 动画执行监听器--横方向的
     */
    private val horizontalValueAnimator = ValueAnimator().apply {
        duration = 500L
        addUpdateListener {
            val animatedValue = it.animatedValue as Float
            printValue("动画监听Value>>>>>>>$animatedValue")
            rightStartX += animatedValue
            if (rightStartX > leftWidth) {//左边距限制
                rightStartX = leftWidth
                cancel()
            }
            if (rightStartX + rightItemCount * itemWidth < measuredWidth) {
                rightStartX = measuredWidth - rightItemCount * itemWidth
                cancel()
            }
            defaultRightStartX = rightStartX - leftWidth
            invalidate()
        }
    }

    init {
        leftWidth = width / 5f
        itemWidth = width / 5f
        mMaxFlingVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        mMinFlingVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
        leftShadownPaint.shader = shadownGridient
    }

    val scroller by lazy {
        OverScroller(context)
    }

//    override fun onDown(p0: MotionEvent?): Boolean {
//        return false
//    }
//
//    override fun onShowPress(p0: MotionEvent?) {
//        println(">>>>>>>onShowPress")
//    }
//
//    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
//        println(">>>>>onSingleTapUp")
//        return false
//    }
//
//    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
//        return false
//    }
//
//    override fun onLongPress(p0: MotionEvent?) {
//
//    }

//    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
//        println(">>>>>>>>>>onFling")
//        return false
//    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        leftWidth = measuredWidth / 5f
        itemWidth = measuredWidth / 4f
        rightStartX = leftWidth
        defaultRightStartX = leftWidth
        shadownGridient = LinearGradient(
            leftWidth,
            0f,
            leftWidth + shadownWidth,
            0f,
            context.getColor(R.color.SHADOWN_START_COLOR),
            context.getColor(R.color.SHADOWN_END_COLOR),
            Shader.TileMode.CLAMP
        )
        leftShadownPaint.shader = shadownGridient
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker.addMovement(event)
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                //取消动画
                verticalValueAnimator.cancel()
                horizontalValueAnimator.cancel()
                downX = event.x
                downY = event.y
                if (downY > leftWidth) {
                    touchRight = true
                }
                verticalStartY = lastVerticalStartY
            }

            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val moveY = event.y
                val deltX = moveX - downX
                val deltY = moveY - downY
                printValue("触摸事件<<<<<<<<<$scrollTag>>>>>>>${deltX.absoluteValue}>>>>>>>>${deltY.absoluteValue}")
                if (deltX.absoluteValue > 0 || deltY.absoluteValue > 0) {
                    if (deltX.absoluteValue > deltY.absoluteValue && (scrollTag == -1 || scrollTag == 1)) {//横向滚动
                        if (!touchRight) {
                            return false
                        }
                        scrollTag = 1
                        rightStartX = leftWidth + deltX + defaultRightStartX
                        if (rightStartX > leftWidth) {//左边距限制
                            rightStartX = leftWidth
                        }
                        if (rightStartX + rightItemCount * itemWidth < measuredWidth) {
                            rightStartX = measuredWidth - rightItemCount * itemWidth
                        }
                        invalidate()
                    } else {//竖向滚动
                        if (scrollTag == 1) {
                            return false
                        }
                        scrollTag = 2
                        verticalStartY = deltY + lastVerticalStartY
                        if (verticalStartY > topTitleHeight) {
                            verticalStartY = topTitleHeight.toFloat()
                        }
                        if (itemHeight * verticalItemCount + topTitleHeight > measuredHeight) {//超出屏幕范围才需要限制滚动距离
                            if (verticalStartY + itemHeight * verticalItemCount < measuredHeight) {
                                verticalStartY = (measuredHeight - itemHeight * verticalItemCount).toFloat()
                            }
                        } else {
                            verticalStartY = topTitleHeight.toFloat()
                        }
                        //不要响应点击事件
                        upYPosition = -1f
                        invalidate()
                        printValue("触摸事件>>>>>>>>纵向")
                    }
                } else {//无滚动
                    upYPosition = event.y
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity.toFloat())
                defaultRightStartX = rightStartX - leftWidth
                lastVerticalStartY = verticalStartY

                //判断滚动的方向
                when (scrollTag) {
                    1 -> { //横向
                        val xVelocity = mVelocityTracker.xVelocity
                        // 速度要大于最小的速度值，才开始滑动
                        if (abs(xVelocity) > mMinFlingVelocity) {
                            printValue("滚动速度X>>>>$xVelocity")
                            horizontalValueAnimator.setFloatValues(xVelocity / 100, 0f)
                            horizontalValueAnimator.start()
                        }
                    }

                    2 -> {//纵向
                        val yVelocity = mVelocityTracker.yVelocity
                        if (abs(yVelocity) > mMinFlingVelocity) {
                            printValue("设置ValueAnimator>>>>>>$verticalStartY>>>>>>>$yVelocity")
                            verticalValueAnimator.setFloatValues(yVelocity / 200, 0f)
                            verticalValueAnimator.start()
                        }
                    }
                }
                scrollTag = -1
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle()
                    mVelocityTracker = null
                }
                printValue("触摸事件>>>>>>>>UP   Cancel")
            }
        }
        return true
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //顶部标题的矩形背景
        val topTitleRect = Rect(0, 0, measuredWidth, topTitleHeight)
        //绘制表格数据
        drawText(canvas, topTitleRect)
//        clipLeftDrawContent(canvas)
        //绘制表头数据
        drawHeadTitle(canvas, topTitleRect)
        //清楚左边多绘制的内容
    }

    private fun clipLeftDrawContent(canvas: Canvas?) {
        val rightPaht = Path()
        val topTitleRightRect = RectF(leftWidth, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        rightPaht.addRect(topTitleRightRect, Path.Direction.CW)
        canvas?.clipPath(rightPaht)
    }

    /**
     * 绘制表头
     */
    private fun drawHeadTitle(canvas: Canvas?, topTitleRect: Rect) {
        canvas?.run {
            tableDatas?.tableTitle?.run {
//                backPaint.color = context.getColor(R.color.SHADOWN_START_COLOR)
                //绘制顶部背景
//                canvas.drawRect(topTitleRect, backPaint)

                //绘制左列标题栏
                leftTitle?.run {
                    val bounds = Rect()
                    textPaint.getTextBounds(this, 0, this.length, bounds)
                    val width = bounds.width()
                    val height = bounds.height()
                    val startX: Int = (leftWidth / 2 - width / 2f).toInt()
                    val startY: Float = (topTitleHeight / 2 + height / 2).toFloat()
                    canvas.drawText(leftTitle ?: "", startX.toFloat(), startY, textPaint)
                }

                //绘制竖线条--
                canvas.drawLine(
                    leftWidth,
                    0f,
                    leftWidth,
                    topTitleHeight.toFloat(),
                    linePaint
                )

                //绘制标题分割线
                canvas.drawLine(0f, topTitleHeight.toFloat(), measuredWidth.toFloat(), topTitleHeight.toFloat(), linePaint)

//                val rightPaht = Path()
//                val topTitleRightRect = RectF(leftWidth, 0f, measuredWidth.toFloat(), topTitleHeight.toFloat())
//                rightPaht.addRect(topTitleRightRect, Path.Direction.CW)
//                canvas.clipPath(rightPaht)

                //绘制右边标题栏
                rightTitle?.forEachIndexed { index, item ->
                    val bounds = Rect()
                    textPaint.getTextBounds(item, 0, item.length, bounds)
                    val width = bounds.width()
                    val height = bounds.height()
                    val startX: Int = (rightStartX + itemWidth * index + itemWidth / 2 - width / 2f).toInt()
                    val startY: Float = (topTitleHeight / 2 + height / 2).toFloat()
                    canvas.drawText(item, startX.toFloat(), startY, textPaint)
                }

                //绘制阴影部分
                if (rightStartX < leftWidth) {//有位移才显示阴影
                    canvas.drawRect(
                        leftWidth,
                        0f,
                        leftWidth + shadownWidth.toFloat(),
                        topTitleHeight.toFloat(),
                        leftShadownPaint
                    )
                }


            }
        }
    }

    /**
     * 绘制表格数据
     */
    private fun drawText(canvas: Canvas?, topTitleRect: Rect) {
        itemHeight = 100
        //确定滑动需要渲染的范围，降低List过长渲染效率
        startIndex = (verticalStartY / itemHeight).absoluteValue.toInt() - 1
        endIndex = if (itemHeight * verticalItemCount > measuredHeight) {
            //超出屏幕范围
            ((verticalStartY - measuredHeight) / itemHeight).absoluteValue.toInt()
        } else {
            //不满一屏全部渲染
            verticalItemCount
        }
        printValue("开始结束>>>>>>>>>>>$startIndex>>>>>$endIndex")
        backPaint.color = context.getColor(R.color.colorWhite)
        canvas?.run {
            tableDatas?.run {
                //列表数据
                val tableData = this.tableDataList
                tableData?.filterIndexed { index, _ ->
                    index in (startIndex) until endIndex + 1
                }?.forEachIndexed { i, item ->
                    val index = startIndex + i
                    //条目顶部Y距离
                    val startHY = itemHeight * index
                    //条目底部Y距离
                    val startHYNext = itemHeight * (index + 1)


                    //绘制右边滑动区域
                    item.rightData?.run {
                        this.forEachIndexed { index, item ->
                            val bounds = Rect()
                            textPaint.getTextBounds(item, 0, item.length, bounds)
                            val width = bounds.width()
                            val height = bounds.height()
                            val startX: Int = (rightStartX + itemWidth * index + itemWidth / 2 - width / 2f).toInt()
                            val startY: Float = verticalStartY + startHY + itemHeight / 2 + height / 2
                            canvas.drawText(item, startX.toFloat(), startY, textPaint)
                        }
                    }


                    //绘制左边固定标题
                    item.header?.run {
                        //绘制左边固定标题
                        val leftTitle = this.leftTitle
                        val bounds = Rect()
                        textPaint.getTextBounds(leftTitle, 0, leftTitle.length, bounds)
                        val width = bounds.width()
                        val height = bounds.height()
                        val startX: Int = (leftWidth / 2 - width / 2f).toInt()
                        val startY: Float = verticalStartY + startHY + itemHeight / 2 + height / 2
//                        canvas.drawRect(
//                            0f,
//                            verticalStartY + index * itemHeight.toFloat(),
//                            leftWidth,
//                            verticalStartY + (index + 1) * itemHeight.toFloat(),
//                            backPaint
//                        )
                        canvas.drawText(leftTitle + index, startX.toFloat(), startY, textPaint)
                        //绘制竖线条--
                        canvas.drawLine(
                            leftWidth,
                            verticalStartY + index * itemHeight.toFloat(),
                            leftWidth,
                            verticalStartY + ((index + 1) * itemHeight).toFloat(),
                            linePaint
                        )

                        //绘制阴影
                        if (rightStartX < leftWidth) {//有位移才显示阴影
                            canvas.drawRect(
                                leftWidth,
                                verticalStartY + index * itemHeight.toFloat(),
                                leftWidth + shadownWidth.toFloat(),
                                verticalStartY + ((index + 1) * itemHeight).toFloat(),
                                leftShadownPaint
                            )
                        }

                        //绘制横向线条
                        canvas.drawLine(
                            0f,
                            verticalStartY + (index + 1) * itemHeight.toFloat(),
                            measuredWidth.toFloat(),
                            verticalStartY + (index + 1) * itemHeight.toFloat(),
                            linePaint
                        )

                    }

                    //绘制条目点击阴影
                    //绘制单个条目举行空间
                    if (upYPosition != -1f) {
                        val itemRect = Rect(
                            0,
                            (verticalStartY + startHY).toInt(),
                            measuredWidth,
                            (startHYNext + verticalStartY).toInt()
                        )
                        if (itemRect.contains(1, (upYPosition).toInt()) && !topTitleRect.contains(0, upYPosition.toInt())) {
                            canvas.drawRect(itemRect, shadowPaint)
                            //添加clickListener响应点击事件
//                            Toast.makeText(context, "点击了条目$index", Toast.LENGTH_SHORT).show()
                            upYPosition = -1f
                        }
                    }
                }
            }
        }
    }


    private fun printValue(value: String, printValue: Boolean = true) {
        if (printValue) {
            Log.d("ScrollTableView>>>>>>>", value)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mVelocityTracker?.clear()
        mVelocityTracker?.recycle()
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {

    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return true
    }
}