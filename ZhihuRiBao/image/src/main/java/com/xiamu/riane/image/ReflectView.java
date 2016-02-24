package com.xiamu.riane.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Xiamu on 2015/12/10.
 */
public class ReflectView extends View {
    private Bitmap mSrcBitmap, mRefBitmap;
    private Paint mPaint;
    private PorterDuffXfermode mXfermode;

    public ReflectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRes(context);

    }

    private void initRes(Context context) {
        mSrcBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.test1);
        Matrix matrix = new Matrix();
        matrix.postScale(1F,-1F);
        mRefBitmap = Bitmap.createBitmap(mSrcBitmap,0,0,
                mSrcBitmap.getWidth(),mSrcBitmap.getHeight(), matrix,true);

        mPaint = new Paint();
        mPaint.setShader(new LinearGradient(0,mSrcBitmap.getHeight(),0,
                mSrcBitmap.getHeight() + mSrcBitmap.getHeight()
                        /4, 0XDD000000, 0X10000000, Shader.TileMode.CLAMP));
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mSrcBitmap,0,0,null);
        canvas.drawBitmap(mRefBitmap,0,mSrcBitmap.getHeight(),null);

        mPaint.setXfermode(mXfermode);

        canvas.drawRect(0,mSrcBitmap.getHeight(),
                mRefBitmap.getWidth(),mSrcBitmap.getHeight()*2,mPaint);
        mPaint.setXfermode(null);
    }
}
