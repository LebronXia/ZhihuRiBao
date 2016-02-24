package com.xiamu.riane.dragviewtest;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Xiamu on 2015/12/9.
 */
public class DragViewGroup extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mMenuView, mMainView;
    private int mWidth;

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    //加载完布局文件后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMenuView.getMeasuredWidth();  //获得View的g宽度处理滑动后的效果
    }

    //重写拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    //处理事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this,callback);

    }

    private ViewDragHelper.Callback callback =
            new ViewDragHelper.Callback() {
                //何时开始检测触摸事件
                @Override
                public boolean tryCaptureView(View child, int pointerId) {
                    return mMainView == child;
                }

                @Override
                public int clampViewPositionVertical(View child, int top, int dy) {
                    return 0;
                }

                @Override
                public int clampViewPositionHorizontal(View child, int left, int dx) {
                    return left;
                }

                //拖动结束时回调
                @Override
                public void onViewReleased(View releasedChild, float xvel, float yvel) {
                    super.onViewReleased(releasedChild, xvel, yvel);

                    if (mMainView.getLeft() < 500){
                        //关闭菜单
                        mViewDragHelper.smoothSlideViewTo(mMainView,0,0);
                        ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
                    } else {
                        //打开菜单
                        mViewDragHelper.smoothSlideViewTo(mMainView,300,0);
                        ViewCompat.postInvalidateOnAnimation(DragViewGroup.this);
                    }
                }
            };

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true));
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
