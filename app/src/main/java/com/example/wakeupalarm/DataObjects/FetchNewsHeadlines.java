package com.example.wakeupalarm.DataObjects;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.wakeupalarm.AlamListAdapter.NewsHeadlineAdapter;
import com.example.wakeupalarm.Interfaces.TaskDone;
import com.example.wakeupalarm.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FetchNewsHeadlines extends AsyncTask<Void, Void, String> {
    private static final String TAG = "FetchNewsHeadlines";

    private Context context;
    private WeakReference<ListView> listView;
    private WeakReference<ProgressBar> progressBar;
    private TaskDone listener;

    public FetchNewsHeadlines(Context context, ListView listView, ProgressBar progressBar, TaskDone taskDone) {
        this.context = context;
        this.listView = new WeakReference<>(listView);
        this.progressBar = new WeakReference<>(progressBar);
        this.listener = taskDone;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... voids) {
        return NetworkUtils.getNewsFeed();
    }

    @Override
    protected void onPostExecute(String s) {
        List<String> headlines = new ArrayList<>();
        super.onPostExecute(s);
        progressBar.get().setVisibility(View.INVISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("articles");

            int i = 0;
            while ( i < jsonArray.length() && i < 5 ) {
                JSONObject news = jsonArray.getJSONObject(i);
                Log.d(TAG, "onPostExecute: Adding title = " + news.getString("title"));
                headlines.add( news.getString("title").substring( 0, news.getString("title").indexOf("-") ).trim() );
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        NewsHeadlineAdapter adapter = new NewsHeadlineAdapter(context, R.layout.news_headline, headlines);
        listView.get().setAdapter(adapter);
        listener.onLoadingFinished(headlines);
    }
}
