package com.example.wakeupalarm.AlamListAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wakeupalarm.Interfaces.ItemOnClickListener;
import com.example.wakeupalarm.R;


public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView description;
    TextView timeRemaining;
    TextView timeTypography;
    ItemOnClickListener listener;
    TextView alarmName;
    ImageView alarmToggle;
    TextView isAlarmActive;

    public AlarmViewHolder(@NonNull View itemView, final ItemOnClickListener listener) {
        super(itemView);

        description = itemView.findViewById(R.id.description);
        timeRemaining = itemView.findViewById(R.id.timeDisplay);
        timeTypography = itemView.findViewById(R.id.time_typography);
        alarmName = itemView.findViewById(R.id.alarm_name_display);
        alarmToggle = itemView.findViewById(R.id.alarmToggle);
        isAlarmActive = itemView.findViewById(R.id.isAlarmActive);

        this.listener = listener;

        itemView.setOnClickListener(this);

        alarmToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAlarmToggleClickListener(getAdapterPosition());
            }
        });
    }

    @Override
    public void onClick(View view) {
        listener.onEditAlarmClick(getAdapterPosition());
    }

}
