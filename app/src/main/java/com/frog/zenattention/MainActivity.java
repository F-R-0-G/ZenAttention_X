package com.frog.zenattention;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.frog.zenattention.utils.ActivityCollector;
import com.frog.zenattention.utils.AlarmClock;
import com.frog.zenattention.utils.HintPopupWindow;
import com.frog.zenattention.utils.PrefUtil;
import com.frog.zenattention.utils.ToastUtil;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.shawnlin.numberpicker.NumberPicker;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
    int mediaPlayerStatus = 0;

    private final int CHOOSE_MUSIC = 1000;
    private final int REQUEST_WRITE_STORAGE = 1111;

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
        numberPicker.setValue(1);
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
        strList.add("鸟");
        strList.add("虫");
        strList.add("鱼");
        strList.add("雨");
        strList.add("自定义");

        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener bird = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bird);
                    mediaPlayer.start();
                } else {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bird);
                }
                ToastUtil.showToast(MainActivity.this, "已切换", Toast.LENGTH_SHORT);
                PrefUtil.saveSelectedMusic("bird");
            }
        };
        View.OnClickListener insect = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.insect);
                    mediaPlayer.start();
                } else {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.insect);
                }
                ToastUtil.showToast(MainActivity.this, "已切换", Toast.LENGTH_SHORT);
                PrefUtil.saveSelectedMusic("insect");
            }
        };
        View.OnClickListener fish = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fish);
                    mediaPlayer.start();
                } else {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fish);
                }
                ToastUtil.showToast(MainActivity.this, "已切换", Toast.LENGTH_SHORT);
                PrefUtil.saveSelectedMusic("fish");
            }
        };
        View.OnClickListener rain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.heavy_rain);
                    mediaPlayer.start();
                } else {
                    stopAndReleaseMedia();
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.heavy_rain);
                }
                ToastUtil.showToast(MainActivity.this, "已切换", Toast.LENGTH_SHORT);
                PrefUtil.saveSelectedMusic("rain");
            }
        };
        View.OnClickListener custom = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                } else {
                    chooseMusicFromStorage();
                }
                PrefUtil.saveSelectedMusic("custom");
            }
        };
        clickList.add(bird);
        clickList.add(insect);
        clickList.add(fish);
        clickList.add(rain);
        clickList.add(custom);

        hintPopupWindow = new HintPopupWindow(this, strList, clickList);

    }

    private void chooseMusicFromStorage() {
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(CHOOSE_MUSIC)
                .withTitle("选择音乐")
                .withIconStyle(Constant.ICON_STYLE_BLUE)
                .withMutilyMode(false)
                //.withMaxNum(2)
                .withStartPath("/storage/emulated/0/Download")//指定初始显示路径
                .withNotFoundBooks("至少选择一个文件")
                //.withIsGreater(false)//过滤文件大小 小于指定大小的文件
                //.withFileSize(500 * 1024)//指定文件大小为500K
                //.withChooseMode(false)//文件夹选择模式
                .withFileFilter(new String[]{"mp3", "wma"})
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_MUSIC) {
                List<String> list = data.getStringArrayListExtra("paths");
                String path = list.get(0);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    stopAndReleaseMedia();
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.prepare();
                    } catch (IOException ex) {
                        ToastUtil.showToast(this, "文件已损坏或不存在", Toast.LENGTH_SHORT);
                    }
                    mediaPlayer.start();
                } else {
                    stopAndReleaseMedia();
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.prepare();
                    } catch (IOException ex) {
                        ToastUtil.showToast(this, "文件已损坏或不存在", Toast.LENGTH_SHORT);
                    }
                }
                PrefUtil.saveCustomMusicPath(path);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseMusicFromStorage();
                } else {
                    ToastUtil.showToast(this, "权限被拒绝", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_music:
                ToastUtil.showToast(this, getString(R.string.prompt_change_music), Toast.LENGTH_SHORT);
                if (mediaPlayer != null) {
                    if (mediaPlayerStatus != 0) {
                        mediaPlayer.seekTo(mediaPlayerStatus);
                    }
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer arg0) {
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);
                        }
                    });
                    start_music.setVisibility(View.INVISIBLE);
                    pause_music.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.pause_music:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayerStatus = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    start_music.setVisibility(View.VISIBLE);
                    pause_music.setVisibility(View.INVISIBLE);
                }
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
                if (alarm_clock.isFinish) {
                    // 如果已经计时结束，则新建计时器
                    alarm_clock = new AlarmClock(chronometer, progressBar,
                            MainActivity.this, numberPicker,
                            startAttachAttention,
                            stopButton, addTimeButton, finishButton);
                    alarm_clock.startCounting(3);
                    stopButton.setVisibility(View.VISIBLE);
                    addTimeButton.setVisibility(View.INVISIBLE);
                    finishButton.setVisibility(View.INVISIBLE);
                    vibrator.vibrate(50);
                } else {
                    stopButton.setVisibility(View.VISIBLE);
                    resumeButton.setVisibility(View.INVISIBLE);
                    cancelButton.setVisibility(View.INVISIBLE);
                    addTimeButton.setVisibility(View.INVISIBLE);
                    alarm_clock.addTime(10 * 60 * 1000);
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
        return true;
        //return true即可解决长按事件跟点击事件同时响应的问题
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
        String music = PrefUtil.getSelectedMusic();
        mediaPlayerStatus = 0;
        if (music == null) {
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bird);
        } else if (music.equals("custom")) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(PrefUtil.getCustomMusicPath());
                mediaPlayer.prepare();
            } catch (IOException ex) {
                ToastUtil.showToast(this, "文件损坏或不存在");
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bird);
            }
        } else {
            if (music.equals("bird")) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bird);
            } else if (music.equals("insect")) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.insect);
            } else if (music.equals("fish")) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fish);
            } else if (music.equals("rain")) {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.heavy_rain);
            } else {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bird);
            }
        }
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
    //再按一次退出

    private void stopAndReleaseMedia() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAndReleaseMedia();
    }
}