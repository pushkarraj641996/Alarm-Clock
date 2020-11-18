package com.example.wakeupalarm.ViewModel;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wakeupalarm.DataObjects.Alarm;
import com.example.wakeupalarm.DataObjects.DateTime;
import com.example.wakeupalarm.DataObjects.RemainingTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmViewModel extends AndroidViewModel {
    private static final String TAG = "ApplicationViewModel";

    private Integer lastNotificationID = 0;
    private final static DateTime currentDateTime = new DateTime();

    private final LiveData<List<Alarm>> alarmList;

    private final MutableLiveData<DateTime> dateTime = new MutableLiveData<DateTime>();

    private final AlarmManager alarmManager;

    private final AlarmDbRepository repository;

    private final MutableLiveData<Long> remainingTime = new MutableLiveData<Long>();

    private CountDownTimer timer;

    private Alarm currentSelectedViewAlarm;

    public AlarmViewModel(Application application) {
        super(application);
        Log.d(TAG, "ApplicationViewModel: Constructor called");
        alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        repository = new AlarmDbRepository(application);
        alarmList = repository.getAlarmList();

        if (timer == null) {
            Log.d(TAG, "AlarmViewModel: thread started");
            startTimer();
        }
    }

    public void setCurrentSelectedViewPosition ( Alarm alarm ) {
        this.currentSelectedViewAlarm = alarm;
    }

    public void onDateSelected(int year, int month, int day) {

        Log.d(TAG, "onDateSelected: " + year);
      //  Log.d(TAG, "Time when date selected: " + dateTime.getHour());
        currentDateTime.setDate(year, month, day);
        dateTime.setValue(currentDateTime);
    }

    public void onTimeSelected(int hour, int min) {
        Log.d(TAG, "onTimeSelected: " + hour);
        //Log.d(TAG, "Date when time selected: " + dateTime.getYear());
        currentDateTime.setTime(hour, min);
        dateTime.setValue(currentDateTime);
    }

    public LiveData<List<Alarm>> getListOfAlarms() {
        Log.d(TAG, "getListOfAlarms: Returning list of alarms");
        return this.alarmList;
    }

    public MutableLiveData<DateTime> getSelectedDateTime () {
        return dateTime;
    }

    public Integer getLastNotificationID() {
        return lastNotificationID;
    }

    public LiveData<Boolean> isAlarmActive ( int notificationID ) {
        return repository.isAlarmActive( notificationID );
    }

    public MutableLiveData<Long> getCurrentTime () {
        return remainingTime;
    }

    public void addAlarm (PendingIntent intent, String name) {
        Log.d(TAG, "addAlarm: Global variable" + currentDateTime.   getHour() + "/////" + currentDateTime.getMin());
        Alarm alarm = new Alarm(currentDateTime, lastNotificationID, true, name);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    getPreparedTime().getTimeInMillis(),
                    intent);
        }
        repository.insert(alarm);
        lastNotificationID ++;
    }

    public void onUpdateAlarm ( int currentNotificationID, PendingIntent pendingIntent, boolean isAlarmActive, String name) {
        // handle when alarm is active and received intent to disable it
        // check alarm status and change pending intent accordingly.
        Alarm updateAlarm = new Alarm(currentDateTime, currentNotificationID, isAlarmActive, name);
        if (pendingIntent != null && isAlarmActive) {
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        getPreparedTime().getTimeInMillis(),
                        pendingIntent);
            }
        }
        else if (!isAlarmActive) {
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
            pendingIntent.cancel();
        }
        repository.update(null, updateAlarm);
    }

    public void onDeleteAlarm ( int notificationID, Alarm deleteAlarm ) {
        repository.delete( notificationID , deleteAlarm);
    }

    private Calendar getPreparedTime () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, currentDateTime.getHour());
        calendar.set(Calendar.MINUTE, currentDateTime.getMin());

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        return calendar;
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared: called");
        super.onCleared();
        timer.cancel();
    }

    private void startTimer () {
        timer = new CountDownTimer(24 * 60 * 60 * 1000, 60000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, "onTick: called");
                remainingTime.setValue(l);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public Alarm getCurrentSelectedViewAlarm () {
        return currentSelectedViewAlarm;
    }
}
