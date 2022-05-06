package com.example.wakeupalarm.Fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wakeupalarm.DataObjects.Alarm;
import com.example.wakeupalarm.DataObjects.DateTime;
import com.example.wakeupalarm.DataObjects.FormatTime;
import com.example.wakeupalarm.R;
import com.example.wakeupalarm.ViewModel.AlarmBroadcastReceiver;
import com.example.wakeupalarm.ViewModel.AlarmViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class BottomSheetEditAlarm extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetEditAlarm";

    private AlarmViewModel viewModel;
    private TextView displayTime;
    private TextView typography;
    private Context context;
    private RadioGroup radioGroup;

    private SwitchMaterial toggleAlarm;
    private ImageView deleteAlarm;
    private MaterialButton selectTime;
    private MaterialButton saveAlarm;
    private MaterialButton cancel;
    MaterialTimePicker timePicker;
    private TextInputEditText alarmName;
    private RadioGroup alarmRadioGroup;

    int notificationID;
    Alarm oldAlarm;
    private String alarmType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_edit_alarm, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);
        oldAlarm = viewModel.getCurrentSelectedViewAlarm();
        notificationID = oldAlarm.getNotificationId();
        alarmType = oldAlarm.getAlarmType();
        deleteAlarm  = view.findViewById(R.id.delete_alarm);
        toggleAlarm = view.findViewById(R.id.alarm_switch);
        alarmRadioGroup = view.findViewById(R.id.alarmTypeEdit);
        saveAlarm = view.findViewById(R.id.edit_alarm);
        cancel = view.findViewById(R.id.cancel);
        selectTime = view.findViewById(R.id.select_time);
        displayTime = view.findViewById(R.id.time_display);
        typography = view.findViewById(R.id.edit_alarm_typography);
        alarmName = view.findViewById(R.id.editAlarmText);
        radioGroup = view.findViewById(R.id.alarmTypeEdit);

        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(oldAlarm.getDateTime().getHour())
                .setMinute(oldAlarm.getDateTime().getMin())
                .build();

        if ( alarmType.equals( getString(R.string.normal_alarm_type) ) ) {
            radioGroup.check(R.id.normal);
        }
        else {
            radioGroup.check(R.id.forceful);
        }

        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.onDeleteAlarm( notificationID , null );
                dismiss();
            }
        });

        viewModel.onTimeSelected( oldAlarm.getDateTime().getHour(), oldAlarm.getDateTime().getMin() );
        if (oldAlarm.getName() != null) {
            alarmName.setText(oldAlarm.getName());
        }

        viewModel.isAlarmActive(notificationID).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isActive) {
                if (isActive) {
                    toggleAlarm.setChecked(true);
                }
                else {
                    toggleAlarm.setChecked(false);
                }
            }
        });

        viewModel.getSelectedDateTime().observe(requireActivity(), new Observer<DateTime>() {
            @Override
            public void onChanged(DateTime dateTime) {
                displayTime.setText(FormatTime.returnTime(dateTime));
                typography.setText(FormatTime.getTypography(dateTime));
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getChildFragmentManager(), "Time_Picker");
            }
        });

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.onTimeSelected( timePicker.getHour(), timePicker.getMinute() );
            }
        });

        toggleAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //enableAlarm
                    Toast.makeText(requireActivity(), "Alarm on", Toast.LENGTH_SHORT).show();
                }
                else {
                    //disable alarm
                    Toast.makeText(requireActivity(), "Alarm off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save button clicked

                PendingIntent newIntent = null;
                Alarm editAlarm = new Alarm(oldAlarm.getDateTime(), notificationID, toggleAlarm.isChecked(), alarmName.getText().toString(), alarmType);
                newIntent = PendingIntent.getBroadcast(requireActivity(),
                        notificationID,
                        new Intent(requireActivity(), AlarmBroadcastReceiver.class)
                                .putExtra("NOTIFICATION_ID" , notificationID)
                                .putExtra("ALARM_TYPE", alarmType)
                                .putExtra("ALARM_NAME" , alarmName.getText().toString()),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                viewModel.onUpdateAlarm(notificationID, newIntent, toggleAlarm.isChecked(), alarmName.getText().toString(), alarmType);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //cancel button clicked
                dismiss();
            }
        });

        alarmRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ( i == R.id.normal ) {
                    Log.d(TAG, "onCheckedChanged: normal alarm selected");
                    alarmType = getString(R.string.normal_alarm_type);
                }
                else if ( i == R.id.forceful ) {
                    Log.d(TAG, "onCheckedChanged: forceful alarm selected");
                    alarmType = getString(R.string.forceful_alarm_type);
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d(TAG, "onAttach: Context = " + context.toString());
    }
}