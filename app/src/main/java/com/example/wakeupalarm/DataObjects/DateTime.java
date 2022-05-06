package com.example.wakeupalarm.DataObjects;

import androidx.room.Ignore;

public class DateTime {

    public int date;
    public int month;
    public int year;

    public int hour;
    public int min;

    @Ignore
    public void setDate(int year, int month, int date) {
        this.date = date;
        this.month = month;
        this.year = year;
    }

    @Ignore
    public void setTime (int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    @Ignore
    public int getDate() {
        return date;
    }

    @Ignore
    public int getMonth() {
        return month;
    }

    @Ignore
    public int getYear() {
        return year;
    }

    @Ignore
    public int getHour() {
        return hour;
    }

    @Ignore
    public int getMin() {
        return min;
    }
}
