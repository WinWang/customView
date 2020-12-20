package com.winwang.myapplication;

import android.content.Context;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Created by WinWang on 2019/8/29
 * Description->通过Scroller实现自动滑动
 */
public class AutoScrollButton extends AppCompatButton {

    private Scroller mScroller; //滚动器
    int direction = -1;
    private int mWidth;
    private int mHeight;


    public AutoScrollButton(Context context) {
        this(context, null);
    }

    public AutoScrollButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        int left = getLeft();
        System.out.println("》》》》》》》》》"+mWidth+">>>>"+left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.startScroll((int) getX(), (int) getY(), mWidth*direction, (int)getY(),5000);
                direction *= -1;
                invalidate();
                break;
        }

        return super.onTouchEvent(event);

    }

    @Override
    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) { //判断滚动是否完成   判断scroll是否完成
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            invalidate();
        }
    }
}
