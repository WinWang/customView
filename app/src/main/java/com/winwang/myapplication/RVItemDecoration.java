package com.winwang.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class RVItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    Context context;

    public RVItemDecoration(Context context) {
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2); //时间轴线的宽度。
        mPaint.setColor(Color.parseColor("#FFC5BAAA")); //时间轴线的颜色。
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);

            //int index = parent.getChildAdapterPosition(view);
            float left = 10;//AutoSizeUtils.pt2px(context, 42);
            float bottom = view.getBottom();
//            if(i==0){ //第一个布局特殊处理
//                c.drawLine(left, AutoSizeUtils.pt2px(context, (50 ) / 2), left, bottom, mPaint);
//            }else {
//            c.drawLine(left, AutoSizeUtils.pt2px(context, 15), left, bottom, mPaint);
//            }
//            c.drawLine(left, 0, left, bottom, mPaint);
        }
    }
}
