package com.xiamu.riane.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Xiamu on 2015/12/10.
 */
public class GuaGuaKa extends View {
    //绘制线条的Paint
    private Paint mOutterPaint = new Paint();
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Bitmap mBackBitmap;


    private boolean isComplete;
    //奖状的一些变量
    private Paint mBackPaint = new Paint();
    private Rect mTextBound = new Rect();
    private String mText = "¥500,000";

    private int mLastX;
    private int mLastY;
    public GuaGuaKa(Context context) {
        super(context);
        init();
    }

    public GuaGuaKa(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuaGuaKa(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        setUpOutPaint();
        setUpBackPaint();
    }

    private void setUpBackPaint() {
        //初始化canvas的绘制用的画笔
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setTextScaleX(2f);
        mBackPaint.setColor(Color.DKGRAY);
        mBackPaint.setTextSize(32);
        //计算文字所在矩形，可以得到宽高
        mBackPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }


    private void setUpOutPaint() {
            mOutterPaint.setColor(Color.parseColor("#c0c0c0"));
            mOutterPaint.setAntiAlias(true);
            mOutterPaint.setDither(true);
            mOutterPaint.setStyle(Paint.Style.STROKE);
            mOutterPaint.setStrokeJoin(Paint.Join.ROUND);
            mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
            mOutterPaint.setStrokeWidth(20);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mBackBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.test1);
        //初始化bitmap
        mBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        setUpOutPaint();
        mCanvas.drawColor(Color.parseColor("#c0c0c0"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawBitmap(mBackBitmap, 0,0,null);
        //绘制奖项
        canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2,
                getHeight() / 2 + mTextBound.height() / 2, mBackPaint);
        if (!isComplete){
            drawPath();
            canvas.drawBitmap(mBitmap,0,0,null);
        }

    }

    private void drawPath(){
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case  MotionEvent.ACTION_MOVE:

                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 3 || dy > 3){
                    mPath.lineTo(x,y);
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                new Thread(mRunnable).start();
                break;
        }
        invalidate();
        return true;
    }

    //统计擦除区域的任务
    private Runnable mRunnable = new Runnable() {
        private int[] mPixels;
        @Override
        public void run() {
            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;   //擦除的面积
            float totalArea = w * h;   //整个的面积

            Bitmap bitmap = mBitmap;
            mPixels = new int[w * h];

            //拿到所有像素信息
            bitmap.getPixels(mPixels, 0, w, 0 , 0 , w, h);

            /*
            * 遍历统计擦除的区域
            * */

            for (int i = 0; i < w; i++){
                for (int j = 0; j < h; j++){
                    int index = i + j * w;
                    if (mPixels[index] == 0){
                        wipeArea ++;
                    }
                }
            }

            //根据所占的百分比,推测
            if (wipeArea > 0 && totalArea > 0){
                int percent = (int) (wipeArea * 100 / totalArea);
                if (percent > 70){
                    isComplete = true;
                    postInvalidate();
                }
            }
        }
    };
}
