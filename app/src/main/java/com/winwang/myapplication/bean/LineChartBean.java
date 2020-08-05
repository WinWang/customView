package com.winwang.myapplication.bean;

/**
 * Created by WinWang on 2020/7/24
 * Description->
 */
public class LineChartBean {
    private float value;
    private int minute;
    private float xPosition;
    private float yPosition;
    private int type; //数据类型
    private String color;
    private String time = "6-10";


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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
