package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.udacity.popularmovies.networking.NetworkingUtil;
import com.udacity.popularmovies.themoviedb.api.MovieDbUrlBuilder;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = findViewById(R.id.tv_default);

        AsyncTask task = new AsyncTask<URL, Void, String>(){
            @Override
            protected void onPreExecute() {
                tv.setText("Loading Data ...");
            }

            @Override
            protected String doInBackground(URL... urls) {
                String jsonResult = null;
                try
                {
                    jsonResult = NetworkingUtil.getResponseFromHttpRequest(urls[0]);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return jsonResult;
            }

            @Override
            protected void onPostExecute(String s) {
                tv.setText(s);
            }
        }.execute(MovieDbUrlBuilder.getMoviesByPopularityURL());
    }
}
