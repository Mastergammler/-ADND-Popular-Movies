package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.udacity.popularmovies.networking.NetworkingUtil;
import com.udacity.popularmovies.themoviedb.api.MovieDbUrlBuilder;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv_default);

        try
        {
            String jsonResult = NetworkingUtil.getResponseFromHttpRequest(MovieDbUrlBuilder.getMoviesByPopularityURL());
            tv.setText(jsonResult);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
