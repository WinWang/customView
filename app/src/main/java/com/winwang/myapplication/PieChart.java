package com.winwang.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by WinWang on 2019/7/10
 * Description->
 */
public class PieChart extends ViewGroup {

    private boolean mShowText;
    private int mTextPos;
    private Paint mTextPaint;

    public float getTextHeight() {
        return mTextHeight;
    }

    public void setTextHeight(float textHeight) {
        mTextHeight = textHeight;
        invalidate();
    }

    public float getPointerRadius() {
        return mPointerRadius;
    }

    public void setPointerRadius(float pointerRadius) {
        mPointerRadius = pointerRadius;
        invalidate();
    }

    private float mTextHeight;
    private float mPointerRadius;

    public PieChart(Context context) {
        super(context);
        init();
    }


    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs);
        init();
    }

    /**
     * 初始化方法，比如初始化画笔等
     */
    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#ffffff"));
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }
    }


    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieChart, 0, 0);
        try {
            mShowText = typedArray.getBoolean(R.styleable.PieChart_showText, false);
            mTextPos = typedArray.getInteger(R.styleable.PieChart_labelPosition, 0);
        } catch (Exception e) {

        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
