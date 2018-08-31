package com.lebanmohamed.stormy.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.lebanmohamed.stormy.R;
import com.lebanmohamed.stormy.adapter.DailyAdapter;
import com.lebanmohamed.stormy.databinding.ContentDailyForecastBinding;
import com.lebanmohamed.stormy.weather.Day;

import java.util.ArrayList;
import java.util.List;

public class DailyForecastActivity extends AppCompatActivity
{

    private ContentDailyForecastBinding binding;
    private DailyAdapter dailyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_daily_forecast);
        List<Day> dayList = (ArrayList<Day>) intent.getSerializableExtra("dailyList");

        binding = DataBindingUtil.setContentView(this, R.layout.content_daily_forecast);



        dailyAdapter = new DailyAdapter(dayList, this );

        binding.dailyListItems.setAdapter(dailyAdapter);
        binding.dailyListItems.setLayoutManager(new LinearLayoutManager(this));








    }


}
