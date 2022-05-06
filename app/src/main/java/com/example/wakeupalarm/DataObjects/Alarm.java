package com.example.wakeupalarm.DataObjects;

import android.app.PendingIntent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Update;

@Entity(tableName = "alarm_table")
public class Alarm {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "NotificationID")
    private final int notificationId;

    @ColumnInfo(name = "Alarm_Name")
    private String name;

    @Embedded
    private DateTime dateTime;

    @ColumnInfo(name = "Alarm_Active")
    private boolean isAlarmActive;

    private String alarmType;

    public Alarm(@NonNull DateTime dateTime, int notificationId, boolean isAlarmActive, String name, String alarmType) {
        this.notificationId = notificationId;
        this.dateTime = dateTime;
        this.isAlarmActive = isAlarmActive;
        this.name = name;
        this.alarmType = alarmType;
    }

    @NonNull
    public DateTime getDateTime() {
        return dateTime;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public boolean isAlarmActive() {
        return isAlarmActive;
    }

    public String getName() {
        return name;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmActive(boolean alarmActive) {
        isAlarmActive = alarmActive;
    }
}