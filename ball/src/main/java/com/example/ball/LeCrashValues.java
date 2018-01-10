package com.example.ball;

/**
 * Created by wsq on 2018/1/5.
 */

public class LeCrashValues {

    public final static int fieldLeft = 0;
    public final static int fieldRight = 800;
    public final static int fieldTop = 0;
    public final static int fieldBottom = 1170;

    public final static int ballSize = 100;

    public final static int screenWidth = 800;
    public final static int screenHeight = 1170;

    public final static int sleepTimes = 1;

    public final static float velocity = 1f;
    public final static int degree = 360;

    public final static int diameter = 100;

    public static void sleep() {
        try {
            Thread.sleep(sleepTimes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
