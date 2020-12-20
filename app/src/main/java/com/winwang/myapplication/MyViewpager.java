package com.winwang.myapplication;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

/**
 * Created by WinWang on 2019/7/12
 * Description->
 */
public class MyViewpager extends ViewPager {
    public MyViewpager(@NonNull Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public MyViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }



    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int currentItem = getCurrentItem();
        if (i >= currentItem) {
            return childCount - 1 - i + currentItem;
        }
        return super.getChildDrawingOrder(childCount, i);


    }
}
