package com.winwang.myapplication;

import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by WinWang on 2019/8/13
 * Description->
 */
public class TimeAdpater extends BaseQuickAdapter<TimeBean, BaseViewHolder> {

    public TimeAdpater(int layoutResId, @Nullable List<TimeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeBean item) {
        TextView view = (TextView) helper.getView(R.id.tv_content);
        if (item.isHasExpand()) {
            view.setMaxLines(3);
        } else {
            view.setMaxLines(100);
        }
    }
}
