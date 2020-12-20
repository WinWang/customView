package com.winwang.myapplication;

import androidx.annotation.FloatRange;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * 旋转木马效果的Transformer
 * Created by lishaojie on 2018/4/16.
 */

public class CarouselTransformer implements ViewPager.PageTransformer {
    public static final float SCALE_MIN = 0.7f;
    public static final float SCALE_MAX = 1f;

    private static final float Max_Trans = 1.0f;
    private static final float Min_Trans = 0.5f;


    public float scale;

    private float pagerMargin;
    private float spaceValue;

    public CarouselTransformer(@FloatRange(from = 0, to = 1) float scale, float pagerMargin, float spaceValue) {
        this.scale = scale;
        this.pagerMargin = pagerMargin;
        this.spaceValue = spaceValue;
    }

    @Override
    public void transformPage(View page, float position) {

        if (scale != 0f) {
            float realScale = getAdapter(1 - Math.abs(position * scale), SCALE_MIN, SCALE_MAX);
            float realAlpha = getAdapter(1 - Math.abs(position * scale), Min_Trans, Max_Trans);
            page.setAlpha(realAlpha);
            page.setScaleX(realScale);
            page.setScaleY(realScale);
        }

        System.out.println("position>>>>>>>>>"+position);

        if (position <= -2 || position >= 2) {
            page.setAlpha(0f);
        }


        if (pagerMargin != 0) {
            float realPagerMargin = position * (pagerMargin);
            page.setTranslationX(realPagerMargin);
        }
    }

    private float getAdapter(float value, float minValue, float maxValue) {
        return Math.min(maxValue, Math.max(minValue, value));
    }

}
