package com.xiamu.riane.zhihuribao.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Xiamu on 2015/12/6.
 */
public class MytextView extends TextView {

    private Paint mPaint;
    private Matrix mGradientMatix;
    private int mViewWidth = 0;
    private int mTranslate = 0;
    private LinearGradient mLinearGradient;


    public MytextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatix != null){
            mTranslate += mViewWidth/5;
            if (mTranslate > 2*mViewWidth){
                mTranslate = -mViewWidth;
            }
            mGradientMatix.setTranslate(mTranslate,0);
            mLinearGradient.setLocalMatrix(mGradientMatix);
            postInvalidateDelayed(100);

        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0){
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0){
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(0,0,mViewWidth,0,
                        new int[]{
                                Color.BLUE,0xffffffff,Color.BLUE},null,Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatix = new Matrix();
            }

        }
    }
}
