package com.winwang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class OptionRateView extends View {

    private Paint leftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint rightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int width;
    private int height;
    private float leftRateRation;
    private float leftRate;
    private float rightRateRatio;
    private float rightRate;
    private float gapWidth = 10f;


    public OptionRateView(Context context) {
        this(context, null);
    }

    public OptionRateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public OptionRateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
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

    /**
     * 基本初始化操作-根据需求是否需要
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        leftPaint.setColor(Color.RED);
        leftPaint.setStyle(Paint.Style.FILL);

        rightPaint.setColor(Color.GREEN);
        rightPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 分别设置数字和比率
     *
     * @param leftRatio
     * @param rightRatio
     */
    public void setRateRation(float leftRatio, float rightRatio) {
        this.leftRate = leftRatio;
        this.rightRate = rightRatio;
        this.leftRateRation = leftRatio / (leftRatio + rightRatio);
        this.rightRateRatio = rightRatio / (leftRatio + rightRatio);
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        drawOptionRateView(canvas);
    }

    private void drawOptionRateView(Canvas canvas) {
        Path leftPath = new Path();
        Path rightPaht = new Path();
        //绘制左边
        float xCenter = leftRateRation * width;
        leftPath.moveTo(xCenter - gapWidth, 0);
        leftPath.lineTo(height, 0);
        leftPath.arcTo(0, 0, (float) height, (float) height, -90f, -180f, false);
        leftPath.lineTo(xCenter - 30 - gapWidth, height);
        leftPath.close();
        canvas.drawPath(leftPath, leftPaint);

        rightPaht.moveTo(xCenter + gapWidth, 0);
        rightPaht.lineTo((float) width - (float) height, 0);
        rightPaht.arcTo((float) width - (float) height, 0, (float) width, (float) height, -90, 180, false);
        rightPaht.lineTo(xCenter - 30 + gapWidth, height);
        rightPaht.close();
        canvas.drawPath(rightPaht, rightPaint);

    }
}
