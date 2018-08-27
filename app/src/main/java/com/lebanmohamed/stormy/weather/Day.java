package com.lebanmohamed.stormy.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Day
{
    private long time;
    private  String summary;
    private double temperature;
    private String icon;
    private String timezone;

    public Day()
    {

    }

    public Day(long time, String summary, double temperature, String icon, String timezone)
    {
        this.time = time;
        this.summary = summary;
        this.temperature = temperature;
        this.icon = icon;
        this.timezone = timezone;
    }

    public String getTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("MMM d");
        format.setTimeZone(TimeZone.getTimeZone(timezone));

        Date date = new Date(time);
        return format.format(date);

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

    public double getTemperature() {
        return Math.round(temperature);
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
