package com.winwang.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WinWang on 2019/8/30
 * Description->蜘蛛网图
 */
public class SpiderChart extends View {

    private int mBackcolor;
    private int mWidth; //控件宽
    private int mHeight; //控件高


    public SpiderChart(Context context) {
        this(context, null);
    }

    public SpiderChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpiderChart);
        mBackcolor = typedArray.getColor(R.styleable.SpiderChart_spiBackColor, context.getResources().getColor(R.color.colorPrimary));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //确保宽高不一是画出来正方形
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }


    /**
     * 获取宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = getWidth();
        mHeight = getHeight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
