package com.winwang.myapplication;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by WinWang on 2019/8/13
 * Description->
 */
public class TimeBean implements MultiItemEntity {
    private boolean hasExpand;
    private String time;
    private String header;
    private int leftPostion = -1;

    public int getLeftPostion() {
        return leftPostion;
    }

    public void setLeftPostion(int leftPostion) {
        this.leftPostion = leftPostion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isHasExpand() {
        return hasExpand;
    }

    public void setHasExpand(boolean hasExpand) {
        this.hasExpand = hasExpand;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
