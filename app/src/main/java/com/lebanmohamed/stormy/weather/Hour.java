package com.lebanmohamed.stormy.weather;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import java.io.Serializable;
import java.util.Date;

public class Hour implements Serializable
{
    private long time;
    private  String summary;
    private double temperature;
    private String icon;
    private String timezone;

    public Hour()
    {

    }

    public Hour(long time, String summary, double temperature, String icon, String timezone)
    {
        this.time = time;
        this.summary = summary;
        this.temperature = temperature;
        this.icon = icon;
        this.timezone = timezone;
    }

    public String getTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("H:MM A");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));

        Date dateTime  = new Date(time * 1000);
        return formatter.format(dateTime);

    }


    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getTemperature() {
        return Math.round((int) temperature);
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getIcon() {
        return Forecast.getIconID(icon);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }


}
