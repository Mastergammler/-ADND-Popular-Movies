package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.themoviedb.api.ImageSize;
import com.udacity.popularmovies.themoviedb.api.MovieDbUrlBuilder;

public class DetailActivity extends AppCompatActivity{


    //---------------
    //  Constants
    //---------------

    public static final String TITLE_KEY = "title";
    public static final String OVERVIEW_KEY = "overview";
    public static final String IMAGE_PATH_KEY = "image-path";
    public static final String RATING_KEY = "rating";
    public static final String RELEASE_KEY = "release";
    public static final String RUNTIME_KEY = "runtime";

    //---------------
    //  Members
    //---------------

    private TextView mTitleTextView;
    private TextView mRatingTextView;
    private TextView mPlotSynopsisTextView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;
    private ImageView mPosterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mRatingTextView = findViewById(R.id.tv_rating_average);
        mPlotSynopsisTextView = findViewById(R.id.tv_plot_synopsis);
        mReleaseDateTextView = findViewById(R.id.tv_release_year);
        mPosterView = findViewById(R.id.iv_movie_poster);
        mRuntimeTextView = findViewById(R.id.tv_runtime);

        setViewValues();
    }

    private void setViewValues() {
        Intent intent = getIntent();

        mTitleTextView.setText(getStringExtra(intent, TITLE_KEY));
        mReleaseDateTextView.setText(getStringExtra(intent, RELEASE_KEY));
        mPlotSynopsisTextView.setText(getStringExtra(intent, OVERVIEW_KEY));
        mRatingTextView.setText(String.valueOf(getFloatExtra(intent, RATING_KEY)));
        mRuntimeTextView.setText(String.valueOf(getIntExtra(intent, RUNTIME_KEY)) + " min");

        Uri uri = MovieDbUrlBuilder.getMovieImageURL(getStringExtra(intent, IMAGE_PATH_KEY), ImageSize.IMAGE_BIG);
        Picasso.get().load(uri).into(mPosterView);
    }

    private String getStringExtra(Intent intent, String key) {
        if(intent.hasExtra(key))
        {
            return intent.getStringExtra(key);
        }
        return "";
    }

    private int getIntExtra(Intent intent, String key)
    {
        if(intent.hasExtra(key))
        {
            return intent.getIntExtra(key,0);
        }
        return 0;
    }

    private float getFloatExtra(Intent intent, String key)
    {
        if(intent.hasExtra(key))
        {
            return intent.getFloatExtra(key,0);
        }
        return 0;
    }
}
