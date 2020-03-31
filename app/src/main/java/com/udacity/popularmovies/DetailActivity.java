package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.themoviedb.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;

/**
 * Activity for the detail view of a specific movies
 * Shows the picture as well as additional information
 */
public class DetailActivity extends AppCompatActivity
{
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

    private IMovieDbApi mMovieApi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = findViewById(R.id.tv_movie_title);
        mRatingTextView = findViewById(R.id.tv_rating_average);
        mPlotSynopsisTextView = findViewById(R.id.tv_plot_synopsis);
        mReleaseDateTextView = findViewById(R.id.tv_release_year);
        mPosterView = findViewById(R.id.iv_movie_poster);
        mRuntimeTextView = findViewById(R.id.tv_runtime);

        mMovieApi = new MovieApi();

        initViewValues();
    }

    private void initViewValues()
    {
        Intent intent = getIntent();

        mTitleTextView.setText(getStringExtra(intent, TITLE_KEY));
        mReleaseDateTextView.setText(formatDateText(getStringExtra(intent, RELEASE_KEY)));
        mPlotSynopsisTextView.setText(getStringExtra(intent, OVERVIEW_KEY));
        mRatingTextView.setText(formatRatingText(getFloatExtra(intent, RATING_KEY)));
        mRuntimeTextView.setText(parseMovieLengthText(getIntExtra(intent, RUNTIME_KEY)));

        Uri uri = mMovieApi.getMoviePoster(getStringExtra(intent, IMAGE_PATH_KEY), ImageSize.IMAGE_MEDIUM);
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
