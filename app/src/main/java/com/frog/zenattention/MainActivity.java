package com.frog.zenattention;

import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frog.zenattention.utils.ActivityCollector;
import com.frog.zenattention.utils.AlarmClock;
import com.frog.zenattention.utils.HintPopupWindow;
import com.frog.zenattention.utils.ToastUtil;
import com.shawnlin.numberpicker.NumberPicker;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;


public class MainActivity extends BasicActivity implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private Chronometer chronometer;
    private ProgressBar progressBar;
    private ProgressBar cancel_bar;
    private NumberPicker numberPicker;
    private Button stopButton;
    private Button cancelButton;
    private Button resumeButton;
    private Button addTimeButton;
    private Button startAttachAttention;
    private Button finishButton;
    private AlarmClock alarm_clock;
    private boolean isCancel = true;
    private ValueAnimator animator;
    private Vibrator vibrator;

    private HintPopupWindow hintPopupWindow;

    private Button open_statistics;
    private Button start_music;
    private Button pause_music;

    MediaPlayer mediaPlayer;
    int mediaPlayerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //定义全屏参数
        window.setFlags(flag, flag);
        //设置当前窗体为全屏显示

        setContentView(R.layout.activity_main);

        ScreenAdapterTools.getInstance().loadView((ViewGroup) getWindow().getDecorView());

        start_music = (Button) findViewById(R.id.start_music);
        pause_music = (Button) findViewById(R.id.pause_music);
        open_statistics = (Button) findViewById(R.id.open_statistics);
        start_music.setOnClickListener(this);
        pause_music.setOnClickListener(this);
        open_statistics.setOnClickListener(this);

        start_music.setOnLongClickListener(this);
        pause_music.setOnLongClickListener(this);

        start_music.setVisibility(View.VISIBLE);
        pause_music.setVisibility(View.INVISIBLE);

        initMediaPlayer();

        chronometer = findViewById(R.id.Clock_chronometer);
        chronometer.setVisibility(View.INVISIBLE);
        progressBar = findViewById(R.id.Clock_ProgressBar);
        cancel_bar = findViewById(R.id.cancel_progressBar);
        cancel_bar.setVisibility(View.INVISIBLE);
        // 创建计时器和进度条, 将cancel_bar设为不可见
        numberPicker = findViewById(R.id.number_picker);
        String[] displayNumber = new String[13];
        displayNumber[0] = "1:00";
        for (int i = 1; i < 13; i++) {
            displayNumber[i] = Integer.toString(i * 5) + ":" + "00";
        }
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(displayNumber.length);
        numberPicker.setDisplayedValues(displayNumber);
        numberPicker.setValue(6);
        // 创建时间选择器
        startAttachAttention = findViewById(R.id.start_attach_attention);
        startAttachAttention.setOnClickListener(this);
        startAttachAttention.getBackground().setAlpha(128); //设置半透明
        //开始专注按钮
        stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        stopButton.setVisibility(View.INVISIBLE);
        stopButton.getBackground().setAlpha(128); //设置半透明
        // 暂停按钮，设为不可见
        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnTouchListener(this);
        cancelButton.setVisibility(View.INVISIBLE);
        cancelButton.getBackground().setAlpha(128); //设置半透明
        // 取消按钮，设为不可见
        resumeButton = findViewById(R.id.resume_button);
        resumeButton.setOnClickListener(this);
        resumeButton.setVisibility(View.INVISIBLE);
        resumeButton.getBackground().setAlpha(128); //设置半透明
        //继续按钮，设为不可见
        addTimeButton = findViewById(R.id.addTime_button);
        addTimeButton.setOnClickListener(this);
        addTimeButton.setVisibility(View.INVISIBLE);
        addTimeButton.getBackground().setAlpha(128); // 设置半透明
        // 加10分钟按钮
        finishButton = findViewById(R.id.finish_button);
        finishButton.setOnClickListener(this);
        finishButton.setVisibility(View.INVISIBLE);
        finishButton.getBackground().setAlpha(128);
        // 计时结束后完成按钮
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        // 震动

        //下面的操作是初始化弹出数据
        ArrayList<String> strList = new ArrayList<>();
        strList.add("选项item1");
        strList.add("选项item2");
        strList.add("选项item3");

        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击事件触发", Toast.LENGTH_SHORT).show();
            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        hintPopupWindow = new HintPopupWindow(this, strList, clickList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_music:
                ToastUtil.showToast(this,getString(R.string.prompt_change_music),Toast.LENGTH_SHORT);

                break;
            case R.id.pause_music:

                break;
            case R.id.open_statistics:
                startActivity(new Intent(this, checkStatistic.class));
                break;
            case R.id.start_attach_attention:
                alarm_clock = new AlarmClock(chronometer, progressBar,
                        MainActivity.this, numberPicker,
                        startAttachAttention,
                        stopButton, addTimeButton, finishButton);
                // 计时器实例
                int num = numberPicker.getValue();
                alarm_clock.startCounting(num);
                startAttachAttention.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                vibrator.vibrate(50);
                break;
            case R.id.resume_button:
                stopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                addTimeButton.setVisibility(View.INVISIBLE);
                alarm_clock.resumeAlarm(0);
                break;
            case R.id.stop_button:
                resumeButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                addTimeButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                alarm_clock.pauseAlarm();
                break;
            case R.id.addTime_button:
                if (alarm_clock.isFinish) {         // 如果已经计时结束，则新建计时器
                    alarm_clock = new AlarmClock(chronometer, progressBar,
                            MainActivity.this, numberPicker,
                            startAttachAttention,
                            stopButton, addTimeButton, finishButton);
                    alarm_clock.startCounting(1);
                    stopButton.setVisibility(View.VISIBLE);
                    addTimeButton.setVisibility(View.INVISIBLE);
                    finishButton.setVisibility(View.INVISIBLE);
                    vibrator.vibrate(50);
                } else {
                    stopButton.setVisibility(View.VISIBLE);
                    resumeButton.setVisibility(View.INVISIBLE);
                    cancelButton.setVisibility(View.INVISIBLE);
                    addTimeButton.setVisibility(View.INVISIBLE);
                    alarm_clock.addTime(1 * 60 * 1000);
                    vibrator.vibrate(50);
                }
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.start_music:
                hintPopupWindow.showPopupWindow(v);
                break;
            case R.id.pause_music:
                hintPopupWindow.showPopupWindow(v);
                break;
            default:
                break;
        }
        return true;   //return true即可解决长按事件跟点击事件同时响应的问题
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.cancel_button:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(50);               // 震动
                    cancel_bar.setVisibility(View.VISIBLE);
                    animator = ValueAnimator.ofInt(0, 100);
                    animator.setDuration(2000);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int value = (int) animator.getAnimatedValue();
                            cancel_bar.setProgress(value);
                            if (value == 100) {
                                cancel_bar.setProgress(0);
                                if (!isCancel) {
                                    isCancel = true;
                                    return;
                                }
                                cancel_bar.setVisibility(View.INVISIBLE);
                                resumeButton.setVisibility(View.INVISIBLE);
                                cancelButton.setVisibility(View.INVISIBLE);
                                addTimeButton.setVisibility(View.INVISIBLE);
                                alarm_clock.cancelAlarm();
                                ToastUtil.showToast(MainActivity.this, "计时已取消");
                                vibrator.vibrate(50);
                            }
                        }
                    });
                    animator.start();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isCancel = false;
                    animator.end();
                    cancel_bar.setVisibility(View.INVISIBLE);
                }
            case R.id.start_music:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(50);
                    // 震动
                }
                break;
            case R.id.pause_music:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(50);
                    // 震动
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void initMediaPlayer() {

    }

    private long mExitTime = 0;

    //计时器，虽然放在这里很丑，但放在实例区明显不合适，就凑合一下（可理解）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 1000) {
                ToastUtil.showToast(this, "再按一次退出程序", Toast.LENGTH_SHORT);
                mExitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //实现再按一次退出，退出时说骚话并以home形式存储

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mediaPlayer1.release();
    }
}