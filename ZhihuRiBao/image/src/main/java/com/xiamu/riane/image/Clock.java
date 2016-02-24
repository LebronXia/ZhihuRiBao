package com.xiamu.riane.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Xiamu on 2015/12/9.
 */
public class Clock extends View {
    private int mHeight, mWidth;

    public Clock(Context context) {
        super(context);
    }

    public Clock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取宽高参数
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //画外圆
        Paint paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(5);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, paintCircle);

        //画刻度
        Paint paintDegree = new Paint();
        paintDegree.setStrokeWidth(3);
        for (int i = 0; i < 24; i++){
            //区分整点与非整点
            if (i == 0 || i == 6 || i == 12 || i == 18){
                paintDegree.setStrokeWidth(5);
                paintDegree.setTextSize(30);
                canvas.drawLine(mWidth / 2, mHeight / 2 - mWidth / 2,
                        mWidth / 2, mHeight / 2 - mWidth / 2 + 60,
                        paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        mWidth / 2 - paintDegree.measureText(degree) / 2,
                        mHeight / 2 - mWidth/2 + 90,paintDegree);
            } else {
                paintDegree.setStrokeWidth(3);
                paintDegree.setTextSize(15);
                canvas.drawLine(mWidth / 2, mHeight/2 - mWidth / 2,
                        mWidth / 2, mHeight / 2 - mWidth / 2 + 30,
                        paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree,
                        mWidth / 2 - paintDegree.measureText(degree) / 2,
                        mHeight / 2 - mWidth / 2 + 60,
                        paintDegree);
            }
            // 通过旋转画布简化坐标运算
            canvas.rotate(15, mWidth / 2, mHeight / 2);
        }

        Paint paintPointer = new Paint();
        paintPointer.setStrokeWidth(30);
        canvas.drawPoint(mWidth / 2, mHeight / 2, paintPointer);

        Paint paintHour = new Paint();
        paintPointer.setStrokeWidth(20);
        Paint paintMinute = new Paint();
        paintMinute.setStrokeWidth(10);
        canvas.save();

        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawLine(0,0,100,100,paintHour);
        canvas.drawLine(0,0,100,200,paintMinute);
        canvas.restore();

    }
}
