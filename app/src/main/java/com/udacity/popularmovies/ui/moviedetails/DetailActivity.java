package com.udacity.popularmovies.ui.moviedetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.popularmovies.MainActivity;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.favouritesdb.AppExecutors;
import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieData;
import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;
import com.udacity.popularmovies.themoviedb.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;
import com.udacity.popularmovies.themoviedb.api.data.MovieReview;
import com.udacity.popularmovies.ui.MainViewModel;

import java.io.IOException;

/**
 * Activity for the detail view of a specific movies
 * Shows the picture as well as additional information
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails>
{
    //---------------
    //  Constants
    //---------------

    private static final String SCROLL_VIEW_STATE = "scroll-view-state";
    public static final String MOVIE_CONTENT_KEY = "movie-content";
    public static final String MOVIE_ID_KEY = "movie-id";
    protected static final String LOADER_PARAM = "loader-param-movie-id";
    private static final int MOVIE_DETAIL_LOADER_ID = 483748;
    private static final String TAG = DetailActivity.class.getSimpleName();

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

        if(intent.hasExtra(MOVIE_CONTENT_KEY))
        {
            MovieInfo info = (MovieInfo) intent.getSerializableExtra(MOVIE_CONTENT_KEY);
            loadDataFromSerializable(info);
        }
        else if(intent.hasExtra(MOVIE_ID_KEY))
        {
;           int movieId =  intent.getIntExtra(MOVIE_ID_KEY,0);
            loadDataFromViewModel(movieId);
        }
        else
        {
            Log.w(TAG,"An error occurred while loading the data from the intent");
        }
    }


    private void loadDataFromSerializable(MovieInfo info)
    {
        mTitleTextView.setText(info.title);
        mReleaseDateTextView.setText(formatDateText(info.release_date));
        mPlotSynopsisTextView.setText(info.overview);
        mRatingTextView.setText(formatRatingText(info.vote_average));
        mRuntimeTextView.setText(parseMovieLengthText(info.runtime));

        Bundle loaderArgs = new Bundle();
        loaderArgs.putInt(LOADER_PARAM,info.id);
        LoaderManager.getInstance(this).initLoader(MOVIE_DETAIL_LOADER_ID,loaderArgs,this);

        Uri uri = mMovieApi.getMoviePoster(info.poster_path, ImageSize.IMAGE_BIG);
        Picasso.get().load(uri).placeholder(R.drawable.placeholder).into(mPosterView);
    }

    private void loadDataFromViewModel(int movieId)
    {
        MainViewModel mainVM = new ViewModelProvider(this).get(MainViewModel.class);
        FullMovieInfo movieInfo = mainVM.getInfoFor(movieId);

        if(movieInfo == null)
        {
            Log.w(TAG,"Movie details could not be obtained from the view model");
            return;
        }

        MovieData data = movieInfo.movieData;
        mTitleTextView.setText(data.getTitle());
        mReleaseDateTextView.setText(formatDateText(data.getRelease_date()));

        Uri uri = mMovieApi.getMoviePoster(data.getMovie_poster_path(), ImageSize.IMAGE_BIG);
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

    //---------------------
    //  Background Loader
    //--------------------

    @NonNull
    @Override
    public Loader<MovieDetails> onCreateLoader(int id, final @Nullable Bundle args) {
        return new AsyncTaskLoader<MovieDetails>(this){

            private MovieDetails movieDetailsCache;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(movieDetailsCache != null) deliverResult(movieDetailsCache);
                else
                {
                    forceLoad();
                    // TODO: 07.04.2020 show loading indicator
                }
            }

            @Nullable
            @Override
            public MovieDetails loadInBackground()
            {
                if(args == null)
                {
                    Log.w(this.getClass().getSimpleName(),"No parameter specified for the loader!");
                    return null;
                }
                int id = args.getInt(LOADER_PARAM);
                if(id == 0)
                {
                    Log.w(this.getClass().getSimpleName(),"Invalid id specified! Unable to load movie details!");
                    return null;
                }

                MovieInfo info = mMovieApi.getMovieDetails(id);
                Uri[] trailerUrls = mMovieApi.getVideoLinks(id,true);
                MovieReview[] reviews = mMovieApi.getMovieReviews(id);

                return new MovieDetails(info,reviews,trailerUrls);
            }

            @Override
            public void deliverResult(@Nullable MovieDetails data) {
                movieDetailsCache = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieDetails> loader, final MovieDetails data)
    {
        TextView tv = findViewById(R.id.debug_view);
        tv.setText(data.movieInfo.tagline != null ? data.movieInfo.tagline : "");
        tv.append("\n" + data.movieInfo.status);
        tv.append("\n" + data.movieInfo.runtime);
        tv.append("\n" + data.movieInfo.revenue + "$$$$$");
        for(Uri uri : data.movieTrailers)
        {
            tv.append("\n" + uri.toString());
        }
        for(MovieReview rev : data.movieReviews)
        {
            tv.append("\n" + rev.author + " : " + rev.getContentPreview());
        }

        AppExecutors.getInstance().runOnDiskIOThread(
                new Runnable(){
                    @Override
                    public void run()
                    {
                        Uri uri = mMovieApi.getMoviePoster(data.movieInfo.poster_path, ImageSize.IMAGE_MEDIUM);
                        Bitmap bitmap = null;
                        try
                        {
                            bitmap = Picasso.get().load(uri).get();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        FavouritesDatabase db = FavouritesDatabase.getInstance(DetailActivity.this);
                        db.favouritesDao().saveMovieAsFavourite(
                                new MovieData(data.movieInfo,bitmap)
                        );
                    }
                }
        );
    }



    @Override
    public void onLoaderReset(@NonNull Loader<MovieDetails> loader) {}
}
