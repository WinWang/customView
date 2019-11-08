package com.winwang.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class ScoreStepView extends View {

    private int mFontColor; //前置颜色
    private int mBackColor; //背景颜色
    private int mTxtColor; //文字颜色
    private int mTxtSize = 100; //文字大小
    private int mLineWidth = 30; //线条的粗细
    private Paint mBackPaint; //背景色画笔
    private Paint mFontPaint; //前景色画笔
    private Paint mTxtPaint; //文字画笔
    private Paint mRulerPaint; //灰色刻度画笔
    private Paint mCirclePaint; //中间紫色圆弧
    private int maxStep;//最大步数
    private int currentStep; //当前步数
    private float rulerCalDegree = 270 / 30;
    private Bitmap mBitmap;
    private Paint mArrowPaint; //指示器指针
    private int mBitmapHeight;
    private int mBitmapWidth;

    public ScoreStepView(Context context) {
        this(context, null);
    }

    public ScoreStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义控件属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScoreStepView);
        mFontColor = typedArray.getColor(R.styleable.ScoreStepView_SfontColor, context.getResources().getColor(R.color.colorPrimary));
        mBackColor = typedArray.getColor(R.styleable.ScoreStepView_SbackColor, context.getResources().getColor(R.color.colorAccent));
        mTxtColor = typedArray.getColor(R.styleable.ScoreStepView_StxtColor, context.getResources().getColor(R.color.colorAccent));
        mTxtSize = typedArray.getDimensionPixelSize(R.styleable.ScoreStepView_StxtSize, mTxtSize);
        mLineWidth = typedArray.getInt(R.styleable.ScoreStepView_SlineWidth, mLineWidth);

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


        //灰色刻度画笔初始化
        mRulerPaint = new Paint();
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setStyle(Paint.Style.FILL);
        mRulerPaint.setStrokeWidth(8);
        mRulerPaint.setColor(Color.GRAY);
        mRulerPaint.setStrokeCap(Paint.Cap.ROUND);

        //紫色圆环
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.parseColor("#1C2241"));
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //初始化指针画笔
        mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPaint.setColor(Color.GREEN);

        //获取指针图片及宽高
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow);
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
        System.out.println("图片的高度" + mBitmapHeight);
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

    public void setCurrentStepWithoutAnim(int step) {
        currentStep = step;
        postInvalidate();
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
        SweepGradient sweepGradient = new SweepGradient(center, center, Color.parseColor("#8DC6F4"), Color.parseColor("#FC504F"));
        Matrix matrix = new Matrix();
        matrix.setRotate(50, center, center);
        sweepGradient.setLocalMatrix(matrix);
        mBackPaint.setShader(sweepGradient);
        canvas.drawArc(rect, 135, 270, false, mBackPaint);

        //设置前景色渐变
        float ratio = (float) currentStep / maxStep;
//        SweepGradient gradient = new SweepGradient(center, center, Color.parseColor("#2C9300"), Color.parseColor("#113B00"));
//        Matrix matrixFont = new Matrix();
//        matrixFont.setRotate(135, center, center);
//        gradient.setLocalMatrix(matrix);
//        mFontPaint.setShader(gradient);
//        canvas.drawArc(rect, 135, ratio * 270, false, mFontPaint);//前置进度

        //绘制刻度
        drawRuleCal(canvas, center);

        //绘制内部原型图
        canvas.drawCircle(center, center, radius / 1.5f, mCirclePaint);

        drawArrow(canvas, center, ratio);
        //绘制中间文字
        drawTxt(canvas, radius);


    }

    /**
     * 绘制文字
     *
     * @param canvas
     * @param radius
     */
    private void drawTxt(Canvas canvas, int radius) {
        String stepText = currentStep + "";
        Rect bounds = new Rect();
        mTxtPaint.getTextBounds(stepText, 0, stepText.length(), bounds);
        int width = bounds.width();
        int height = bounds.height();

        int startX = radius - width / 2;
        int startY = radius + height / 2;
        canvas.drawText(stepText, startX, startY, mTxtPaint);
    }

    //绘制箭头
    private void drawArrow(Canvas canvas, int center, float ratio) {
        canvas.save();
        canvas.rotate(135 + 270 * ratio, center, center);
        Matrix matrix = new Matrix();
//        matrix.preRotate(center,center);
//        canvas.drawBitmap(mBitmap, cente)r, center - mBitmapHeight / 2, mArrowPaint);
        canvas.drawBitmap(mBitmap, center * 2 - 100, center - mBitmapHeight / 2, mArrowPaint);
        canvas.restore();
    }

    //绘制灰色刻度值
    private void drawRuleCal(Canvas canvas, int center) {

        for (int i = 0; i <= 30; i++) {
            canvas.save();
            canvas.rotate(135 + rulerCalDegree * i, center, center);
            canvas.drawLine(center * 2 - 80, center, center * 2 - 55, center, mRulerPaint);
            canvas.restore();
        }
    }
}
