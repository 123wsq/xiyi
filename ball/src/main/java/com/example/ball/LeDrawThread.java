package com.example.ball;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by wsq on 2018/1/5.
 */

public class LeDrawThread extends Thread{

    private boolean running = true;
    private LeBallSurfaceView view;
    private SurfaceHolder holder;

    public LeDrawThread(LeBallSurfaceView view, SurfaceHolder holder) {
        // TODO Auto-generated constructor stub
        this.view = view;
        this.holder = holder;
        this.running = true;
    }

    public void run() {
        Canvas canvas = null;
        while (running) {
            try {
                canvas = holder.lockCanvas(null);
                synchronized (holder) {
                    view.doDraw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            LeCrashValues.sleep();
        }
    }


    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
