package com.example.wakeupalarm.DataObjects;

import android.os.AsyncTask;

import com.example.wakeupalarm.Interfaces.AlarmDao;

public class AddAlarmTask extends AsyncTask<Alarm, Void, Void> {

    private AlarmDao dao;

    public AddAlarmTask(AlarmDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Alarm... alarms) {
        dao.insert(alarms[0]);
        return null;
    }
}
