package com.frog.zenattention;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.frog.zenattention.utils.AttentionTimeData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class checkStatistic extends BasicActivity {
    private static String[] week = {"周日", "周一", "周二", "周三", "周四", "周五" ,"周六"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setNavigationBarColor(Color.parseColor("#897D55"));          // 设置底部导航栏的颜色

        setContentView(R.layout.activity_check_statistic);

        BarChart barChart = findViewById(R.id.chart);
        List<BarEntry> entries = new ArrayList<>();
        final String[] quarters = new String[7];
        Calendar c = Calendar.getInstance();

        for (int i = 0; i < 7; i++){
            String dateInWeek = getDayInWeek(c);
            quarters[6-i] = dateInWeek;
            String dateInString = getDateInString(c);
            long getTime = AttentionTimeData.getTime(dateInString, checkStatistic.this);
            float timeInFloat = (float) (getTime / 1000 / 60);
            entries.add(new BarEntry(6-i, timeInFloat));
            c.add(Calendar.DAY_OF_MONTH, -1);
        }

        quarters[6] = "今天";

        BarDataSet set = new BarDataSet(entries, "专注时间（分钟）");
        BarData data = new BarData(set);


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {        // 将x轴设为周一到周日
                return quarters[(int) value];
            }
        };

        set.setValueFormatter(new IValueFormatter() {          // 将柱状图数据设为整数
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                return n + "";
            }
        });

        barChart.setData(data);
        set.setValueTextSize(15f);         // 设置柱状图上方的值的字体
        set.setColors(new int[]{Color.parseColor("#B4A582")});
        data.setBarWidth(0.9f);
        barChart.setFitBars(true);
        barChart.setNoDataText("你还没有开始专注");
        barChart.animateY(3000, Easing.EasingOption.EaseInOutCubic);   // 设置y轴动画
        barChart.getDescription().setEnabled(false);       // 将description label去掉

        AssetManager mgr=getAssets();
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/Extralight.ttf");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);           // 去掉网格线
        xAxis.setTextSize(15f);                // 设置x轴的字体大小
        xAxis.setTypeface(tf);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(formatter);    // 将x轴设为周一到周日

        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setAxisMinimum(0f);
        yLAxis.setEnabled(false);

        YAxis yRAxis = barChart.getAxisRight();
        yRAxis.setAxisMinimum(0f);
        yRAxis.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setTextSize(15f);  // 设置标签的字体大小
        legend.setTypeface(tf);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        barChart.invalidate();

    }

    private String getDateInString(Calendar c){
        String year = Integer.toString(c.get(Calendar.YEAR));
        String month = Integer.toString(c.get(Calendar.MONTH) + 1);
        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String calendarData = year + "//" + month + "//" + day;
        return calendarData;
    }

    private String getDayInWeek(Calendar c){
        int dayInWeek = c.get(Calendar.DAY_OF_WEEK);
        return week[dayInWeek - 1];
    }
}
