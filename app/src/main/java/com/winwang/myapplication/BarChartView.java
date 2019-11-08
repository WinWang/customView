package com.winwang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WinWang on 2019/7/17
 * Description->自定义柱状图
 */
public class BarChartView extends View {

    private Paint mTextPaint; //文字画笔
    private Paint mRedPaint; //红色柱状图画笔
    private Paint mGreenPaint;//绿色柱状图画笔
    private List<BarChartBean> chartData = new ArrayList<>();
    private int height;
    private int width;
    private float maxHeight;

    private int spaceWidth = 60;//柱状图宽度
    private float mHeightScale;
    private int textSize = 23;

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#FF333333"));
        mTextPaint.setTextSize(textSize);

        mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedPaint.setColor(Color.parseColor("#FFD93A32"));
        mRedPaint.setStrokeWidth(spaceWidth);

        mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGreenPaint.setColor(Color.parseColor("#FF1EA373"));
        mGreenPaint.setStrokeWidth(spaceWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println(">>>123");
        height = getMeasuredHeight();
        width = getMeasuredWidth();
    }

    public void setDataList(List<BarChartBean> dataList) {
        maxHeight = 0;
        if (dataList != null && dataList.size() > 0) {
            //计算出图标的最高位置
            for (int i = 0; i < dataList.size(); i++) {
                float value = dataList.get(i).getValue();
                if (value > maxHeight) {
                    maxHeight = value;
                }
            }

            maxHeight = maxHeight;
            //设置高度比
            Log.d(">>>>>>", mHeightScale + "");
            chartData.clear();
            chartData.addAll(dataList);
            postInvalidate();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (chartData != null && chartData.size() > 0) {
            int space = (int) (width / 6);
            int startX = space / 2; //初始化展示6根柱子(X轴位子)
            int startY = height - 40;
            mHeightScale = startY / maxHeight;
            int pWidth = startX;
            for (int i = 0; i < chartData.size(); i++) {
                float value = chartData.get(i).getValue();
                String lableText = chartData.get(i).getxLableName();
                //绘制柱状图
                if (i <= 2) {
                    Log.d(">>>>>>1", (height - value * mHeightScale) + "");
                    float topPos = startY - value * mHeightScale + 50;
                    if (topPos >= startY) {
                        topPos = startY - 5;
                    }
                    canvas.drawLine(pWidth, startY, pWidth, topPos, mRedPaint);
                    mTextPaint.setColor(Color.parseColor("#FFD93A32"));
                    String valusText = value + "";
                    Rect bounds = new Rect();
                    mTextPaint.getTextBounds(valusText, 0, valusText.length(), bounds);
                    float offSetX = (bounds.right - bounds.left) / 2;
                    canvas.drawText(valusText, pWidth - offSetX, topPos - 10, mTextPaint);
                } else {
                    float topPos = startY - value * mHeightScale + 50;
                    if (topPos >= startY) {
                        topPos = startY - 5;
                    }
                    canvas.drawLine(pWidth, startY, pWidth, topPos, mGreenPaint);
                    mTextPaint.setColor(Color.parseColor("#FF1EA373"));
                    String valusText = value + "";
                    Rect bounds = new Rect();
                    mTextPaint.getTextBounds(valusText, 0, valusText.length(), bounds);
                    float offSetX = (bounds.right - bounds.left) / 2;
                    canvas.drawText(valusText, pWidth - offSetX, topPos - 10, mTextPaint);
                }
                //绘制底部文字
                mTextPaint.setColor(Color.parseColor("#333333"));
                Rect bounds = new Rect();
                mTextPaint.getTextBounds(lableText, 0, lableText.length(), bounds);
                float offSetX = (bounds.right - bounds.left) / 2;
                canvas.drawText(lableText, pWidth - offSetX, height - 20, mTextPaint);
                pWidth += space;
            }
        }

    }
}
