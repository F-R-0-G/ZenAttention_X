package com.frog.zenattention;

import android.app.Activity;
import android.os.Bundle;

import com.frog.zenattention.colorUi.util.SharedPreferencesMgr;
import com.frog.zenattention.utils.ActivityCollector;

public class BasicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        if(SharedPreferencesMgr.getInt("theme", 0) == 0) {
            setTheme(R.style.theme_1);
        } else if(SharedPreferencesMgr.getInt("theme", 0) == 1){
            setTheme(R.style.theme_2);
        } else {
            setTheme(R.style.theme_3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
