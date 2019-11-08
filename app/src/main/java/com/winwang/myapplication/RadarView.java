package com.winwang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 蜘蛛网
 */
public class RadarView extends View {

    /**
     * 半径
     */
    private float radius;

    /**
     * 正多边形的画笔
     */
    private Paint polygonPaint;

    /**
     * 画文本的画笔
     */
    private Paint textPaint;

    /**
     * 正多边形的路径
     */
    private Path polygonPath;

    /**
     * 绘制区域
     */
    private Paint valuePaint;

    /**
     * 6边形
     */
    private int count = 6;

    /**
     * 文本的偏移量,使用与扩大半径,让文本组成的圆的半径大于真实圆的半径
     */
    private int textOffset = 20;

    /**
     * 存放所有多边形的所有顶点的坐标值
     */
    private List<List<XYPoint>> xyList= new ArrayList<List<XYPoint>>();

    /**
     * 记录文本的坐标
     */
    private List<XYPoint> textXy= new ArrayList<XYPoint>();

    private String[] texts = {"第一个","第二个","第三个","第四个","第五个","第六个"};

    /**
     * 最大值
     */
    private float maxValue = 6;
    private float[] value = {2, 4.2f, 5, 3.5f, 4.6f,6};
    //private String[] texts = {"第一个","第二个","第三个","第四个","第五个"};

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        polygonPaint = new Paint();
        polygonPaint.setAntiAlias(true);
        polygonPaint.setStrokeWidth(4);
        polygonPaint.setColor(Color.WHITE);
        polygonPaint.setStyle(Paint.Style.STROKE);

        polygonPath = new Path();


        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(2);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setStrokeWidth(2);
        valuePaint.setColor(Color.parseColor("#6699ff"));
        valuePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //确定半径值,高宽最小值的0.6倍作为半径
        radius = Math.min(w,h)/2 * 0.6f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,getWidth(),getHeight(),polygonPaint);

        //移动原点到中点
        canvas.translate(getWidth()/2,getHeight()/2);
        canvas.drawArc(new RectF(-radius,-radius,radius,radius),0,360,false,polygonPaint);//正多边形的外切圆
        drawPolygon(canvas);//画正多边形
        drawLine(canvas);//画正多边形的线(连接定点)
        drawPeakText(canvas);//画文本

        drawValue(canvas);

    }

    /**
     * 绘制值与覆盖区域
     * @param canvas
     */
    private void drawValue(Canvas canvas) {
        Path path = new Path();
        valuePaint.setAlpha(255);//画点的值
        for (int i = 0; i < value.length; i++) {
            //获取这个值所暂半径的长度
            float v_radius = radius / maxValue * value[i];
            float x = getCosX(360 / count * i, v_radius);
            float y = getSinY(360 / count * i, v_radius);//该值对应的坐标
            if(i == 0){
                path.moveTo(x,y);
            }else{
                path.lineTo(x,y);
            }
            //画点
            canvas.drawCircle(x,y,10,valuePaint);
        }
        path.close();
        valuePaint.setAlpha(180);
        canvas.drawPath(path,valuePaint);
    }

    /**
     * 绘制顶点外的文本
     * @param canvas
     */
    private void drawPeakText(Canvas canvas) {
        for (int i = 0; i < texts.length; i++) {
            Rect rect = new Rect();
            textPaint.getTextBounds(texts[i],0,texts[i].length(),rect);
            int angle = 360 / count * i;//获取角度值
            canvas.drawText(texts[i],getOffsetCosX(angle,rect.width()),getOffsetSinY(angle,rect.height()),textPaint);
            //记录文本的左下角坐标 & 文本的宽度与高度
            XYPoint xyPoint = new XYPoint(getOffsetCosX(angle, rect.width()), getOffsetSinY(angle, rect.height()));
            xyPoint.textWidth = rect.width();
            xyPoint.textHeight = rect.height();
            textXy.add(xyPoint);
        }
    }

    /**
     * 根据象限加一个偏移量(文本的宽度)
     * @param angle  只有在90--270度才需要加一个文本宽度值的偏移量
     * @param width  文本宽度
     * @return
     */
    private float getOffsetCosX(int angle,float width){
        double hudu = (2*Math.PI / 360) * angle;//要偏移的角度,弧度与角度的转换
        if(hudu == Math.PI/2 || hudu == 3*Math.PI/2){
            //使得Y轴平分文本高度,Y轴上时
            return width/2;
        }
        if(getCosX(angle,radius)<0){
            //radius + textOffset 代表文字所在圆的半径值
            return getCosX(angle,radius + textOffset) - width;
        }else{
            return getCosX(angle,radius + textOffset);//这里扩大了半径的长度,使得文本不会与蜘蛛网交叉
        }
    }

    /**
     * 在Y轴的偏移量
     * @param angle  只有在0--180度才需要加一个高度值
     * @param height  文本高度
     * @return
     */
    private float getOffsetSinY(int angle,int height){
        double hudu = (2*Math.PI / 360) * angle;//要偏移的角度,弧度与角度的转换
        if(hudu == 0 || hudu == Math.PI){
            //使得Y轴平分文本高度,Y轴上时
            return height/2;
        }

        if(getSinY(angle,radius) > 0){
            return getSinY(angle,radius + textOffset) + height;
        }else{
            return getSinY(angle,radius + textOffset);//这里扩大了半径的长度,使得文本不会与蜘蛛网交叉
        }
    }

    /**
     * 画正多边形的线(连接顶点)
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        List<XYPoint> xyPoints = xyList.get(xyList.size() - 1);
        for (int i = 0; i < xyPoints.size(); i++) {
            polygonPath.reset();
            polygonPath.moveTo(0,0);//从原点到最外面那层正多边形的顶点之间的连线
            polygonPath.lineTo(xyPoints.get(i).x,xyPoints.get(i).y);
            canvas.drawPath(polygonPath,polygonPaint);
        }
    }

    /**
     * 画正多边形
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        for (int i=0;i<count;i++){
            float polygonRadius = (i+1) * radius/count;
            polygonPath.reset();//重置
            ArrayList<XYPoint> xyPoints = new ArrayList<>();
            for (int j=0;j<count;j++){
                if(j==0){
                    polygonPath.moveTo(polygonRadius,0);
                    xyPoints.add(new XYPoint(polygonRadius,0));//记录坐标值
                }else{
                    polygonPath.lineTo(getCosX(360/count*j,polygonRadius),getSinY(360/count*j,polygonRadius));
                    xyPoints.add(new XYPoint(getCosX(360/count*j,polygonRadius),getSinY(360/count*j,polygonRadius)));
                }
            }
            xyList.add(xyPoints);
            polygonPath.close();
            canvas.drawPath(polygonPath, polygonPaint);//画
        }

    }

    /**
     * 根据角度值获得X坐标
     * @param angle  角度值(0-360)
     * @param radius  半径
     * @return 该角度在X轴对应的坐标值
     */
    private float getCosX(int angle,float radius){
        double jiaodu = (2*Math.PI / 360) * angle;//要偏移的角度,弧度与角度的转换
        return (float) (radius*Math.cos(jiaodu));
    }

    /**
     * 根据角度值获得Y坐标
     * @param angle
     * @return
     */
    private float getSinY(int angle,float radius){
        double jiaodu = (2*Math.PI / 360) * angle;//要偏移的角度
        return (float) (radius*Math.sin(jiaodu));
    }

    /**
     * 记录坐标值
     */
    private class XYPoint{
        //XY轴的坐标
        public float x,y;

        public float textWidth;//文本的宽度
        public float textHeight;//文本的高度

        public XYPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


    //增加点击事件
    float x ,y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                //由于event.getX()代表的原点是该控件的左上角,而记录的文本坐标值的原点是在该控件的正中心
                x = event.getX() - getWidth() / 2;
                y = event.getY() - getHeight() / 2;
                //Log.e("tag","相对中心位置的坐标点:x="+x+",y="+y);
                break;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < texts.length; i++) {
                    XYPoint xyPoint = textXy.get(i);
                    if( xyPoint.x <x && x<(xyPoint.x+xyPoint.textWidth) && (xyPoint.y - xyPoint.textHeight)<y && y<xyPoint.y){
                        Toast.makeText(getContext(),"点击的:"+texts[i],Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                break;
        }
        return true;
    }
}