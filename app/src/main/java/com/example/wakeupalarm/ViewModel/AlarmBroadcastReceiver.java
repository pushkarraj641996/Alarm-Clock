package com.example.wakeupalarm.ViewModel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import com.example.wakeupalarm.AlarmTriggered;
import com.example.wakeupalarm.R;
import com.example.wakeupalarm.ViewModel.AlarmDbRepository;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private final static String NOTIFICATION_ID = "Notification_KEY";
    private final static String ALARM_NAME = "alarm_name";
    private static final String TAG = "AlarmBroadcastReceiver";

    private AlarmDbRepository repository;
    private Context ctx;

    private int notificationID;
    private String alarmName;

    @Override
    public void onReceive(Context context, Intent intent) {
        repository = new AlarmDbRepository(context);
        this.ctx = context;

        if (intent.hasExtra("NOTIFICATION_ID")) {
            notificationID = intent.getIntExtra("NOTIFICATION_ID", -1);
            Log.d(TAG, "onReceive: Received Intent extra  = " + notificationID);
        }
        else{
            Log.d(TAG, "onReceive: Extra received in intent is null");
        }
        
        if ( intent.hasExtra("ALARM_NAME") ) {
            if (intent.getStringExtra("ALARM_NAME") != null) {
                alarmName = intent.getStringExtra("ALARM_NAME");
            }
            else{
                alarmName = null;
            }
        }
        else {
            Log.d(TAG, "onReceive: Received alarm name through intent is null");
        }

        Log.d(TAG, "onReceive: Alarm Up");
        Toast.makeText(context, "Alarm triggered", Toast.LENGTH_SHORT).show();
        intent = new Intent(context.getApplicationContext(), AlarmTriggered.class);
        intent.putExtra(NOTIFICATION_ID, notificationID);
        intent.putExtra(ALARM_NAME, alarmName);
        context.startActivity(intent);

        if (notificationID != -1) {
            //repository.delete(id);
            repository.update( notificationID, null );
            Log.d(TAG, "onReceive: Notification Id = " + notificationID);
        }
    }
}
