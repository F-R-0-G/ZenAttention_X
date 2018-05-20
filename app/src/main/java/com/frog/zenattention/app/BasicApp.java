package com.frog.zenattention.app;

import android.app.Application;
import android.content.Context;

import com.frog.zenattention.colorUi.util.SharedPreferencesMgr;

import org.litepal.LitePal;

public class BasicApp extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context = getApplicationContext();
        SharedPreferencesMgr.init(this, "frog");
    }

}
