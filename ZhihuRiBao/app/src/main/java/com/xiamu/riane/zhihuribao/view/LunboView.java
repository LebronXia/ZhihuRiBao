package com.xiamu.riane.zhihuribao.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.model.Latest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiamu on 2015/11/14.
 */
public class LunboView extends FrameLayout implements View.OnClickListener{
    private List<Latest.TopStoriesEntity> mTopStoriesEntities;
    private ImageLoader mImageLoader;    //图片加载框架
    private List<View> views;          //图片的合集
    private ViewPager vp;              //滑动图片
    private LinearLayout ll_dot;             //小店
    private List<ImageView> iv_dots;           //小点的集合
    private DisplayImageOptions options;
    private Context context;
    private boolean isAutoPlay;   //自动滑动
    private int currentItem;
    private int delayTime;
    private OnItemClickListener mItemClickListener;
    private android.os.Handler mHandler = new android.os.Handler();
    public LunboView(Context context) {
        this(context, null);
    }

    public LunboView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //一般自定义view里都在构造函数初始化
    public LunboView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //只能获取单例
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        this.mTopStoriesEntities = new ArrayList<>();
        initView();
    }

    private void initView() {
        views = new ArrayList<View>();
        iv_dots = new ArrayList<ImageView>();
    }

    //设置自定义view的时候重置UI
    public void setTopStoriesEntities(List<Latest.TopStoriesEntity> topEntities){
        this.mTopStoriesEntities = topEntities;
        reset();
    }

    private void reset() {
            views.clear();
        initUI();

    }

    private void initUI() {
        View view = LayoutInflater.from(context).inflate(R.layout.lunbo_layout,this,true);
        vp = (ViewPager) view.findViewById(R.id.vp);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        ll_dot.removeAllViews();

        int length = mTopStoriesEntities.size();
        for (int i = 0; i < length; i++){
            ImageView iv_dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            ll_dot.addView(iv_dot,params);
            iv_dots.add(iv_dot);
        }

        for (int i = 0; i <= length + 1; i++){
            View fm = LayoutInflater.from(context).inflate(R.layout.lunbo_content_layout,null);
            ImageView iv = (ImageView) fm.findViewById(R.id.iv_title);
            TextView tv_title = (TextView) fm.findViewById(R.id.tv_title);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //达到循环效果view要比length多两个
            if (i == 0){
                mImageLoader.displayImage(mTopStoriesEntities.get(length - 1).getImage(),iv,options);
                tv_title.setText(mTopStoriesEntities.get(length - 1).getTitle());
            } else if (i == length + 1){
                mImageLoader.displayImage(mTopStoriesEntities.get(0).getImage(),iv,options);
                tv_title.setText(mTopStoriesEntities.get(0).getTitle());
            } else {
                mImageLoader.displayImage(mTopStoriesEntities.get(i- 1).getImage(),iv,options);
                tv_title.setText(mTopStoriesEntities.get(i - 1).getTitle());
            }
            fm.setOnClickListener(this);
            views.add(fm);
        }
        vp.setFocusable(true);
        vp.setCurrentItem(1);
        currentItem = 1;
        vp.setAdapter(new MyPagetAdapter());
        vp.addOnPageChangeListener(new MyOnPageChangeListener());
        startPlay();
    }

    private void startPlay() {
        isAutoPlay = true;
        mHandler.postDelayed(task, 3000);

    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay){
                currentItem = currentItem % (mTopStoriesEntities.size() + 1) + 1;
                if (currentItem == 1){
                    vp.setCurrentItem(currentItem, false);
                } else {
                    vp.setCurrentItem(currentItem);
                    mHandler.postDelayed(task, 5000);
                }
            } else {
                mHandler.postDelayed(task,5000);
            }
        }
    };

    class MyPagetAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return views.size();
        }

        //判断view是否跟key对象相关联
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //实例化条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //指的是当前选择那页
        @Override
        public void onPageSelected(int position) {

            for (int i = 0; i < iv_dots.size(); i++){
                if (i == position - 1){
                    iv_dots.get(i).setImageResource(R.mipmap.dot_focus);
                } else {
                    iv_dots.get(i).setImageResource(R.mipmap.dot_blur);
                }

            }
        }

        //当前的滑动状态   0:没有任何动作  1：正在滑动  2：滑动停止
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state){
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    //setCurrentItem(0)表示不被选中
                    if (vp.getCurrentItem() == 0){
                        vp.setCurrentItem(mTopStoriesEntities.size(),false);
                    } else if (vp.getCurrentItem() == mTopStoriesEntities.size() + 1){
                        vp.setCurrentItem(1,false);
                    }
                    currentItem = vp.getCurrentItem();
                    isAutoPlay = true;
                    break;
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener{
        public void click(View v, Latest.TopStoriesEntity entity);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null){
            Latest.TopStoriesEntity entity = mTopStoriesEntities.get(vp.getCurrentItem() - 1);
            mItemClickListener.click(v, entity);
        }
    }


}
