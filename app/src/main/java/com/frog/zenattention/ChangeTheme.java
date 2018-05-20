package com.frog.zenattention;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.frog.zenattention.colorUi.util.ColorUiUtil;
import com.frog.zenattention.colorUi.util.SharedPreferencesMgr;
import com.frog.zenattention.utils.ActivityCollector;

public class ChangeTheme extends BasicActivity {

    LinearLayout ll_autumn;
    LinearLayout ll_luxun;
    LinearLayout ll_picnic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_theme);

        ll_autumn = (LinearLayout) findViewById(R.id.ll_autumn);
        ll_luxun = (LinearLayout) findViewById(R.id.ll_luxun);
        ll_picnic = (LinearLayout) findViewById(R.id.ll_picnic);

        ll_autumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesMgr.getInt("theme", 0) != 0) {
                    SharedPreferencesMgr.setInt("theme", 0);
                    setTheme(R.style.theme_1);
                    setAnimation();
                    ActivityCollector.finishOthers(ChangeTheme.this);
                }
            }
        });

        ll_luxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesMgr.getInt("theme", 0) != 1) {
                    SharedPreferencesMgr.setInt("theme", 1);
                    setTheme(R.style.theme_2);
                    setAnimation();
                    ActivityCollector.finishOthers(ChangeTheme.this);
                }
            }
        });

        ll_picnic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesMgr.getInt("theme", 0) != 2) {
                    SharedPreferencesMgr.setInt("theme", 2);
                    setTheme(R.style.theme_3);
                    setAnimation();
                    ActivityCollector.finishOthers(ChangeTheme.this);
                }
            }
        });
    }

    private void setAnimation() {
        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);
        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View localView2 = new View(getApplicationContext());
            localView2.setBackground(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(localView2, params);
            localView2.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ColorUiUtil.changeTheme(rootView, getTheme());
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(localView2);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ActivityCollector.getActivities().size() == 1) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}