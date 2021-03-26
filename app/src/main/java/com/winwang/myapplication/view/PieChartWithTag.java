package com.winwang.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.winwang.myapplication.R;

/**
 * Created by WinWang on 2021/3/20
 * Description->
 */
public class PieChartWithTag extends View {
    private final static int LINE_LENGTH = 80;//直线线的长度
    private final static int CORNER_LENGTH = 10;//折线的长度
    private final static int TEXT_MARGIN = 5;//线的长度
    private final static int TEXT_SIZE = 10;
    //isPieWidth：android:layout_width决定的饼状图的大小还是饼状图+饼状图文字数据的大小
    //true:layout_width决定饼状图+文字数据的大小
    //false:layout_width决定饼状图的大小，文字数据不算在里面
    private boolean isPieWidth;
    private float[] progressS;//所有数据
    private int[] colorS;//所有颜色
    private Paint paint;//画笔
    private Paint whitePaint; //白底圈圈
    private float percent;//百分比
    private float centreX, centreY;//饼状图的中心点（centreX，centreY）
    private float r;//半径
    private float rWhite; //白色圆圈半径

    public PieChartWithTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setDither(true);//抗抖动
        paint.setAntiAlias(true);//抗锯齿
        paint.setStrokeWidth(2);//画笔粗细
        paint.setStyle(Paint.Style.FILL);//画笔样式为填充
        paint.setStrokeCap(Paint.Cap.ROUND);//设置笔端样式为圆
        paint.setTextSize(TEXT_SIZE);
        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.parseColor("#ffffff"));
        whitePaint.setStyle(Paint.Style.FILL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PieChartWithTag);
        if (array != null) {
            int color0 = array.getInt(R.styleable.PieChartWithTag_pie_color0, 0);
            int color1 = array.getInt(R.styleable.PieChartWithTag_pie_color1, 0);
            int color2 = array.getInt(R.styleable.PieChartWithTag_pie_color2, 0);
            int color3 = array.getInt(R.styleable.PieChartWithTag_pie_color3, 0);
            int color4 = array.getInt(R.styleable.PieChartWithTag_pie_color4, 0);
            colorS = new int[]{color0, color1, color2, color3, color4};
            float progress0 = array.getFloat(R.styleable.PieChartWithTag_pie_progress0, 0);
            float progress1 = array.getFloat(R.styleable.PieChartWithTag_pie_progress1, 0);
            float progress2 = array.getFloat(R.styleable.PieChartWithTag_pie_progress2, 0);
            float progress3 = array.getFloat(R.styleable.PieChartWithTag_pie_progress3, 0);
            float progress4 = array.getFloat(R.styleable.PieChartWithTag_pie_progress4, 0);
            percent = 360 / (progress0 + progress1 + progress2 + progress3 + progress4);
            progressS = new float[]{progress0, progress1, progress2, progress3, progress4};
            isPieWidth = array.getBoolean(R.styleable.PieChartWithTag_pie_layoutWidth_isPieWidth, false);
            boolean isRank = array.getBoolean(R.styleable.PieChartWithTag_pie_isRank, false);//是否进行排序
            if (isRank) {
                //冒泡(最大的在在前面)
                for (int i = 0; i < progressS.length; i++) {
                    for (int j = 0; j < progressS.length - 1 - i; j++) {
                        if (progressS[j] < progressS[j + 1]) {
                            float num = progressS[j + 1];
                            int color = colorS[j + 1];
                            progressS[j + 1] = progressS[j];
                            colorS[j + 1] = colorS[j];
                            progressS[j] = num;
                            colorS[j] = color;
                        }
                    }
                }
            }
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //wrap_content:-2
        //match_parent:-1
        //android:layout_width="150dp":150*2
        //android:layout_width="70dp":70*2
        int width = getLayoutParams().width;
        int height = getLayoutParams().height;
        if (width > 0 || height > 0) {
            int index = width > height ? width : height;
            if (isPieWidth) {
                //width = 饼+文字数据宽度
                centreX = centreY = index - (LINE_LENGTH * 2) >> 1;
                r = index >> 2;
                rWhite = r - 20;
                setMeasuredDimension(index, index);
            } else {
                //width = 饼的宽度
                centreX = centreY = (index >> 1) + LINE_LENGTH;
                r = index >> 2;
                rWhite = r - 20;
                setMeasuredDimension(index + LINE_LENGTH * 2, index + LINE_LENGTH * 2);
            }
        } else {
            centreX = centreY = 200;
            r = 100;
            rWhite = r - 20;
            setMeasuredDimension(400, 400);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(colorS[0]);
        //计算最大文字的宽度
        float textLength = (String.valueOf(progressS[0]).length() - 1) / 1.5f * TEXT_SIZE;
        //为保证饼状图的美观，所以这统一X坐标
        float referenceRightX = centreX + r + LINE_LENGTH - textLength;
        float referenceLeftX = centreX - r - LINE_LENGTH + textLength;
        float p = 0;
        canvas.drawCircle(centreX, centreY, rWhite, whitePaint);
        for (int i = 0; i < progressS.length; i++) {
            if (progressS[i] == 0) {
                return;
            }
            if (i != 0) {
                p += progressS[i - 1] * percent;
            }
            paint.setColor(colorS[i]);
            canvas.drawArc(centreX - r, centreX - r, centreX + r, centreX + r, i == 0 ? 0 : p, progressS[i] * percent, true, paint);
            float data = p + (percent * progressS[i]) / 2;
            double a = Math.toRadians(data);//把数字90 转换成 90度
            double sin = Math.sin(a);
            double cos = Math.cos(a);
            //饼的任意一点坐标
            float v = (float) (centreX + r * cos);
            float v1 = (float) (centreY + r * sin);
            //折线坐标
            float cornerX = (float) (centreX + (r + CORNER_LENGTH) * cos);
            float cornerY = (float) (centreX + (r + CORNER_LENGTH) * sin);
            canvas.drawLine(v, v1, cornerX, cornerY, paint);
            //270°和90°分别是12点钟和6点钟，作为区别左右边的凭据
            if (data < 270 && data > 90) {
                //在左边
                canvas.drawLine(cornerX, cornerY, referenceLeftX, cornerY, paint);
                canvas.drawText(String.valueOf(progressS[i]), referenceLeftX - TEXT_MARGIN - textLength, cornerY + (TEXT_SIZE >> 2), paint);
            } else {
                //在右边
                canvas.drawLine(cornerX, cornerY, referenceRightX, cornerY, paint);
                canvas.drawText(String.valueOf(progressS[i]), referenceRightX + TEXT_MARGIN, cornerY + (TEXT_SIZE >> 2), paint);
            }
        }

    }

}

