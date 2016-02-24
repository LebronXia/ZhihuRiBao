package com.xiamu.riane.zhihuribao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.model.StoriesEntity;

import java.util.List;

/**
 * Created by Xiamu on 2015/11/24.
 */
public class NewsItemAdapter extends BaseAdapter{
    private List<StoriesEntity> entities;
    private Context context;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private boolean isLight;

    public NewsItemAdapter(Context context, List<StoriesEntity> items) {

        this.context = context;
        entities = items;
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

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
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item,parent, false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StoriesEntity entity = entities.get(position);
        viewHolder.tv_title.setText(entity.getTitle());
        if (entity.getImages() != null){
            viewHolder.iv_title.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(entity.getImages().get(0), viewHolder.iv_title, options);

        } else {
            viewHolder.iv_title.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static class ViewHolder{
        ImageView iv_title;
        TextView tv_title;
    }


}
