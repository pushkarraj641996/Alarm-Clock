package com.example.wakeupalarm.DataObjects;

import android.os.CountDownTimer;
import android.util.Log;

public class RemainingTime extends Thread {
    private static final String TAG = "RemainingTime";

    private Long remainingTime;
    private CountDownTimer timer;
    private Long targetMinutes;

    public RemainingTime(Long targetMinutes) {
        Log.d(TAG, "RemainingTime: called");
        this.targetMinutes = targetMinutes;
        this.remainingTime = targetMinutes;
    }

    @Override
    public void run() {
        timer = new CountDownTimer(targetMinutes, 60000) {
            @Override
            public void onTick(long l) {
               remainingTime = l;
            }

            @Override
            public void onFinish() {

            }
        };
    }

    public Long getRemainingTime () {
        return remainingTime;
    }

    public void stopTimer () {
        timer.cancel();
    }
}

