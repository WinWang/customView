package com.winwang.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WinWang on 2019/8/20
 * Description->计步器View
 */
public class StepView extends View {

    private int mFontColor; //前置颜色
    private int mBackColor; //背景颜色
    private int mTxtColor; //文字颜色
    private int mTxtSize = 100; //文字大小
    private int mLineWidth = 30; //线条的粗细
    private Paint mBackPaint; //背景色画笔
    private Paint mFontPaint; //前景色画笔
    private Paint mTxtPaint; //文字画笔
    private int maxStep;//最大步数
    private int currentStep; //当前步数

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义控件属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mFontColor = typedArray.getColor(R.styleable.StepView_fontColor, context.getResources().getColor(R.color.colorPrimary));
        mBackColor = typedArray.getColor(R.styleable.StepView_backColor, context.getResources().getColor(R.color.colorAccent));
        mTxtColor = typedArray.getColor(R.styleable.StepView_txtColor, context.getResources().getColor(R.color.colorAccent));
        mTxtSize = typedArray.getDimensionPixelSize(R.styleable.StepView_txtSize, mTxtSize);
        mLineWidth = typedArray.getInt(R.styleable.StepView_lineWidth, mLineWidth);

        typedArray.recycle();

        //内弧
        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStrokeWidth(mLineWidth);
        mBackPaint.setColor(mBackColor);
        mBackPaint.setStrokeCap(Paint.Cap.ROUND);//设置下方为圆形
        mBackPaint.setStyle(Paint.Style.STROKE);//设置内部为空心


        //外弧
        mFontPaint = new Paint();
        mFontPaint.setAntiAlias(true);
        mFontPaint.setStrokeWidth(mLineWidth);
        mFontPaint.setColor(mFontColor);
        mFontPaint.setStrokeCap(Paint.Cap.ROUND);//设置下方为圆形
        mFontPaint.setStyle(Paint.Style.STROKE);//设置内部为空心

        //文字
        mTxtPaint = new Paint();
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setTextSize(mTxtSize);
        mTxtPaint.setColor(mTxtColor);

    }


    public void setCurrentStep(int step) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, step);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float animatedValue = (Float) animation.getAnimatedValue();
                currentStep = animatedValue.intValue();
                postInvalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.start();

    }


    public void setMaxStep(int step) {
        this.maxStep = step;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //确保宽高不一是画出来正方形
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = getWidth() / 2 - mLineWidth / 2;
        RectF rect = new RectF(center - radius, center - radius, center + radius, center + radius);
        //设置背景色渐变
        SweepGradient sweepGradient = new SweepGradient(center, center, Color.parseColor("#FFCD33"), Color.parseColor("#EA6F5A"));
        Matrix matrix = new Matrix();
        matrix.setRotate(50, center, center);
        sweepGradient.setLocalMatrix(matrix);
        mBackPaint.setShader(sweepGradient);
        canvas.drawArc(rect, 135, 270, false, mBackPaint);

        //设置前景色渐变
        float ratio = (float) currentStep / maxStep;
        SweepGradient gradient = new SweepGradient(center, center, Color.parseColor("#2C9300"), Color.parseColor("#113B00"));
        Matrix matrixFont = new Matrix();
        matrixFont.setRotate(135, center, center);
        gradient.setLocalMatrix(matrix);
        mFontPaint.setShader(gradient);
        canvas.drawArc(rect, 135, ratio * 270, false, mFontPaint);//前置进度

        //绘制中间文字
        String stepText = currentStep + "";
        Rect bounds = new Rect();
        mTxtPaint.getTextBounds(stepText, 0, stepText.length(), bounds);
        int width = bounds.width();
        int height = bounds.height();

        int startX = radius - width / 2;
        int startY = radius + height / 2;
        canvas.drawText(stepText, startX, startY, mTxtPaint);


    }
}
