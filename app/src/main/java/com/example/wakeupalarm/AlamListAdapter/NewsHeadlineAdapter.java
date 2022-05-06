package com.example.wakeupalarm.AlamListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wakeupalarm.R;

import java.util.List;

public class NewsHeadlineAdapter extends ArrayAdapter<String> {

    private Context context;
    private int mResource;
    private List<String> headlines;

    public NewsHeadlineAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.mResource = resource;
        this.context = context;
        this.headlines = objects;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(mResource, viewGroup, false);

        TextView newsHeadline = (TextView) view.findViewById(R.id.headline);
        newsHeadline.setText( headlines.get(position) );
        return view;
    }

    public List<String> getHeadlines() {
        return headlines;
    }
}
