package com.example.wakeupalarm.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wakeupalarm.ViewModel.AlarmViewModel;

import java.util.Calendar;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePicker";
    AlarmViewModel fragmentViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        fragmentViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);
        return new TimePickerDialog(getActivity(),this,hour,min,false);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
        Log.d(TAG, "onDateSet: " + String.valueOf(hour) + ":" + String.valueOf(minute));
        fragmentViewModel.onTimeSelected(hour, minute);
    }
}
