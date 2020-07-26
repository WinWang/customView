package com.winwang.myapplication.bean;

/**
 * Created by WinWang on 2020/7/24
 * Description->
 */
public class LineChartBean {
    private float value;
    private int minute;
    private float xPosition;


    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
