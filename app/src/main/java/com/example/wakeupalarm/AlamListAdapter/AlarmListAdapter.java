package com.example.wakeupalarm.AlamListAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeupalarm.DataObjects.Alarm;
import com.example.wakeupalarm.DataObjects.DateTime;
import com.example.wakeupalarm.DataObjects.FormatTime;
import com.example.wakeupalarm.Interfaces.ItemOnClickListener;
import com.example.wakeupalarm.R;

import java.util.Calendar;
import java.util.List;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private static final String TAG = "AlarmListAdapter";

    List<Alarm> alarmList;
    DateTime currentDateTime;
    ItemOnClickListener listener;
    Context context;

    public AlarmListAdapter(ItemOnClickListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_view_holder, parent, false);
        return new AlarmViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        currentDateTime = alarmList.get(position).getDateTime();
        holder.description.setText(FormatTime.returnTime(currentDateTime));
        holder.timeTypography.setText(FormatTime.getTypography(currentDateTime).toUpperCase());
        holder.alarmName.setText(alarmList.get(position).getName());

        if ( alarmList.get(position).isAlarmActive() ) {
            holder.alarmToggle.setImageResource(R.drawable.ic__alarm_on);
            holder.alarmToggle.setColorFilter(ContextCompat.getColor(context, R.color.alarm_icon_status));

            holder.isAlarmActive.setText(R.string.alarm_active);
            holder.isAlarmActive.setTextColor(ContextCompat.getColor(context, R.color.alarm_icon_status));
            long minutes = getRemainingTime(currentDateTime);
            Log.d(TAG, "onBindViewHolder: returned time = " + minutes);
            String remainingTime = minutes / 60 +
                    " hours " + minutes % 60 + " minutes remaining";
            holder.timeRemaining.setText(remainingTime);
            Log.d(TAG, "onBindViewHolder: invoked + " + minutes);
        }

        else {
            holder.alarmToggle.setImageResource(R.drawable.ic__alarm_off);
            holder.alarmToggle.setColorFilter(ContextCompat.getColor(context, R.color.disabled_bright));

            holder.isAlarmActive.setText(R.string.alarm_inactive);
            holder.isAlarmActive.setTextColor(ContextCompat.getColor(context, R.color.disabled_bright));
            holder.timeRemaining.setText(null);
        }
    }

    @Override
    public int getItemCount() {
        if (alarmList != null) {
            return alarmList.size();
        }
        else {
            return 0;
        }
    }

    public void addAlarm ( List<Alarm> alarmList ) {
        this.alarmList = alarmList;
        notifyDataSetChanged();
    }

    private Long getRemainingTime ( DateTime dateTime ) {
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR , dateTime.getHour());
        target.set(Calendar.MINUTE , dateTime.getMin());
        Long currentMins = Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "getRemainingTime: currentMins = " + currentMins);
        Log.d(TAG, "getRemainingTime: targetMins = " + target.getTimeInMillis());

        return (((target.getTimeInMillis() - currentMins) / 60000) < 0) ?
                (1440 + (target.getTimeInMillis() - currentMins) / 60000):
                ((target.getTimeInMillis() - currentMins) / 60000);
    }
    
    public void updateRemainingTime () {
        Log.d(TAG, "updateRemainingTime: called");
        notifyDataSetChanged();
    }
}
