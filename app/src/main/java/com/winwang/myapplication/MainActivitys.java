package com.winwang.myapplication;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivitys extends AppCompatActivity {

    private final int IMAGE_COUNT = 8;
    private ViewPager mViewCard;
    private ArrayList<Integer> urls = new ArrayList<>(IMAGE_COUNT);
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        initView();
    }

    private void initView() {
        frameLayout = findViewById(R.id.fr_root);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewCard.dispatchTouchEvent(event);
            }
        });
        mViewCard = findViewById(R.id.vp);
        mViewCard.setAdapter(new ViewPagerCardAdapter());

        initData();
        mViewCard.setOffscreenPageLimit(IMAGE_COUNT);
        mViewCard.setPageTransformer(false, new CarouselTransformer(0.2f, -300, 0));
    }


    private void initData() {
        urls.add(R.drawable.pic1);
        urls.add(R.drawable.pic2);
        urls.add(R.drawable.pic3);
        urls.add(R.drawable.pic4);

        urls.add(R.drawable.pic1);
        urls.add(R.drawable.pic2);
        urls.add(R.drawable.pic3);
        urls.add(R.drawable.pic4);
    }

    private class ViewPagerCardAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return IMAGE_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(MainActivitys.this).inflate(R.layout.item_view_card, null);
            ImageView card = view.findViewById(R.id.iv);
            card.setImageResource(urls.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }
    }
}
