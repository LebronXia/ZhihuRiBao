package com.xiamu.riane.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by Xiamu on 2015/12/9.
 */
public class ColorMatrix extends Activity {

    private ImageView mImageView;
    private GridLayout mGroup;
    private Bitmap bitmap;
    private int mEtWidth, mEtHeight;
    private EditText[] mEts = new EditText[20];
    private float[] mColorMatrix = new float[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bitmap = BitmapFactory.decodeResource(getResources(),
        R.mipmap.test1);
        mImageView = (ImageView) findViewById(R.id.imageview);
        mGroup = (GridLayout) findViewById(R.id.group);
        mImageView.setImageBitmap(bitmap);

        //无法在onCreat()进程中获得视图的宽高值，所以通过View的post方法，在试图创建完毕后获得其宽高值
        mGroup.post(new Runnable() {
            @Override
            public void run() {
                mEtWidth = mGroup.getWidth() / 5;
                mEtHeight = mGroup.getHeight() / 4;
                addEts();
                initMatrix();

            }


        });

    }

    //获取矩阵值
    private void getMatrix(){
        for (int i = 0; i < 20; i++){
            mColorMatrix[i] = Float.valueOf(mEts[i].getText().toString());
        }
    }

    //将矩阵值设置到图像
    private void setImageMatrix(){
        //Android系统不允许直接修改原图，类似PhotoShop中的锁定.必须通过原图创建一个同样大小的Bitmap
        Bitmap bmp = Bitmap.createBitmap(
          bitmap.getWidth(),
          bitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        android.graphics.ColorMatrix colorMatrix =
                new android.graphics.ColorMatrix();
        colorMatrix.set(mColorMatrix);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        mImageView.setImageBitmap(bmp);
    }

    private void addEts() {
        for (int i = 0; i < 20; i++){
            EditText editText = new EditText(ColorMatrix.this);
            mEts[i] = editText;
            mGroup.addView(editText,mEtWidth,mEtHeight);
        }
    }

    private void initMatrix(){
        for (int i = 0; i < 20; i++){
            if (i % 6 == 0){
                mEts[i].setText(String.valueOf(1));
            } else {
                mEts[i].setText(String.valueOf(0));
            }
        }
    }

    public void btnChange(View view){
        getMatrix();
        setImageMatrix();
    }

    public void btnReset(View view){
        initMatrix();
        getMatrix();
        setImageMatrix();
    }
}
