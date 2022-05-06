package com.example.wakeupalarm.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.wakeupalarm.DataObjects.AddAlarmTask;
import com.example.wakeupalarm.DataObjects.Alarm;
import com.example.wakeupalarm.DataObjects.AlarmListDatabase;
import com.example.wakeupalarm.DataObjects.DeleteAlarmTask;
import com.example.wakeupalarm.DataObjects.UpdateAlarmTask;
import com.example.wakeupalarm.Interfaces.AlarmDao;

import java.util.List;

public class AlarmDbRepository {

    private AlarmDao dao;
    private LiveData<List<Alarm>> alarmList;
    private Context context;

    AlarmDbRepository(Context application) {
        AlarmListDatabase database =AlarmListDatabase.getDatabase(application);
        dao = database.alarmDao();
        alarmList = dao.getListOfAlarms();
        this.context = application;
    }

    LiveData<List<Alarm>> getAlarmList () {
        return this.alarmList;
    }

    LiveData<Boolean> isAlarmActive (int notificationID) {
        return dao.getAlarmStatus(notificationID);
    }

    void insert (Alarm alarm) {
        new AddAlarmTask(dao).execute(alarm);
    }

    void delete (Integer notificationId, Alarm deleteAlarm) {
        new DeleteAlarmTask( dao, context ).execute( notificationId, deleteAlarm);
    }

    void update ( Integer notificationID , Alarm updateAlarm ) {
        new UpdateAlarmTask(this.dao).execute( notificationID , updateAlarm );
    }
}
