package com.example.wakeupalarm.Fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wakeupalarm.DataObjects.FormatTime;
import com.example.wakeupalarm.ViewModel.AlarmBroadcastReceiver;
import com.example.wakeupalarm.DataObjects.DateTime;
import com.example.wakeupalarm.ViewModel.AlarmViewModel;
import com.example.wakeupalarm.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class BottomSheetAddAlarm extends BottomSheetDialogFragment {
    private static final String TAG = "BottomSheetFragment";

    private AlarmViewModel viewModel;
    private Context ctx;
    private MaterialButton selectTime;
    private MaterialButton addAlarm;
    private MaterialButton cancel;

    private TextView displayTime;
    private MaterialTimePicker timePicker;
    private TextInputEditText alarmName;
    private TextView typography;
    private String alarmType;
    private RadioGroup alarmRadioButton;

    private final DateTime currentDateTime = new DateTime();
    private Intent broadcastIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_add_alarm, container, false);
        if (view != null) {
            Log.d(TAG, "onCreateView: View created successfully");
            return view;
        } else {
            Log.d(TAG, "onCreateView: Error creating view");
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: Called");

        super.onViewCreated(view, savedInstanceState);

        if (broadcastIntent == null) {
            broadcastIntent = new Intent(getActivity(), AlarmBroadcastReceiver.class);
        }

        selectTime = view.findViewById(R.id.select_time);
        addAlarm = view.findViewById(R.id.add_alarm);
        cancel = view.findViewById(R.id.cancel);
        displayTime = view.findViewById(R.id.displayTime);
        alarmRadioButton = view.findViewById(R.id.alarmType);
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(Calendar.getInstance().getTime().getHours())
                .setMinute(Calendar.getInstance().getTime().getMinutes())
                .build();
        alarmName = view.findViewById(R.id.alarm_name_add);
        typography = view.findViewById(R.id.add_alarm_typography);

        viewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);

        viewModel.getSelectedDateTime().observe(this, new Observer<DateTime>() {
            @Override
            public void onChanged(DateTime dateTime) {
                displayTime.setText(FormatTime.returnTime(dateTime));
                typography.setText(FormatTime.getTypography(dateTime));
                currentDateTime.setTime(dateTime.getHour(), dateTime.getMin());
            }
        });

        addButtonListeners();
    }

    public void addButtonListeners() {
        Log.d(TAG, "addButtonListeners: invoked:");

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getChildFragmentManager(), "Time_Picker");
            }
        });

        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Time Selected = " + timePicker.getHour() + ":" + timePicker.getMinute());
                viewModel.onTimeSelected(timePicker.getHour(), timePicker.getMinute());
                //timePicker = null;
            }
        });

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                broadcastIntent.putExtra("NOTIFICATION_ID", viewModel.getLastNotificationID());
                broadcastIntent.putExtra("ALARM_NAME" , alarmName.getText().toString());
                broadcastIntent.putExtra("ALARM_TYPE", alarmType);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(),
                        viewModel.getLastNotificationID(),
                        broadcastIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                viewModel.addAlarm(pendingIntent, alarmName.getText().toString(), alarmType);
                dismiss();
                Toast.makeText(getActivity(), "Alarm set", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        alarmRadioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        this.ctx = context;
    }
}
