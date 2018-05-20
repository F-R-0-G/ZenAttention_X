package com.frog.zenattention.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

public class AttentionTimeData {

    private static final String TAG = "AttentionTimeData";

    public static void storeTime(long time, Context context){
        Calendar c = Calendar.getInstance();
        String year = Integer.toString(c.get(Calendar.YEAR));
        String month = Integer.toString(c.get(Calendar.MONTH) + 1);
        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String calendarData = year + "//" + month + "//" + day;

        SharedPreferences timeData = context.getSharedPreferences("AttentionTimeData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = timeData.edit();
        Long getExist = timeData.getLong(calendarData, 0);
        if (getExist == 0){
            editor.putLong(calendarData, time);
        }
        else{
            editor.putLong(calendarData, time + getExist);
        }
        editor.apply();
    }

    public static long getTime(String calendar, Context context){
        SharedPreferences timeData = context.getSharedPreferences("AttentionTimeData", Context.MODE_PRIVATE);
        long getData = timeData.getLong(calendar, 0);
        Log.e(TAG, Long.toString(getData));
        return getData;
    }

    public static void clearData(Context context){
        SharedPreferences timeData = context.getSharedPreferences("AttentionTimeData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = timeData.edit();
        editor.clear();
        editor.apply();
    }
}
