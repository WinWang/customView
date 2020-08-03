package com.winwang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.winwang.myapplication.bean.LineChartBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by WinWang on 2020/7/24
 * Description->
 */
public class LineChart extends View {

    private Paint mAxisPaint; //轴线画笔-坐标画笔
    private Paint mDashPaint; //虚线画笔
    private Paint mLinePaint; //画折线图画笔
    private Paint mShaderPaint; //阴影画笔
    private Paint mCirclePaint; //小圆圈的画笔
    private Paint mTextPaint; //文字画笔
    private Paint mTipsLinePaint; //提示线条画笔
    private int lineWidth = 1; //边框宽度
    private int borderWidth = 80;
    private String colorBorder = "#DBDBDB";
    private String lineColor = "#EA4C43"; //折现的颜色
    private String lineColor1 = "#FFA957"; //折现的颜色
    private String tipsLineColor = "#528EDA"; //折现的颜色
    private int colorLineWidth = 2;
    private int textSize = 30;
    private float xEndData; //数据在屏幕停止位置x轴
    private float xStartData;//数据在屏幕开始绘制的位置x轴
    private DecimalFormat decimalFormat = new DecimalFormat("#0.0");
    private float totalCount; //总的点数个数


    private float chartMaxY; //y轴最大值
    private int mWidth;
    private int mHeight;
    private List<LineChartBean> list;
    private List<List<LineChartBean>> multiList;
    private float x;
    private float y;
    private boolean drawTipsLine = false;
    private int chartHeigh;
    private int chartWidth;
    private int slidePostion;

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
        PathEffect effects = new DashPathEffect(new float[]{4, 4}, 0);
        mDashPaint.setPathEffect(effects);
        //初始化折现条
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(colorLineWidth);
        mLinePaint.setColor(Color.parseColor(lineColor));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);
//        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        CornerPathEffect cornerPathEffect = new CornerPathEffect(10);
        mLinePaint.setPathEffect(cornerPathEffect);

        //阴影画笔
        mShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShaderPaint.setStrokeWidth(colorLineWidth);
        mShaderPaint.setColor(Color.parseColor(lineColor));
        mShaderPaint.setPathEffect(cornerPathEffect);


        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor(lineColor));

        //提示线条
        mTipsLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTipsLinePaint.setStrokeWidth(colorLineWidth);
        mTipsLinePaint.setColor(Color.parseColor(tipsLineColor));
        mTipsLinePaint.setStyle(Paint.Style.STROKE);


        //文字画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(Color.parseColor(colorBorder));
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
        list = chartBeanList;
        totalCount = list.size() - 1;
//        chartMaxY = 400;
        for (LineChartBean lineChartBean : chartBeanList) {
            chartMaxY = Math.max(chartMaxY, lineChartBean.getValue());
        }
        invalidate();
    }

    public void setMultiChartData(List<List<LineChartBean>> chartBeanList) {
        multiList = chartBeanList;
//        chartMaxY = 400;
        for (List<LineChartBean> lineChartBeans : chartBeanList) {
            totalCount = lineChartBeans.size() - 1;
            for (LineChartBean lineChartBean : lineChartBeans) {
                chartMaxY = Math.max(chartMaxY, lineChartBean.getValue());
            }
        }
        invalidate();
    }


    private void initSize() {
        mWidth = getWidth();
        mHeight = getHeight();
        chartHeigh = mHeight - borderWidth * 2;
        chartWidth = mWidth - borderWidth * 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initSize();
        drawGridBack(canvas);
        drawLine(canvas);
        drawTipsLine(canvas);
    }

    /**
     * 绘制提示线条
     *
     * @param canvas
     */
    private void drawTipsLine(Canvas canvas) {
        if (drawTipsLine) {
            if (list != null && list.size() > 0) {
                LineChartBean lineChartBean = list.get(slidePostion);
                float xPostion = lineChartBean.getxPosition();
                float yPostion = lineChartBean.getyPosition();
                canvas.drawLine(xPostion, mHeight - borderWidth, xPostion, borderWidth, mTipsLinePaint);
                canvas.drawCircle(xPostion, yPostion, 8, mCirclePaint);
            }
            if (multiList != null && multiList.size() > 0) {
                List<LineChartBean> lineChartBeanList = multiList.get(0);
                float v = lineChartBeanList.get(slidePostion).getxPosition();
                canvas.drawLine(v, mHeight - borderWidth, v, borderWidth, mTipsLinePaint);
                //修改逻辑更改选中tipsLine绘制原点


            }
        }
    }

    /**
     * 绘制网格背景
     *
     * @param canvas
     */
    private void drawGridBack(Canvas canvas) {
        Rect rect = new Rect(borderWidth, borderWidth, mWidth - borderWidth, mHeight - borderWidth);
        canvas.drawRect(rect, mAxisPaint); //绘制背景方框
        float perY = chartMaxY / 4;
        int yGap = rect.height() / 4; //y轴的间隙
//        getTextHeight();
        //绘制Y轴坐标
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        float textHeight = getTextHeight();
        for (int i = 0; i < 5; i++) {
            canvas.drawText(decimalFormat.format(i * perY), borderWidth, mHeight - borderWidth - yGap * i + textHeight / 3, mTextPaint);
            if (i != 0 && i != 4) {
                canvas.drawLine(borderWidth, mHeight - borderWidth - yGap * i, mWidth - borderWidth, mHeight - borderWidth - yGap * i, mDashPaint);
            }
        }
        //回值X轴坐标
        float xLableY = mHeight - borderWidth + textHeight; //y轴的标签位置
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    mTextPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("9:30", borderWidth, xLableY, mTextPaint);
                    break;

                case 1:
                    mTextPaint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawLine(mWidth / 2, mHeight - borderWidth, mWidth / 2, borderWidth, mDashPaint);
                    canvas.drawText("11:30/13:30", mWidth / 2, xLableY, mTextPaint);
                    break;

                case 2:
                    mTextPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("15:30", mWidth - borderWidth, xLableY, mTextPaint);
                    break;
            }
        }
    }

    private float getTextHeight() {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float ascent = fontMetrics.ascent;
        float descent = fontMetrics.descent;
        float textHeigh = descent - ascent;
        return textHeigh;
    }


    /**
     * 绘制线条-折现
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        float valueHeigh = chartHeigh / chartMaxY; //平均每个数字表示的高度
        float valueWidth = chartWidth / totalCount;//平均每个点的宽度
        if (list != null && list.size() > 0) {
            drawSingleLine(canvas, list, valueHeigh, valueWidth);
        } else if (multiList != null && multiList.size() > 0) {
            for (int i = 0; i < multiList.size(); i++) {
                if (i == 0) {
                    mLinePaint.setColor(Color.parseColor(lineColor));
                } else {
                    mLinePaint.setColor(Color.parseColor(lineColor1));
                }
                drawSingleLine(canvas, multiList.get(i), valueHeigh, valueWidth);
            }
        }


    }

    private void drawSingleLine(Canvas canvas, List<LineChartBean> chartList, float valueHeigh, float valueWidth) {
        Path path = new Path();
        Path shadowPath = new Path();
//        xEndData = chartList.get(chartList.size() - 1).getMinute() * valueWidth + borderWidth;
        xEndData = chartList.size() * valueWidth + borderWidth;
        xStartData = borderWidth;
        String colorString = "";
        for (int i = 0; i < chartList.size(); i++) {
            LineChartBean lineChartBean = chartList.get(i);
            float value = lineChartBean.getValue();
            float yData = chartHeigh - value * valueHeigh + borderWidth;
            float xData = i * valueWidth + borderWidth;
//            canvas.drawCircle(xData, yData, 3, mCirclePaint);
            if (i == 0) {
                path.moveTo(xData, yData);
                shadowPath.moveTo(xData, yData);
            } else {
                path.lineTo(xData, yData);
                shadowPath.lineTo(xData, yData);
            }
            if (i == chartList.size() - 1) {
                shadowPath.lineTo(xData, mHeight - borderWidth);
            }
            lineChartBean.setxPosition(xData);
            lineChartBean.setyPosition(yData);
            colorString = lineChartBean.getColor();
        }
        shadowPath.lineTo(borderWidth, mHeight - borderWidth);
        shadowPath.close();
        getSlideNumber(canvas, chartList);
        LinearGradient linearGradient = new LinearGradient(mWidth / 2, borderWidth, mWidth / 2, mHeight - borderWidth, Color.parseColor("#5f" + colorString), Color.parseColor("#5fffffff"), Shader.TileMode.REPEAT);
        mShaderPaint.setShader(linearGradient);
        canvas.drawPath(shadowPath, mShaderPaint);
        mLinePaint.setColor(Color.parseColor("#" + colorString));
        canvas.drawPath(path, mLinePaint);
    }

    /**
     * 获取滑动的点数
     *
     * @param chartList
     */
    private void getSlideNumber(Canvas canvas, List<LineChartBean> chartList) {
        if (x <= xEndData && x >= xStartData) {
//            slidePostion = (int) ((x - borderWidth) / (mWidth - borderWidth * 2) * totalCount);
            float v = (x - borderWidth) / chartWidth * totalCount;
            slidePostion = Math.round(v);
        } else if (x < xStartData) {
            slidePostion = 0;
        } else if (x > xEndData) {
            if (list != null && list.size() > 0) {
                slidePostion = list.size() - 1;
            } else if (multiList != null && multiList.size() > 0) {
                List<LineChartBean> lineChartBeans = multiList.get(0);
                if (lineChartBeans != null && lineChartBeans.size() > 0) {
                    slidePostion = lineChartBeans.size() - 1;
                }
            }
        }
        String drawText = "";
        if (slidePostion > chartList.size()) {
            drawText = String.valueOf(chartList.get(chartList.size() - 1).getValue());
        } else {
            drawText = String.valueOf(chartList.get(slidePostion).getValue());
        }
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(drawText, mWidth / 2, getTextHeight(), mTextPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                if (x >= xStartData && x <= xEndData) {
                    postDelayed(drawTipRunnable, 500);
                }
            case MotionEvent.ACTION_MOVE:
                float xTemp = event.getX();
                float yTemp = event.getY();
                float distance = Math.abs(yTemp - y) - Math.abs(xTemp - x);
                if (distance > 0) {
                    if (!drawTipsLine) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        removeCallbacks(drawTipRunnable);
                    }
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (drawTipsLine) {
                        this.x = event.getX();
                        y = event.getY();
                        if (this.x >= xStartData && this.x <= xEndData) {
                            postInvalidate();
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                removeCallbacks(drawTipRunnable);
                reInitData();
                postInvalidate();
                break;
        }

        return true;
    }

    private Runnable drawTipRunnable = new Runnable() {
        @Override
        public void run() {
            drawTipsLine = true;
            postInvalidate();
        }
    };


    private void reInitData() {
        drawTipsLine = false;
        x = 0;
        y = 0;
    }
}
