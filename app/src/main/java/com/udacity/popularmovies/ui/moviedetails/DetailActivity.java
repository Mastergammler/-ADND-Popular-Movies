package com.udacity.popularmovies.ui.moviedetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;

import android.content.Intent;
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
import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;
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

        loadAvailableMovieDetails();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_VIEW_STATE,mContentView.getScrollY());
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mContentView.setScrollY(savedInstanceState.getInt(SCROLL_VIEW_STATE));
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
        updateMenuText(item);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_like:
                mMovieLiked = !mMovieLiked;
                updateMenuText(item);
                restartLoader();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    //***************
    //**  HELPERS  **
    //***************

    private void loadAvailableMovieDetails()
    {
        Intent intent = getIntent();

        if(intent.hasExtra(MOVIE_CONTENT_KEY))
        {
            MovieInfo info = (MovieInfo) intent.getSerializableExtra(MOVIE_CONTENT_KEY);
            mMovieId = info.id;

            loadDataFromSerializable(info);
        }
        else if(intent.hasExtra(MOVIE_ID_KEY))
        {
            int movieId =  intent.getIntExtra(MOVIE_ID_KEY,0);
            mMovieId = movieId;
            mMovieLiked = true;

            loadDataFromViewModel(movieId);
        }
        else
        {
            Log.w(TAG,"Unable to load movie details. Intent doesn't seem to have the required extras");
        }
    }
    private void loadDataFromSerializable(MovieInfo info)
    {
        updateMovieLikedState(info.id);

        mTitleTextView.setText(info.title);
        mReleaseDateTextView.setText(DisplayUtils.formatDateText(info.release_date));
        mPlotSynopsisTextView.setText(info.overview);
        mRatingTextView.setText(DisplayUtils.formatRatingText(info.vote_average));
        mRuntimeTextView.setText(DisplayUtils.parseMovieLengthText(info.runtime));

        initLoader(info.id);
        loadMovieCover(info.poster_path);
    }
    private void updateMovieLikedState(int movieId)
    {
        FavouritesDatabase db = FavouritesDatabase.getInstance(getApplicationContext());
        DetailViewModelFactory factory = new DetailViewModelFactory(db,movieId);
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
    }
    private void initLoader(int movieId)
    {
        Bundle loaderArgs = new Bundle();
        loaderArgs.putInt(MovieDetailLoaderCallback.LOADER_PARAM,movieId);
        LoaderManager.getInstance(this).initLoader(MOVIE_DETAIL_LOADER_ID,loaderArgs,mLoaderCallback);
    }
    private void loadMovieCover(String posterPath)
    {
        Uri uri = mMovieApi.getMoviePoster(posterPath, ImageSize.IMAGE_BIG);
        Picasso.get().load(uri)
                .placeholder(R.drawable.placeholder)
                .into(mPosterView);
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
                mTitleTextView.setText(fullMovieInfo.movieData.getTitle());
                mReleaseDateTextView.setText(DisplayUtils.formatDateText(fullMovieInfo.movieData.getRelease_date()));
                loadMovieCoverWithFallback(fullMovieInfo);
            }
        });
    }
    private void loadMovieCoverWithFallback(FullMovieInfo fullInfo)
    {
        Uri uri = mMovieApi.getMoviePoster(fullInfo.movieData.getMovie_poster_path(), ImageSize.IMAGE_BIG);
        Drawable w185 = new BitmapDrawable(getResources(),fullInfo.cover.getMoviePosterW185());
        Picasso.get().load(uri)
                .placeholder(R.drawable.placeholder)
                .error(w185)
                .into(mPosterView);
    }

    private void restartLoader()
    {
        Bundle loaderArgs = new Bundle();
        loaderArgs.putInt(MovieDetailLoaderCallback.LOADER_PARAM,mMovieId);
        loaderArgs.putBoolean(MovieDetailLoaderCallback.LOADER_PARAM_SAVE_OR_DELETE,mMovieLiked);
        LoaderManager.getInstance(this).restartLoader(MOVIE_DETAIL_LOADER_ID,loaderArgs,mLoaderCallback);
    }
    private void updateMenuText(MenuItem item)
    {
        String title = mMovieLiked ? getString(R.string.action_unlike) : getString(R.string.action_like);
        item.setTitle(title);
    }
}
