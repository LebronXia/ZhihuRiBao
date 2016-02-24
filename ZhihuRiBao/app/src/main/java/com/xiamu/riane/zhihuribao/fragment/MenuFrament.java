package com.xiamu.riane.zhihuribao.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.activity.MainActivity;
import com.xiamu.riane.zhihuribao.model.NewsListItem;
import com.xiamu.riane.zhihuribao.util.Constant;
import com.xiamu.riane.zhihuribao.util.HttpUtils;
import com.xiamu.riane.zhihuribao.util.PreUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Xiamu on 2015/11/13.
 */
public class MenuFrament extends BaseFragment implements View.OnClickListener{
    private ListView lv_item;
    private TextView tv_download, tv_main, tv_login,tv_backup;  //下载，首页，登录，
    private LinearLayout ll_menu; //头布局
    private List<NewsListItem> items;
    private android.os.Handler mHandler = new android.os.Handler();
    private NewsTypeAdapter mAdapter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
        tv_download = (TextView) view.findViewById(R.id.tv_download);
        tv_main = (TextView) view.findViewById(R.id.tv_main);
        tv_backup = (TextView) view.findViewById(R.id.tv_backup);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        lv_item = (ListView) view.findViewById(R.id.lv_item);
        tv_main.setOnClickListener(this);
        lv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_content,
                                new NewsFragment(items.get(position).getId(), items.get(position).getName())
                                , "News"
                        ).commit();
                ((MainActivity) mActivity).setCurId(items.get(position).getId());
                ((MainActivity) mActivity).closeMenu();
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        items = new ArrayList<NewsListItem>();
        if (HttpUtils.isNetworkConnected(mActivity)){
            HttpUtils.get(Constant.THEMES, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    // super.onSuccess(statusCode, headers, response);
                    String json = response.toString();
                    PreUtils.putStringToDefault(mActivity, Constant.THEMES, json);
                    parseJson(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
                //                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                   // super.onSuccess(statusCode, headers, response);
//                    String json = response.toString();
//                    PreUtils.putStringToDefault(mActivity, Constant.THEMES, json);
//                    parseJson(response);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    //super.onFailure(statusCode, headers, throwable, errorResponse);
//                }
            });
        } else {
            //离线时配置到配置中保存
            String json = PreUtils.getStringFromDefault(mActivity, Constant.THEMES, "");
            try {
                JSONObject jsonObject = new JSONObject(json);
                parseJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJson(JSONObject response){
        try {
            JSONArray itemsArray = response.getJSONArray("others");
            for (int i = 0; i < itemsArray.length(); i++){
                NewsListItem newsListItem = new NewsListItem();
                JSONObject itemObject = itemsArray.getJSONObject(i);

                newsListItem.setName(itemObject.getString("name"));
                newsListItem.setId(itemObject.getString("id"));
                items.add(newsListItem);
            }
            mAdapter = new NewsTypeAdapter();
            lv_item.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class NewsTypeAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if (convertView == null){
               convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_menu,parent,false);
           }
            TextView tv_item = (TextView) convertView.findViewById(R.id.tv_item);
            tv_item.setText(items.get(position).getName());
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_main:
                ((MainActivity) mActivity).loadLatest();
                ((MainActivity) mActivity).closeMenu();
                break;
        }
    }
}
