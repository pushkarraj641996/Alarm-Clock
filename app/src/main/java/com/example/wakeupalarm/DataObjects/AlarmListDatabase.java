package com.example.wakeupalarm.DataObjects;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.wakeupalarm.Interfaces.AlarmDao;

@Database(entities = {Alarm.class}, version = 4, exportSchema = false)
public abstract class AlarmListDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "alarm_database";

    public abstract AlarmDao alarmDao();

    public static AlarmListDatabase ALARM_LIST_DATABASE;

    public static AlarmListDatabase getDatabase (final Context context) {
        if (ALARM_LIST_DATABASE == null) {
            synchronized ( AlarmListDatabase.class ) {
                if (ALARM_LIST_DATABASE == null) {
                    ALARM_LIST_DATABASE = Room.databaseBuilder(context, AlarmListDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return ALARM_LIST_DATABASE;
    }
}
