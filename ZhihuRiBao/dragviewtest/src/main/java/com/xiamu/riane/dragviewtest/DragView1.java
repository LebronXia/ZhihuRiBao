package com.xiamu.riane.dragviewtest;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Xiamu on 2015/12/8.
 */
public class DragView1 extends View{

    private int lastX;
    private int lastY;

    public DragView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    private void initView() {
        setBackgroundColor(Color.BLUE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawx = (int) event.getRawX();
        int rawy = (int) event.getRawY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = rawx;
                lastY = rawy;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = rawx - lastX;
                int offsetY = rawy - lastY;

                ((View) getParent()).scrollTo(-lastX+50, -lastY+50);
                lastX = rawx;
                lastY = rawy;
                break;
        }
        return true;
    }
}
