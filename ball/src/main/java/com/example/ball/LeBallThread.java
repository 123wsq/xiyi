package com.example.ball;

import android.util.Log;

/**
 * Created by wsq on 2018/1/5.
 */

public class LeBallThread extends Thread{

    private boolean running = true;

    public LeBallThread() {
        super.setName("##-LeBall-");
    }

    public void run() {
        while (running) {
            moveBall();
            LeCrashValues.sleep();
        }
    }

    private void moveBall() {
        try {
            for (LeBall mb : LeBallSurfaceView.ballList) {
                moveBall(mb);
                checkCrash(mb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveBall(LeBall mb){
        mb.setxAxis(mb.getxAxis() + (float)(LeCrashValues.velocity * LeTrigonometricFunction.cos(mb.getAngle())));
        mb.setyAxis(mb.getyAxis() + (float)(LeCrashValues.velocity * LeTrigonometricFunction.sin(mb.getAngle())));
    }

    private void checkCrash(LeBall mb) {
        if (checkForEdge(mb))
            return;
        checkForCrash(mb);
    }

    private boolean checkForEdge(LeBall mb) {
        if (mb.getxAxis() <= LeCrashValues.fieldLeft) {
            if (mb.getAngle() > 90 && mb.getAngle() <= 180) {
                mb.setAngle(180 - mb.getAngle());
            } else if (mb.getAngle() > 180 && mb.getAngle() < 270) {
                mb.setAngle(540 - mb.getAngle());
            }
            return true;
        } else if (mb.getxAxis() >= LeCrashValues.fieldRight - LeCrashValues.ballSize) {
            if (mb.getAngle() >= 0 && mb.getAngle() < 90) {
                mb.setAngle(180 - mb.getAngle());
            } else if (mb.getAngle() > 270 && mb.getAngle() < 360) {
                mb.setAngle(540 - mb.getAngle());
            }
            return true;
        } else if (mb.getyAxis() <= LeCrashValues.fieldTop) {
            if (mb.getAngle() > 180 && mb.getAngle() < 360) {
                mb.setAngle(360 - mb.getAngle());
            }
            return true;
        } else if (mb.getyAxis() >= LeCrashValues.fieldBottom - LeCrashValues.ballSize) {
            if (mb.getAngle() > 0 && mb.getAngle() < 180) {
                mb.setAngle(360 - mb.getAngle());
            }
            return true;
        }
        return false;
    }

    private void checkForCrash(LeBall moveBall) {
        if (null == LeBallSurfaceView.ballList)
            return;
        try {
            for (LeBall mb : LeBallSurfaceView.ballList) {
                if (mb.getOrder() > moveBall.getOrder()) {
                    if (isCrash(moveBall.getxAxis() - mb.getxAxis(), moveBall.getyAxis() - mb.getyAxis(), LeCrashValues.diameter)) {
                        handleCrash(moveBall, mb);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCrash(LeBall b1, LeBall b2) {
        Log.v(this.getClass().getName(), this.getName() + b1.getOrder() + "--------angle="+b1.getAngle());
        int angle = b2.getAngle();
        b2.setAngle(b1.getAngle());
        b1.setAngle(angle);

        moveBall(b1);

        while (!isSeperated(b1, b2)) {
            moveBall(b1);
        }

    }

    private boolean isSeperated(LeBall b1, LeBall b2) {
        if (isCrash(b1.getxAxis() - b2.getxAxis(), b1.getyAxis() - b2.getyAxis(), LeCrashValues.diameter))
            return false;
        return true;
    }

    private boolean isCrash(float x, float y, float d) {
        if (pow(x) + pow(y) <= pow(d))
            return true;
        return false;
    }

    private float pow(float x) {
        return x * x;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
