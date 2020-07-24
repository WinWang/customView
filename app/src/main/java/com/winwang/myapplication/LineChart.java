package com.winwang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.winwang.myapplication.bean.LineChartBean;

import java.util.List;

/**
 * Created by WinWang on 2020/7/24
 * Description->
 */
public class LineChart extends View {

    private Paint mAxisPaint; //轴线画笔-坐标画笔
    private Paint mDashPaint; //虚线画笔
    private Paint mLinePaint; //画折线图画笔
    private Paint mTextPaint; //文字画笔
    private int lineWidth = 3; //边框宽度
    private int borderWidth = 80;
    private int dashLineWidth = 2; //虚线宽度
    private String colorBorder = "#DBDBDB";

    private float chartMaxY; //y轴最大值
    private int mWidth;
    private int mHeight;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //初始化线条画笔
        mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAxisPaint.setStrokeWidth(lineWidth);
        mAxisPaint.setColor(Color.parseColor(colorBorder));
        mAxisPaint.setStyle(Paint.Style.STROKE);
        //初始化虚线框
        mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashPaint.setColor(Color.parseColor(colorBorder));
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(dashLineWidth);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        mDashPaint.setPathEffect(effects);
        //初始化折现条
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(lineWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        //文字画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(Color.parseColor(colorBorder));
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int finalWidth, finalHeight;

        // 不明
        finalWidth = getSuggestedMinimumWidth();
        finalHeight = getSuggestedMinimumHeight();

        if (widthMode == MeasureSpec.EXACTLY) {
            finalWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            int padding = getPaddingLeft() + getPaddingRight();
            finalWidth += padding;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            finalHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            int padding = getPaddingTop() + getPaddingBottom();
            finalHeight += padding;
        }
        setMeasuredDimension(finalWidth, finalHeight);
    }

    public void setChartData(List<LineChartBean> chartBeanList) {
        for (LineChartBean lineChartBean : chartBeanList) {
            chartMaxY = Math.max(chartMaxY, lineChartBean.getValue());
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initSize();
        drawGridBack(canvas);
        drawLine(canvas);
    }

    private void initSize() {
        mWidth = getWidth();
        mHeight = getHeight();

    }


    /**
     * 绘制网格背景
     *
     * @param canvas
     */
    private void drawGridBack(Canvas canvas) {
        Rect rect = new Rect(borderWidth, borderWidth, mWidth - borderWidth, mHeight - borderWidth);
        canvas.drawRect(rect, mAxisPaint);
        float perY = chartMaxY / 5;
        int yGap = rect.height() / 4; //y轴的间隙
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float ascent = fontMetrics.ascent;
        float descent = fontMetrics.descent;
        float textHeigh = descent - ascent;
        for (int i = 0; i < 5; i++) {
            canvas.drawText(String.valueOf(i * perY), borderWidth, mHeight - borderWidth - yGap * i - textHeigh, mTextPaint);

        }
    }


    /**
     * 绘制线条-折现
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {

    }

}
