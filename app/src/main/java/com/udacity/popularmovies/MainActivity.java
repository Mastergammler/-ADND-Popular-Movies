package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
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

    private GridLayout mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.gl_main_view);
        new DiscoverMoviesTask().execute(DiscoveryMode.POPULAR_DESC);
    }

    private void loadImages(MovieCollection collection)
    {
        for(MovieInfo movie : collection.results)
        {
            ImageView iv = new ImageView(mGrid.getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER);
            Uri imageUri = MovieDbUrlBuilder.getMovieImageURL(movie.poster_path, ImageSize.IMAGE_BIG);
            Picasso.get().load(imageUri).into(iv);
            mGrid.addView(iv);
        }
    }

    //------------------
    //  LOAD JSON TASK
    //------------------

    class DiscoverMoviesTask extends AsyncTask<DiscoveryMode,Void,MovieCollection>
    {
        @Override
        protected MovieCollection doInBackground(DiscoveryMode... discoveryModes) {
            String jsonResult = null;
            URL url = getUrlForMode(discoveryModes[0]);
            try
            {
                jsonResult = NetworkingUtil.getResponseFromHttpRequest(url);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            // TODO: 30.03.2020 check for null
            MovieCollection coll = MovieCollection.parseJson(jsonResult);
            return coll;
        }

        @Override
        protected void onPostExecute(MovieCollection movieCollection) {
            loadImages(movieCollection);
        }

        private URL getUrlForMode(DiscoveryMode mode)
        {
            switch (mode)
            {
                case USER_RATING_DESC:
                    return MovieDbUrlBuilder.getMoviesByUserRatingURL();
                default: return MovieDbUrlBuilder.getMoviesByPopularityURL();
            }
        }
    }

}
