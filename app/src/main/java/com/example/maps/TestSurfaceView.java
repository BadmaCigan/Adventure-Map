package com.example.maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void run() {
        int color = 0;
        while(true){
            Canvas canvas = getHolder().lockCanvas();
            if(canvas!= null){
                if(color==0){
                    canvas.drawColor(Color.GREEN);
                }else if(color==1){
                    canvas.drawColor(Color.YELLOW);
                }else if(color==2){
                    canvas.drawColor(Color.RED);
                }
                color = (color+1)%3;


                getHolder().unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }

            }
        }
    }
}