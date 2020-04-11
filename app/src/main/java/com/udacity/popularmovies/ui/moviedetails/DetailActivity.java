package com.udacity.popularmovies.ui.moviedetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.favouritesdb.AppExecutors;
import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieCover;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieData;
import com.udacity.popularmovies.favouritesdb.Entitites.ReviewData;
import com.udacity.popularmovies.favouritesdb.Entitites.VideoData;
import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;
import com.udacity.popularmovies.themoviedb.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;
import com.udacity.popularmovies.themoviedb.api.data.MovieReview;
import com.udacity.popularmovies.themoviedb.api.data.VideoInfo;

import java.io.IOException;

/**
 * Activity for the detail view of a specific movies
 * Shows the picture as well as additional information
 */
public class DetailActivity extends AppCompatActivity
{
    //*****************
    //**  CONSTANTS  **
    //*****************

    private static final int MOVIE_DETAIL_LOADER_ID = 483748;

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String SCROLL_VIEW_STATE = "scroll-view-state";

    public static final String MOVIE_CONTENT_KEY = "movie-content";
    public static final String MOVIE_ID_KEY = "movie-id";

    //***************
    //**  MEMBERS  **
    //***************

    private boolean mMovieLiked = false;
    private int mMovieId;

    private TextView mTitleTextView;
    private TextView mRatingTextView;
    private TextView mPlotSynopsisTextView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;

    private ScrollView mContentView;
    private ImageView mPosterView;

    private IMovieDbApi mMovieApi;
    private MovieDetailLoaderCallback mLoaderCallback;

    //*****************
    //**  LIFECYCLE  **
    //*****************

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
        TextView debugView = findViewById(R.id.debug_view);

        mMovieApi = new MovieApi();
        mLoaderCallback = new MovieDetailLoaderCallback(this,debugView);

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

    //************
    //**  MENU  **
    //************

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.detail_activity,menu);
        MenuItem item = menu.findItem(R.id.action_like);
        String title = mMovieLiked ? getString(R.string.action_unlike) : getString(R.string.action_like);
        item.setTitle(title);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_like:
                Bundle loaderArgs = new Bundle();
                mMovieLiked = !mMovieLiked;
                loaderArgs.putInt(MovieDetailLoaderCallback.LOADER_PARAM,mMovieId);
                // TODO: 09.04.2020 case for delete
                loaderArgs.putBoolean(MovieDetailLoaderCallback.LOADER_PARAM_SAVE_OR_DELETE,mMovieLiked);
                LoaderManager.getInstance(this).restartLoader(MOVIE_DETAIL_LOADER_ID,loaderArgs,mLoaderCallback);
                String title = mMovieLiked ? getString(R.string.action_unlike) : getString(R.string.action_like);
                item.setTitle(title);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    //***************
    //**  HELPERS  **
    //***************

    private void initViewValues()
    {
        Intent intent = getIntent();

        // TODO: 09.04.2020 this is not how liked is determined, this just represents the context we come from
        if(intent.hasExtra(MOVIE_CONTENT_KEY))
        {
            MovieInfo info = (MovieInfo) intent.getSerializableExtra(MOVIE_CONTENT_KEY);
            loadDataFromSerializable(info);
            mMovieId = info.id;
        }
        else if(intent.hasExtra(MOVIE_ID_KEY))
        {
            int movieId =  intent.getIntExtra(MOVIE_ID_KEY,0);
            loadDataFromViewModel(movieId);
            mMovieId = movieId;
            mMovieLiked = true;
        }
        else
        {
            Log.w(TAG,"Unable to load movie details. Intent doesn't seem to have the required extras");
        }
    }
    private void loadDataFromSerializable(MovieInfo info)
    {
        FavouritesDatabase db = FavouritesDatabase.getInstance(getApplicationContext());
        DetailViewModelFactory factory = new DetailViewModelFactory(db,info.id);
        final DetailViewModel vm = new ViewModelProvider(DetailActivity.this,factory).get(DetailViewModel.class);
        vm.getMovieData().observe(this, new Observer<FullMovieInfo>(){
            @Override
            public void onChanged(FullMovieInfo fullMovieInfo) {
                vm.getMovieData().removeObserver(this);

                if(fullMovieInfo == null)
                {
                    mMovieLiked = false;
                }
                else
                {
                    mMovieLiked = true;
                }
                invalidateOptionsMenu();
            }
        });

        mTitleTextView.setText(info.title);
        mReleaseDateTextView.setText(formatDateText(info.release_date));
        mPlotSynopsisTextView.setText(info.overview);
        mRatingTextView.setText(formatRatingText(info.vote_average));
        mRuntimeTextView.setText(parseMovieLengthText(info.runtime));

        Bundle loaderArgs = new Bundle();
        loaderArgs.putInt(MovieDetailLoaderCallback.LOADER_PARAM,info.id);
        LoaderManager.getInstance(this).initLoader(MOVIE_DETAIL_LOADER_ID,loaderArgs,mLoaderCallback);

        Uri uri = mMovieApi.getMoviePoster(info.poster_path, ImageSize.IMAGE_BIG);
        Picasso.get().load(uri).placeholder(R.drawable.placeholder).into(mPosterView);
    }
    private void loadDataFromViewModel(int movieId)
    {
        FavouritesDatabase db = FavouritesDatabase.getInstance(getApplicationContext());
        DetailViewModelFactory factory = new DetailViewModelFactory(db,movieId);
        final DetailViewModel vm = new ViewModelProvider(this,factory).get(DetailViewModel.class);
        vm.getMovieData().observe(this, new Observer<FullMovieInfo>(){
            @Override
            public void onChanged(FullMovieInfo fullMovieInfo)
            {
                vm.getMovieData().removeObserver(this);
                MovieData data = fullMovieInfo.movieData;
                mTitleTextView.setText(data.getTitle());
                mReleaseDateTextView.setText(formatDateText(data.getRelease_date()));

                Uri uri = mMovieApi.getMoviePoster(data.getMovie_poster_path(), ImageSize.IMAGE_BIG);
                Drawable w185 = new BitmapDrawable(getResources(),fullMovieInfo.cover.getMoviePosterW185());
                Picasso.get().load(uri)
                        .placeholder(R.drawable.placeholder)
                        .error(w185)
                        .into(mPosterView);
            }
        });
    }

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
}
