package com.example.wakeupalarm.Interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wakeupalarm.DataObjects.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert
    void insert(Alarm alarm);

    @Delete
    void delete (Alarm alarm);

    @Query("UPDATE alarm_table SET Alarm_Active = :isAlarmActive WHERE NotificationID = :notificationID")
    void setAlarmStatus (int notificationID, Boolean isAlarmActive);

    @Update
    void updateAlarm ( Alarm alarm );

    @Query("SELECT * FROM alarm_table")
    LiveData<List<Alarm>> getListOfAlarms();

    @Query("SELECT * FROM alarm_table WHERE NotificationID LIKE :notificationID LIMIT 1")
    Alarm getAlarmByNotificationId( int notificationID );

    @Query("SELECT Alarm_Active FROM alarm_table WHERE NotificationID LIKE :notificationID LIMIT 1")
    LiveData<Boolean> getAlarmStatus (int notificationID);
}
