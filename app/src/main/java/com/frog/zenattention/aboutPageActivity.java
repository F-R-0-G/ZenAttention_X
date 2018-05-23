package com.frog.zenattention;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_about_page);

        Element version = new Element();
        version.setTitle("Version 1.0.0");
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTime++;
                if (countTime == 5){
                    ToastUtil.showToast(aboutPageActivity.this, "Excited!");
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
                .create();

        RelativeLayout rl = findViewById(R.id.about_title);
        rl.addView(aboutPage);
    }
}
