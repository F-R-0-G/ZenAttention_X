package com.frog.zenattention.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
        Long getExistDay = timeData.getLong(calendarData, 0);
        Long getExistMonth = timeData.getLong(month, 0);
        if (getExistDay == 0){
            editor.putLong(calendarData, time);
        }
        else{
            editor.putLong(calendarData, time + getExistDay);
        }
        if (getExistMonth == 0){
            editor.putLong(month, time);
        }
        else {
            editor.putLong(month, time + getExistMonth);
        }
        editor.apply();
    }

    public static long getTimeWeek(String calendar, Context context){
        SharedPreferences timeData = context.getSharedPreferences("AttentionTimeData", Context.MODE_PRIVATE);
        long getData = timeData.getLong(calendar, 0);
        return getData;
    }

    public static long getTimeMonth(String month, Context context){
        SharedPreferences timeData = context.getSharedPreferences("AttentionTimeData", Context.MODE_PRIVATE);
        long getData = timeData.getLong(month, 0);
        return getData;
    }

    public static void clearData(Context context){
        SharedPreferences timeData = context.getSharedPreferences("AttentionTimeData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = timeData.edit();
        editor.clear();
        editor.apply();
    }
}
