package com.winwang.myapplication;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by WinWang on 2019/8/14
 * Description->
 */
public class PagerAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {
    public PagerAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {


    }
}
