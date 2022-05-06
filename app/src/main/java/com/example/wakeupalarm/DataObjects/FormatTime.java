package com.example.wakeupalarm.DataObjects;

import com.example.wakeupalarm.R;

public final class FormatTime {

    public static String returnTime(DateTime dateTime) {
        int hour = dateTime.getHour();
        int min = dateTime.getMin();
        String time = null;

        if (hour > 12) {
            hour -= 12;
        }

        if (hour == 10 || hour == 11 || hour == 12) {
            time = String.valueOf(hour);
            time += ":";
        } else {
            time = "0";
            time += String.valueOf(hour);
            time += ":";
        }

        if (min < 10) {
            time += "0";
            time += String.valueOf(min);
        } else {
            time += String.valueOf(min);
        }
        return time;
    }

    public static String getTypography ( DateTime dateTime ) {

        if (dateTime.getHour() > 12) {
            return "pm";
        }
        else{
            return "am";
        }
    }
}
