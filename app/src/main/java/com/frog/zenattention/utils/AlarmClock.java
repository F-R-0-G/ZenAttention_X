package com.frog.zenattention.utils;

import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;

import com.frog.zenattention.MainActivity;
import com.shawnlin.numberpicker.NumberPicker;

public class AlarmClock {
    private Chronometer chronometer;
    private ProgressBar progressBar;
    private NumberPicker numberPicker;
    private static final int MAX_PROGRESS = 10000;
    private long selectTime;
    private Context context;
    private long pauseTime;
    private boolean isCancel = false;
    private long startTime;
    CountDownTimer countDownTimer;
    private Button startButton;
    private Button stopButton;
    private long leftTime;

    private static final String TAG = "Alarm_Clock";

    public AlarmClock (Chronometer chronometer, ProgressBar progressBar, Context context,
                        NumberPicker numberPicker){
        this.chronometer = chronometer;
        this.progressBar = progressBar;
        this.context = context;
        this.numberPicker = numberPicker;
    }

    public void startCounting(final int numberChoosed, Button startButton, Button stopButton){
        this.startButton = startButton;
        this.stopButton = stopButton;
        numberPicker.setVisibility(View.INVISIBLE);
        chronometer.setVisibility(View.VISIBLE);
        if (numberChoosed == 1) selectTime = 60 * 1000;
        else selectTime = (numberChoosed - 1) * 5 * 60 * 1000;
        ToastUtil.showToast(context, "设置成功");

        startTime = SystemClock.elapsedRealtime();
        chronometer.setBase(SystemClock.elapsedRealtime() + selectTime);  // 设置倒计时
        chronometer.setCountDown(true);
        chronometer.start();
        progressBar.setMax(MAX_PROGRESS);
        progressBar.setProgress(0);
        startCountDownTimer(selectTime);
    }

    private void startCountDownTimer(long duration){
        countDownTimer = new CountDownTimer(duration, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int percentage = (int) ((selectTime - millisUntilFinished) * 10000 / selectTime);
                leftTime = millisUntilFinished;
                progressBar.setProgress(percentage);
            }

            @Override
            public void onFinish() {
                chronometer.stop();
                progressBar.setProgress(0);
                chronometer.setVisibility(View.INVISIBLE);
                numberPicker.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                if (isCancel){
                    isCancel = false;
                    return;
                }
                AttentionTimeData.storeTime(selectTime, context);      // 存储时间数据

                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationUtils notification = new NotificationUtils(context);
                notification.sendNotification("时间","预设时间到", pi);

                PowerManager pm = (PowerManager) context       // 点亮屏幕
                        .getSystemService(Context.POWER_SERVICE);
                boolean screenOn = pm.isScreenOn();
                if (!screenOn) {
                    PowerManager.WakeLock wl = pm.newWakeLock(
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                    wl.acquire(10000); // 点亮屏幕
                    wl.release(); // 释放
                }
            }
        };
        countDownTimer.start();
    }

    public void pauseAlarm(){
        countDownTimer.cancel();
        chronometer.stop();
        pauseTime = SystemClock.elapsedRealtime();
    }

    public void resumeAlarm(){
        if (pauseTime != 0){
            chronometer.setBase(chronometer.getBase() + (SystemClock.elapsedRealtime() - pauseTime));
        }
        else {
            chronometer.setBase(SystemClock.elapsedRealtime());
        }
        chronometer.start();
        startCountDownTimer(leftTime);
    }

    public void cancelAlarm(){
        isCancel = true;
        countDownTimer.onFinish();
    }
}
