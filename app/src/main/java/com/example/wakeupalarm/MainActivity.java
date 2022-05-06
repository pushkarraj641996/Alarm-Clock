package com.example.wakeupalarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wakeupalarm.AlamListAdapter.AlarmListAdapter;
import com.example.wakeupalarm.DataObjects.Alarm;
import com.example.wakeupalarm.Fragments.BottomSheetAddAlarm;
import com.example.wakeupalarm.Fragments.BottomSheetEditAlarm;
import com.example.wakeupalarm.Interfaces.ItemOnClickListener;
import com.example.wakeupalarm.ViewModel.AlarmBroadcastReceiver;
import com.example.wakeupalarm.ViewModel.AlarmViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemOnClickListener{

    MaterialButton addAlarmButton;
    private AlarmViewModel mViewModel;
    RecyclerView recyclerView;
    AlarmListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addAlarmButton = findViewById(R.id.openBottomSheet);
        recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new AlarmListAdapter(this, MainActivity.this);

        mViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        mViewModel.getListOfAlarms().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                mAdapter.addAlarm(alarms);
            }
        });

        mViewModel.getCurrentTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                mAdapter.updateRemainingTime();
            }
        });
        if (addAlarmButton != null) {
            addAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomSheetAddAlarm addAlarmFragment = new BottomSheetAddAlarm();
                    addAlarmFragment.show(getSupportFragmentManager(), "Bottom_sheet");
                }
            });
        }

        if (recyclerView != null) {
            recyclerView.setLayoutManager( new LinearLayoutManager(MainActivity.this));
        }
        if (mAdapter != null && recyclerView != null) {
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onEditAlarmClick(int position) {
        BottomSheetEditAlarm editAlarm = new BottomSheetEditAlarm();
        mViewModel.setCurrentSelectedViewPosition( mViewModel.getListOfAlarms().getValue().get(position) );
        editAlarm.show(getSupportFragmentManager(), "edit_alarm_fragment");
    }

    @Override
    public void onAlarmToggleClickListener(int position) {
        Alarm editAlarm = mViewModel.getListOfAlarms().getValue().get(position);
        if ( !editAlarm.isAlarmActive() ) {
            editAlarm.setAlarmActive(true);
        }
        else {
            editAlarm.setAlarmActive(false);
        }
        PendingIntent newIntent = PendingIntent.getBroadcast(this,
                editAlarm.getNotificationId(),
                new Intent(this, AlarmBroadcastReceiver.class)
                        .putExtra("NOTIFICATION_ID" , editAlarm.getNotificationId())
                        .putExtra("ALARM_NAME" , editAlarm.getName()),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mViewModel.onUpdateAlarm(editAlarm.getNotificationId(), newIntent, editAlarm.isAlarmActive(), editAlarm.getName(), editAlarm.getAlarmType());
    }
}