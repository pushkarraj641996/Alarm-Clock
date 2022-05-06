package com.example.wakeupalarm.DataObjects;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.wakeupalarm.Interfaces.AlarmDao;
import com.example.wakeupalarm.ViewModel.AlarmBroadcastReceiver;

public class DeleteAlarmTask extends AsyncTask<Object, Void, Void> {

    AlarmDao dao;
    Context context;

    public DeleteAlarmTask( AlarmDao dao, Context context ) {
        this.dao = dao;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        int searchID = -1;
        Alarm deleteAlarm = null;

        if ( objects[0] != null ) {
            searchID = (int) objects[0];
            deleteAlarm = dao.getAlarmByNotificationId(searchID);
        }
        else if ( objects[1] != null ) {
            deleteAlarm =  (Alarm) objects[1];
            searchID = deleteAlarm.getNotificationId();
        }
        if (searchID != -1) {
            PendingIntent cancelIntent = PendingIntent.getBroadcast(context,
                    searchID,
                    new Intent(context, AlarmBroadcastReceiver.class),
                    PendingIntent.FLAG_NO_CREATE);
            if (cancelIntent != null) {
                cancelIntent.cancel();
            }
        }

        if (deleteAlarm != null) {
            dao.delete(deleteAlarm);
        }
        return null;
    }
}
