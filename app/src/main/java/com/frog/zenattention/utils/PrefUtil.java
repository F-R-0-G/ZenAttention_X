package com.frog.zenattention.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.frog.zenattention.app.BasicApp;

/**
 * Modified by Wen Sun
 */
public class PrefUtil {

    private static final String PRE_NAME = "com.frog.zenattention";

    private static SharedPreferences getSharedPreferences() {
        return BasicApp.getContext()
                .getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存选择音乐
     */
    public static void saveSelectedMusic(String content){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("SelectedMusic",content);
        editor.apply();
    }

    /**
     * 获取选择音乐
     */
    public static String getSelectedMusic(){
        return getSharedPreferences().getString("SelectedMusic",null);
    }

    /**
     * 保存自定义音乐路径
     */
    public static void saveCustomMusicPath(String path){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("CustomMusicPath",path);
        editor.apply();
    }

    /**
     * 获取自定义音乐路径
     */
    public static String getCustomMusicPath(){
        return getSharedPreferences().getString("CustomMusicPath",null);
    }

    /**
     * 删除某个 key
     */
    public static void removeKey(String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.apply();
    }
}
