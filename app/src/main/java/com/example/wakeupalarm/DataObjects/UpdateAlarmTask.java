package com.example.wakeupalarm.DataObjects;

import android.os.AsyncTask;

import com.example.wakeupalarm.Interfaces.AlarmDao;

public class UpdateAlarmTask extends AsyncTask<Object, Void, Void> {

    private AlarmDao dao;

    public UpdateAlarmTask(AlarmDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        if ( objects[0] != null ) {
            Integer notificationID = (Integer) objects[0];
            dao.setAlarmStatus( notificationID, false );
        }
        else if ( objects[1] != null ) {
            Alarm updateAlarm = (Alarm) objects[1];
            dao.updateAlarm(updateAlarm);
        }

        return null;
    }
}
