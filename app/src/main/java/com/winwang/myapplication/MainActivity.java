package com.winwang.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener, BlankFragment1.OnFragmentInteractionListener, BlankFragment2.OnFragmentInteractionListener {


    private List<Fragment> mFragmentList = new ArrayList<>();
    private MyViewpager mVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivitys.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mVp = findViewById(R.id.view_pager);

        mFragmentList.add(new BlankFragment());
        mFragmentList.add(new BlankFragment1());
        mFragmentList.add(new BlankFragment2());
        mFragmentList.add(new BlankFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mVp.setAdapter(adapter);

        mVp.setPageTransformer(true, new ZoomOutPageTransformer());//设置切换动画
        mVp.setOffscreenPageLimit(2);
        mVp.setPageMargin(-300);//改小间隔

        initMagicIndicator1();
    }


    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator1);
        CircleNavigator circleNavigator = new CircleNavigator(this);
        circleNavigator.setCircleCount(mFragmentList.size());
        circleNavigator.setCircleColor(Color.RED);
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mVp.setCurrentItem(index);
            }
        });

        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, mVp);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


    //切换动画
    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MAX_SCALE = 1.0f;
        private static final float MIN_SCALE = 0.8f;
        private static final float Max_Trans = 1.0f;
        private static final float Min_Trans = 0.3f;

        @Override
        public void transformPage(View view, float position) {
            if (position < -1) {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
                view.setAlpha(Min_Trans);
            } else if (position <= 1) {

                float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);
                float scaleFactorAL = Min_Trans + (1 - Math.abs(position)) * (Max_Trans - Min_Trans);
                view.setScaleX(scaleFactor);
                view.setAlpha(scaleFactorAL);
                if (position > 0) {
                    view.setTranslationX(-scaleFactor * 2);
                } else if (position < 0) {
                    view.setTranslationX(scaleFactor * 2);
                }
                view.setScaleY(scaleFactor);
            } else {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
                view.setAlpha(Min_Trans);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
