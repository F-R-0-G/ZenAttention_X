package com.frog.zenattention.app;

import android.app.Application;
import android.content.Context;

import com.yatoooon.screenadaptation.ScreenAdapterTools;

public class BasicApp extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenAdapterTools.init(this);
        context = getApplicationContext();
    }

}
