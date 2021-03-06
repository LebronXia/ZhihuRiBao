package com.xiamu.riane.zhihuribao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.activity.MainActivity;
import com.xiamu.riane.zhihuribao.model.StoriesEntity;
import com.xiamu.riane.zhihuribao.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiamu on 2015/11/25.
 */
public class MainNewsItemAdapter extends BaseAdapter {
    private List<StoriesEntity> entities;
    private Context context;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private boolean isLight;
    public MainNewsItemAdapter(Context context) {
        this.context = context;
        this.entities = new ArrayList<>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        isLight = ((MainActivity)context).isLight();
    }

    //添加items
    public void addList(List<StoriesEntity> items){
        this.entities.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.main_news_item, parent, false);
            viewHolder.tv_topic = (TextView) convertView.findViewById(R.id.tv_topic);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //夜间模式字体调整
        //viewHolder.tv_topic.setTextColor();
        StoriesEntity entity = entities.get(position);
        if (entity.getType() == Constant.TOPIC) {
            ((FrameLayout) viewHolder.tv_topic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            viewHolder.tv_title.setVisibility(View.GONE);
            viewHolder.iv_title.setVisibility(View.GONE);
            viewHolder.tv_topic.setVisibility(View.VISIBLE);
            viewHolder.tv_topic.setText(entity.getTitle());
        } else {
            //((FrameLayout) viewHolder.tv_topic.getParent()).setBackgroundResource(isLight ? R.drawable.item_background_selector_light : R.drawable.item_background_selector_dark);
            viewHolder.tv_topic.setVisibility(View.GONE);
            viewHolder.tv_title.setVisibility(View.VISIBLE);
            viewHolder.iv_title.setVisibility(View.VISIBLE);
            viewHolder.tv_title.setText(entity.getTitle());
            mImageLoader.displayImage(entity.getImages().get(0), viewHolder.iv_title, mOptions);
        }
        return convertView;
    }

    private static class ViewHolder{
        TextView tv_topic;
        TextView tv_title;
        ImageView iv_title;
    }
}
