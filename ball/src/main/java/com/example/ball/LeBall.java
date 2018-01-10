package com.example.ball;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by wsq on 2018/1/5.
 */

public class LeBall {

    private float xAxis;
    private float yAxis ;
    private float radius;
    private int angle;
    private int order;

    private Bitmap bitmap;

    public LeBall(float x, float y, float r, int o, Bitmap bitmap) {
        // TODO Auto-generated constructor stub
        this.xAxis = x;
        this.yAxis = y;
        this.radius = r;
        this.order = o;
        this.bitmap = bitmap;
        this.angle = new Random().nextInt(LeCrashValues.degree);
    }

    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.drawBitmap(this.bitmap, xAxis, yAxis, null);
    }


    public float getxAxis() {
        return xAxis;
    }


    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }


    public float getyAxis() {
        return yAxis;
    }


    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }


    public float getRadius() {
        return radius;
    }


    public void setRadius(float radius) {
        this.radius = radius;
    }


    public int getAngle() {
        return angle;
    }


    public void setAngle(int angle) {
        this.angle = angle;
    }


    public int getOrder() {
        return order;
    }


    public void setOrder(int order) {
        this.order = order;
    }
}
