package com.xiamu.riane.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Xiamu on 2015/12/10.
 */
public class RoundRectSharderView extends View {

    private BitmapShader mBitmapShader;
    private Bitmap mBitmap;
    private Paint mPaint;

    public RoundRectSharderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        mBitmap = BitmapFactory.decodeResource(getResources(),
//                R.mipmap.ic_launcher);
//
//        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT,
//                Shader.TileMode.REPEAT);

        mPaint = new Paint();
        mPaint.setShader(new LinearGradient(0,0,400,400,
                Color.BLUE,Color.YELLOW, Shader.TileMode.REPEAT));
        canvas.drawRect(0,0,400,400,mPaint);
    }
}
