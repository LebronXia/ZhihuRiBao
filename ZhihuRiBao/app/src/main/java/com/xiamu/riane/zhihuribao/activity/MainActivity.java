package com.xiamu.riane.zhihuribao.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.xiamu.riane.zhihuribao.R;
import com.xiamu.riane.zhihuribao.db.CacheDbHelper;
import com.xiamu.riane.zhihuribao.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fl_content;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
   // private SwipeRefreshLayout sr;
    private long firstTime;
    private String curId;
    private boolean isLight;
    private CacheDbHelper dbHelper;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new CacheDbHelper(this,1);
        isLight = sp.getBoolean("isLight", true);
        initView();
        loadLatest();
    }

    public void loadLatest() {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                replace(R.id.fl_content, new MainFragment(), "latest")
                .commit();

        curId = "latest";
    }

    public void setCurId(String id){
        curId = id;
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        //setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));

//        sr = (SwipeRefreshLayout) findViewById(R.id.sr);
//        //通过颜色资源文件设置进度动画的颜色资源
//        sr.setColorSchemeColors(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light
//        );
//
//        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                replaceFragment();
//                sr.setRefreshing(false);
//            }
//        });

        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        //ActionBarDrawerToggle是一个开关，用于打开/关闭DrawerLayout抽屉
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar,R.string.app_name,R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        //该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        drawerToggle.syncState();
    }

    public void closeMenu(){
        mDrawerLayout.closeDrawers();
    }

    public void setToolbarTitle(String text){
        toolbar.setTitle(text);
    }

    private void replaceFragment() {
        if (curId.equals("latest")){
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                    .replace(R.id.fl_content,
                            new MainFragment(), "latest").commit();
        }
    }

//    public void setSwipeRefreshEnable(boolean enable){
//        sr.setEnabled(enable);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle(sp.getBoolean("isLight", true) ? "夜间模式" : "日间模式");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mode) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean isLight(){
        return isLight;
    }

    public CacheDbHelper getCacheDbHelper(){
        return dbHelper;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            closeMenu();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000){
                Snackbar sb = Snackbar.make(fl_content, "再按一次退出", Snackbar.LENGTH_LONG );
                sb.getView().setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                sb.show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }
    }
}
