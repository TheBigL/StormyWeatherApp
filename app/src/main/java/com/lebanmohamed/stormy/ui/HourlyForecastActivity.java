package com.lebanmohamed.stormy.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.lebanmohamed.stormy.R;
import com.lebanmohamed.stormy.adapter.HourlyAdapter;
import com.lebanmohamed.stormy.databinding.ActivityHourlyForecastBinding;
import com.lebanmohamed.stormy.weather.Hour;

import java.util.ArrayList;
import java.util.List;

public class HourlyForecastActivity extends AppCompatActivity
{

    private ActivityHourlyForecastBinding binding;
    private HourlyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        List<Hour> hoursList = (ArrayList<Hour>) intent.getSerializableExtra("hourlyList");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_hourly_forecast);

        adapter = new HourlyAdapter(hoursList, this);


        binding.hourlyListItems.setAdapter(adapter);
        binding.hourlyListItems.setHasFixedSize(true);
        binding.hourlyListItems.setLayoutManager(new LinearLayoutManager(this));





    }

}
