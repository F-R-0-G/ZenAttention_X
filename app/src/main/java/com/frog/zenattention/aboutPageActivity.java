package com.frog.zenattention;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.frog.zenattention.utils.ToastUtil;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class aboutPageActivity extends AppCompatActivity {

    private int countTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));          // 设置底部导航栏的颜色
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));             // 设置状态栏的颜色
        setContentView(R.layout.activity_about_page);

        Element version = new Element();
        version.setTitle("Version 1.0");
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTime++;
                if (countTime == 5){
                    ToastUtil.showToast(aboutPageActivity.this, "Excited!");
                }else if(countTime==10){
                    ToastUtil.showToast(aboutPageActivity.this, "再点也不会进入开发者模式（笑）");
                    countTime = 0;
                }
            }
        });

        Element thanks = new Element();
        thanks.setTitle("鸣谢：枫叶的印记、reset、舍车、栾修非");
        thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTime++;
                if (countTime == 5){
                    ToastUtil.showToast(aboutPageActivity.this, "以及67，和用到的所有开源库的作者(笑）");
                }else if(countTime==10){
                    ToastUtil.showToast(aboutPageActivity.this, "by Wen Sun, Green-Wood");
                    countTime = 0;
                }
            }
        });


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.about_image)
                .setDescription("ZenAttention 一个能让你静下心专注的软件")
                .addGitHub("F-R-0-G")
                .addItem(version)
                .addItem(thanks)
                .create();

        RelativeLayout rl = findViewById(R.id.aboutPageBody);
        rl.addView(aboutPage);
    }
}
