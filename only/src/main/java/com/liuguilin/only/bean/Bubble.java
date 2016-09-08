package com.liuguilin.only.bean;

/*
 *  项目名：  Only 
 *  包名：    com.liuguilin.only.bean
 *  文件名:   Bubble
 *  创建者:   LGL
 *  创建时间:  2016/7/6 16:57
 *  描述：    海洋
 */
public class Bubble {

    //气泡半径
    private float radius;
    //上升速度
    private float speedY;
    //平移速度
    private float speedX;
    //气泡x坐标
    private float x;
    // 气泡y坐标
    private float y;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

}
