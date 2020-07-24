package com.winwang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WinWang on 2020/7/23
 * Description->
 */
public class CircleChart extends View {

    //圆环色块
    private String redColor1 = "#B9190F";
    private String redColor2 = "#D22117";
    private String redColor3 = "#EA4C43";
    private String redColor4 = "#FF5950";
    private String greenColor1 = "#087A3E";
    private String greenColor2 = "#06974B";
    private String greenColor3 = "#1BB865";
    private String greenColor4 = "#0CCD68";
    private List<Integer> chartList = new ArrayList<>();
    private List<String> colorList = new ArrayList<>();
    private int ringWidth;
    private Paint mPaint;

    public CircleChart(Context context) {
        this(context, null);
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setDataList(List<Float> dataList) {
        if (dataList != null && dataList.size() > 0) {
            float sum = 0;
            for (int i = 0; i < dataList.size(); i++) {
                Float data = dataList.get(i);
                sum += data;
            }
            int percents = 0;
            for (Float aFloat : dataList) {
                int percent = Math.round(aFloat / sum * 360); //每一份的角度
                percents += percent;
                chartList.add(percent);

            }
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //确保宽高不一是画出来正方形
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }


    private void init(AttributeSet attrs) {
        ringWidth = 100;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(ringWidth);
        initColorList();
    }

    private void initColorList() {
        colorList.add(greenColor1);
        colorList.add(greenColor2);
        colorList.add(greenColor3);
        colorList.add(greenColor4);
        colorList.add(redColor1);
        colorList.add(redColor2);
        colorList.add(redColor3);
        colorList.add(redColor4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        if (chartList != null && chartList.size() > 0) {
            int center = getWidth() / 2;
            int radius = getWidth() / 2 - ringWidth / 2;
            int angleGreenSum = -90;
            int angleRedSum = -90;
            RectF rect = new RectF(center - radius, center - radius, center + radius, center + radius);
            for (int i = 0; i < chartList.size(); i++) {
                String colorString = colorList.get(i);
                mPaint.setColor(Color.parseColor(colorString));
                if (i < 4) { //绿色
                    canvas.drawArc(rect, angleGreenSum - 1, chartList.get(i) + 1, false, mPaint);
                    angleGreenSum += chartList.get(i);
                } else { //红色
                    angleRedSum -= chartList.get(i);
                    canvas.drawArc(rect, angleRedSum - 1, chartList.get(i) + 1, false, mPaint);
                }
            }
        }
    }
}
