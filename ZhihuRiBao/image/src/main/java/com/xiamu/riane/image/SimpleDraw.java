package com.xiamu.riane.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Xiamu on 2015/12/10.
 */
public class SimpleDraw extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsDrawing;  //子线程标志位
    private Path mPath;
    private Paint mPaint;

    public SimpleDraw(Context context) {
        super(context);
        initView();
    }
    
    public SimpleDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        //初始化SurfaceHolder
        mHolder = getHolder();
        mHolder.addCallback(this);
        //在键盘状态下也可以获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //请将屏幕一直保持为开启状态,以便校准
        this.setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(40);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //开始绘画时线程开启，起一个子线程进行绘画
        mIsDrawing = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while(mIsDrawing){
            draw();
        }
        long end = System.currentTimeMillis();
        //在绘画时间小于100s是，尽量不用那么频繁的调用draw，控制在50ms 到100ms左右
        if (end - start < 100){
            try {
                Thread.sleep(100 - (end-start));
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

    private void draw() {

        try {
            //得到当前的绘画对象,获得的还是上一次的Canvas对象,而不是一个新的对象
            mCanvas = mHolder.lockCanvas();
            //清屏操作
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath,mPaint);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
}
