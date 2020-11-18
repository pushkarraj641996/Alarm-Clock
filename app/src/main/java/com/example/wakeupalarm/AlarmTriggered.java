package com.example.wakeupalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wakeupalarm.AlamListAdapter.NewsHeadlineAdapter;
import com.example.wakeupalarm.DataObjects.CreateNotification;
import com.example.wakeupalarm.DataObjects.FetchNewsHeadlines;
import com.example.wakeupalarm.Interfaces.TaskDone;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AlarmTriggered extends AppCompatActivity implements TaskDone {
    private static final String TAG = "AlarmTriggered";
    private final static String ALARM_NAME = "alarm_name";
    private final static String NOTIFICATION_ID = "Notification_KEY";

    private MaterialButton button;
    private TextView alarmTextView;
    private MediaPlayer mediaPlayer;
    private int notificationID;
    private int currentHeadlineNumber;
    private String alarmName;
    private boolean isAlarmStopped;
    private boolean hasFocus;
    private ListView newsItems;
    private ProgressBar progressBar;
    private TextInputEditText validateText;
    private List<String> headlines;
    private static final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_triggered);
        button = (MaterialButton) findViewById(R.id.stop_alarm);
        alarmTextView = (TextView) findViewById(R.id.alarm_name);
        newsItems = (ListView) findViewById(R.id.news_headline_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        validateText = findViewById(R.id.validate_text);

        isAlarmStopped = false;
        hasFocus = true;
        init();
        startAlarm();
    }

    private void init() {
        prepareAlarmWindow ();
        dataHandler ();
        new FetchNewsHeadlines(this, newsItems, progressBar, this).execute();
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                stopAlarm();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateText();
            }
        });
    }

    private void prepareAlarmWindow () {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    private void validateText() {
        if (validateText.getText() != null) {
            String input = validateText.getText().toString();
            Log.d(TAG, "validateText: Input1 = " + input);
            Log.d(TAG, "validateText: Input2 = "+ headlines.get(currentHeadlineNumber));
            if (input.equals(headlines.get(currentHeadlineNumber))) {
                stopAlarm();
            } else {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Enter valid text", Toast.LENGTH_SHORT).show();
        }
    }

    private void dataHandler () {
        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra(NOTIFICATION_ID) && receivedIntent.hasExtra(ALARM_NAME)) {
            notificationID = receivedIntent.getIntExtra(NOTIFICATION_ID, -1);

            if (receivedIntent.getStringExtra(ALARM_NAME) != null) {
                alarmName = receivedIntent.getStringExtra(ALARM_NAME);
            }
        }

        if (alarmName != null) {
            alarmTextView.setText(alarmName);
        }
    }

    private void startAlarm() {
        Uri alarmTone  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        );
        try {
            mediaPlayer.setDataSource(getApplicationContext(), alarmTone);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private void stopAlarm() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        isAlarmStopped = true;
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.hasFocus = hasFocus;
        if (hasFocus) {
            Log.d(TAG, "onWindowFocusChanged: has Focus = true");
            hideSystemUI();
        }
        else{
            Log.d(TAG, "onWindowFocusChanged: has focus = false");
        }
    }

    private void hideSystemUI() {
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0 && View.SYSTEM_UI_FLAG_HIDE_NAVIGATION != 0) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {}

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: Invoked");
        super.onStop();
        if ( !isAlarmStopped && !hasFocus) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                if (notificationID != -1) {
                    CreateNotification notification = new CreateNotification(getApplicationContext(), notificationID);
                    Intent intent = new Intent(this, MainActivity.class);
                    notification.sendNotification("Missed Alarm", "The Alarm was missed", intent);
                } else {
                    Log.d(TAG, "onPause: Invalid notification ID received");
                }
            }
        }
    }

    @Override
    public void onLoadingFinished(List<String> headlines) {
        this.headlines = headlines;
        currentHeadlineNumber = random.nextInt(5);
        Log.d(TAG, "onLoadingFinished: current number = " + String.valueOf(currentHeadlineNumber));
        validateText.setHint("Enter news headline " + String.valueOf(currentHeadlineNumber + 1));
    }
}