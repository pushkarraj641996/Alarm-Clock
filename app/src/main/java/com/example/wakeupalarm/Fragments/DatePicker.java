package com.example.wakeupalarm.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wakeupalarm.ViewModel.AlarmViewModel;

import java.util.Calendar;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "DatePicker";

    private AlarmViewModel fragmentViewModel;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        fragmentViewModel = new ViewModelProvider(requireActivity()).get(AlarmViewModel.class);
        return new DatePickerDialog(getActivity(),this,year,month,date);
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int date) {
        Log.d(TAG, "onDateSet: " + String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(date));
        fragmentViewModel.onDateSelected(year, month, date);
    }
}
