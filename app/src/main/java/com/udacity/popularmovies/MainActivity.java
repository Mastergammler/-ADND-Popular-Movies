package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.networking.NetworkingUtil;
import com.udacity.popularmovies.themoviedb.api.ImageSize;
import com.udacity.popularmovies.themoviedb.api.MovieCollection;
import com.udacity.popularmovies.themoviedb.api.MovieDbUrlBuilder;
import com.udacity.popularmovies.themoviedb.api.MovieInfo;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity{

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = findViewById(R.id.tv_default);
        final ImageView iv = findViewById(R.id.iv_poster);

        AsyncTask task = new AsyncTask<URL, Void, MovieInfo>(){
            @Override
            protected void onPreExecute() {
                tv.setText("Loading Data ...");
            }

            @Override
            protected MovieInfo doInBackground(URL... urls) {
                String jsonResult = null;
                MovieInfo info = null;
                try
                {
                    jsonResult = NetworkingUtil.getResponseFromHttpRequest(urls[0]);
                    MovieCollection coll = MovieCollection.parseJson(jsonResult);
                    info = coll.results[10];
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    jsonResult = "An error occurred";
                }
                return info;
            }

            @Override
            protected void onPostExecute(MovieInfo s) {
                tv.setText(s.title);
                Uri imageUri = MovieDbUrlBuilder.getMovieImageURL(s.poster_path,ImageSize.IMAGE_BIG);
                Log.i(MainActivity.class.getSimpleName(),"Image Uri: " + imageUri.toString());
                Picasso.get().load(imageUri).into(iv);
            }
        }.execute(MovieDbUrlBuilder.getMoviesByPopularityURL());
    }
}
