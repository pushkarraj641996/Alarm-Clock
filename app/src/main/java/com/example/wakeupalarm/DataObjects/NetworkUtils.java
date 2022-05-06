package com.example.wakeupalarm.DataObjects;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";
    private static final String API_KEY_VAL = "765548e8d01948d8a0bde7161b07f61e";
    private final static String BASE_URL = "http://newsapi.org/v2/top-headlines?";
    private final static String COUNTRY = "country";
    private final static String APIKEY = "apiKey";

    static String getNewsFeed () {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = null;

        try {

            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(COUNTRY, "in")
                    .appendQueryParameter(APIKEY , API_KEY_VAL)
                    .build();

            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.connect();

            Log.d(TAG, "getNewsFeed: response Code = " + urlConnection.getResponseCode());
            Log.d(TAG, "getNewsFeed: response message  = " + urlConnection.getResponseMessage());
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader( new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }

            resultJson =builder.toString();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Log.d(TAG, "getNewsFeed: " + resultJson);
        return resultJson;
    }
}
