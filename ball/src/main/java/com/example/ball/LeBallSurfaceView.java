package com.example.ball;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by wsq on 2018/1/5.
 */

public class LeBallSurfaceView  extends SurfaceView implements SurfaceHolder.Callback  {

    private LeDrawThread drawThread;
    private LeBallThread ballThread;

    private Bitmap bg;
    private Bitmap[] bitmaps;

    private LeBall moveBall;
    private int mapCount = 0;
    private int ballCount = 0;
    private int currentCount = 0;

    public static List<LeBall> ballList = new Vector();

    public LeBallSurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        getHolder().addCallback(this);
        initBitmaps(getResources());
        addMoveBall(20, 20);
        drawThread = new LeDrawThread(this, getHolder());
        ballThread = new LeBallThread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (!drawThread.isAlive()) {
            drawThread.start();
        }
        if (!ballThread.isAlive()) {
            ballThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        ballList.clear();
        ballList = null;
        drawThread.setRunning(false);
        drawThread = null;
        ballThread.setRunning(false);
        ballThread = null;
    }

    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(bg, 0, 0, null);
        if (null == ballList)
            return;
        for (LeBall mb : ballList) {
            mb.draw(canvas);
        }
    }

    private void initBitmaps(Resources r) {
        // TODO Auto-generated method stub
        bg = BitmapFactory.decodeResource(r, R.drawable.image_3);
        bitmaps = new Bitmap[] {
                BitmapFactory.decodeResource(r, R.drawable.image_1),
                BitmapFactory.decodeResource(r, R.drawable.image_2)};
        mapCount = bitmaps.length;
    }

    public void addMoveBall(float x, float y) {
        if (null == ballList)
            ballList = new Vector();
        moveBall = new LeBall(x, y, LeCrashValues.ballSize / 2, ballCount, bitmaps[new Random().nextInt(mapCount)]);
        ballList.add(moveBall);
        ballCount ++;
        currentCount ++;
        Log.v(this.getClass().getName(), "current ball count="+currentCount);
    }

    public void removeMoveBall(LeBall mb) {
        ballList.remove(mb);
        mb = null;
        currentCount --;
        Log.v(this.getClass().getName(), "current ball count="+currentCount);
    }
}
