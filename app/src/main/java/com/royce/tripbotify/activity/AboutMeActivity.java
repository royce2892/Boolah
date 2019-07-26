package com.royce.tripbotify.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.royce.tripbotify.R;
import com.royce.tripbotify.utils.AppConstants;
import com.royce.tripbotify.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_me);
        initActionBar();

        setUpPieChart();
    }

    private void setUpPieChart() {

        Set<String> set = PreferenceManager.getInstance(this).getSet(AppConstants.PREFS_TAGS);
        HashMap<String, Integer> placesCount = new HashMap<>();
        Log.i(AppConstants.LOG_TAG, set.toString());
        int total = 0;
        for (String tag : set) {
            //Log.i(AppConstants.LOG_TAG, tag + " count = " + PreferenceManager.getInstance(this).getInt(tag));
            total += PreferenceManager.getInstance(this).getInt(tag);
            placesCount.put(tag, PreferenceManager.getInstance(this).getInt(tag));
        }

        PieChart pieChart = findViewById(R.id.pie_chart);
        pieChart.setUsePercentValues(true);

        ArrayList<Entry> yAxisValues = new ArrayList<>();
        ArrayList<String> xAxisValues = new ArrayList<>();

        int index = 0;


        for (String key : placesCount.keySet()) {
            if (placesCount.get(key) > 5) {
                xAxisValues.add(key);
                yAxisValues.add(new Entry((float) placesCount.get(key) / total, index++));
            }
        }

        PieDataSet dataSet = new PieDataSet(yAxisValues, "");
        PieData data = new PieData(xAxisValues, dataSet);

        data.setValueFormatter(new PercentFormatter());
//data.setValueFormatter(new DefaultValueFormatter(0));

        pieChart.setData(data);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        int[] colors = {R.color.flat_ui_1, R.color.flat_ui_2, R.color.flat_ui_3, R.color.flat_ui_4, R.color.flat_ui_5,
                R.color.flat_ui_bg_blue, R.color.flat_ui_green, R.color.flat_ui_orange, R.color.flat_ui_purple, R.color.flat_ui_red};
        dataSet.setColors(ColorTemplate.createColors(getResources(), colors));
        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        //dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        pieChart.setDescription("How I like to travel");
        pieChart.setDrawHoleEnabled(false);

        pieChart.animateXY(1400, 1400);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("About Me");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
