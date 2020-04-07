package com.udacity.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.popularmovies.themoviedb.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

/**
 * Activity for the detail view of a specific movies
 * Shows the picture as well as additional information
 */
public class DetailActivity extends AppCompatActivity
{
    //---------------
    //  Constants
    //---------------

    private static final String SCROLL_VIEW_STATE = "scroll-view-state";

    public static final String MOVIE_CONTENT_KEY = "movie-content";

    public static final String TITLE_KEY = "title";
    public static final String OVERVIEW_KEY = "overview";
    public static final String IMAGE_PATH_KEY = "image-path";
    public static final String RATING_KEY = "rating";
    public static final String RELEASE_KEY = "release";
    public static final String RUNTIME_KEY = "runtime";

    //---------------
    //  Members
    //---------------

    private ScrollView mContentView;
    private TextView mTitleTextView;
    private TextView mRatingTextView;
    private TextView mPlotSynopsisTextView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;
    private ImageView mPosterView;

    private IMovieDbApi mMovieApi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mContentView = findViewById(R.id.sv_detail_content);
        mTitleTextView = findViewById(R.id.tv_movie_title);
        mRatingTextView = findViewById(R.id.tv_rating_average);
        mPlotSynopsisTextView = findViewById(R.id.tv_plot_synopsis);
        mReleaseDateTextView = findViewById(R.id.tv_release_year);
        mPosterView = findViewById(R.id.iv_movie_poster);
        mRuntimeTextView = findViewById(R.id.tv_runtime);

        mMovieApi = new MovieApi();

        initViewValues();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_VIEW_STATE,mContentView.getTop());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mContentView.setTop(savedInstanceState.getInt(SCROLL_VIEW_STATE));
    }

    private void initViewValues()
    {
        Intent intent = getIntent();

        MovieInfo info = (MovieInfo) intent.getSerializableExtra(MOVIE_CONTENT_KEY);

        mTitleTextView.setText(info.title);
        mReleaseDateTextView.setText(formatDateText(info.release_date));
        mPlotSynopsisTextView.setText(info.overview);
        mRatingTextView.setText(formatRatingText(info.vote_average));
        mRuntimeTextView.setText(parseMovieLengthText(info.runtime));

        Uri uri = mMovieApi.getMoviePoster(info.poster_path, ImageSize.IMAGE_BIG);
        Picasso.get().load(uri).placeholder(R.drawable.placeholder).into(mPosterView);
    }

    //------------
    //  HELPERS
    //------------

    private String parseMovieLengthText(int movieLength)
    {
        if(movieLength == 0) return "";
        return movieLength + " min";
    }
    private String formatDateText(String dateString)
    {
        if(dateString == null) return "";
        String parsed = "";

        String[] date = dateString.split("-");
        if(date.length>1)
        {
            int monthNumber = Integer.parseInt(date[1]);
            parsed += parseMonth(monthNumber);
        }
        parsed += " ";
        parsed += date[0];

        return parsed;
    }
    private String parseMonth(int monthNumber)
    {
        switch(monthNumber)
        {
            default:return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
        }
    }
    private String formatRatingText(float rating)
    {
        if(rating == 0) return "?";

        if(rating >= 10)
            return String.valueOf((int)rating);
        else
            return String.valueOf(rating);
    }

    private String getStringExtra(Intent intent, String key)
    {
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
