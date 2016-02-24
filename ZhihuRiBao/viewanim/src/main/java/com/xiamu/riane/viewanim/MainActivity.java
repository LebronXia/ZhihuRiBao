package com.xiamu.riane.viewanim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnAlpha(View view){
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(1000);
        view.startAnimation(aa);
    }

    public void btnRotate(View view){
        RotateAnimation ra = new RotateAnimation(0,360,
                RotateAnimation.RELATIVE_TO_SELF,0.5F,
                RotateAnimation.RELATIVE_TO_SELF,0.5F);
        ra.setDuration(1000);
        view.startAnimation(ra);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
