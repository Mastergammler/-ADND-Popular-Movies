package com.udacity.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.favouritesdb.Entitites.MovieCover;
import com.udacity.popularmovies.ui.DiscoveryMode;
import com.udacity.popularmovies.ui.DisplayMode;
import com.udacity.popularmovies.ui.MainViewModel;
import com.udacity.popularmovies.ui.MovieCacheLoaderCallback;
import com.udacity.popularmovies.ui.MovieCoverAdapter;
import com.udacity.popularmovies.ui.MovieInfoAdapter;
import com.udacity.popularmovies.ui.moviedetails.DetailActivity;
import com.udacity.popularmovies.settings.AppPreferences;
import com.udacity.popularmovies.sync.SyncDiscoveryTask;
import com.udacity.popularmovies.themoviedb.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    //#################
    //##  CONSTANTS  ##
    //#################

    private static final int SYNC_DISCOVERY_CACHE_LOADER_ID = 344382;
    private static final String GRID_VIEW_SCROLL_POSITION_KEY = "grid-view-scroll-state";

    //------------
    //  Members
    //------------

    private GridView mGrid;
    private TextView mErrorMessageText;
    private ProgressBar mLoadingIndicator;
    private MovieCacheLoaderCallback mLoaderCallbback;

    private IMovieDbApi mMovieApi;
    //private Observer<List<MovieCover>> mFavouriteObserver;

    private static Bundle mLastSavedInstanceState = new Bundle();

    //----------------
    //  Android Init
    //----------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.gv_main_view);
        if(savedInstanceState != null && savedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY) != null)
        {
            mGrid.onRestoreInstanceState(savedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY));
        }
        else if(mLastSavedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY) != null)
        {
            mGrid.onRestoreInstanceState(mLastSavedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY));
        }

        mErrorMessageText = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mLoaderCallbback = new MovieCacheLoaderCallback(this,mGrid,mLoadingIndicator,mErrorMessageText);
        mMovieApi = new MovieApi();

        MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);

        // TEST ONLY
        AppPreferences.setPreferredGrid(this, DisplayMode.GRID_3x3);



        setPreferredGridView();
        loadMoviesFor(AppPreferences.getLatestDiscoveryMode(this));
        SyncDiscoveryTask.initialize(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(GRID_VIEW_SCROLL_POSITION_KEY,mGrid.onSaveInstanceState());
        mLastSavedInstanceState.putParcelable(GRID_VIEW_SCROLL_POSITION_KEY,mGrid.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if(savedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY)!=null)
        {
            mGrid.onRestoreInstanceState(savedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY));
        }
    }

    @Override
    protected void onResume() {
        onRestoreInstanceState(mLastSavedInstanceState);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        DiscoveryMode selectedMode;

        switch (item.getItemId())
        {
            case R.id.action_sort_by_popular:
                selectedMode = DiscoveryMode.POPULAR_DESC;
                break;
            case R.id.action_sort_by_rating:
                selectedMode = DiscoveryMode.USER_RATING_DESC;
                break;
            case R.id.action_sort_by_favourites:
                selectedMode = DiscoveryMode.FAVOURITES;
                break;
            default: return super.onOptionsItemSelected(item);
        }

        AppPreferences.updateLatestDiscoveryMode(this,selectedMode);
        loadMoviesFor(selectedMode);
        return true;
    }

    //------------
    //  Methods
    //------------

    private void setPreferredGridView()
    {
        DisplayMode mode = AppPreferences.getPreferredGrid(this);
        int currentOrientation = getResources().getConfiguration().orientation;

        if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            mGrid.setNumColumns((mode.ordinal() + 1) * 2);
        }
        else
        {
            mGrid.setNumColumns((mode.ordinal()+1));
        }
    }

    private void loadMoviesFor(DiscoveryMode mode)
    {
        MainViewModel vm = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);
        Observer<List<MovieCover>> obs = new Observer<List<MovieCover>>(){
            @Override
            public void onChanged(List<MovieCover> coverList)
            {
                loadImages(coverList.toArray(new MovieCover[coverList.size()]));
            }
        };
        switch (mode)
        {
            case POPULAR_DESC:
                setTitle(R.string.popular_movies);
                vm.getFavourites().removeObserver(obs);
                break;
            case USER_RATING_DESC:
                setTitle(R.string.highest_rated_movies);
                vm.getFavourites().removeObserver(obs);
                break;
            case FAVOURITES:
                setTitle(R.string.liked_movies);
                vm.getFavourites().observe(this,obs);
                return;
        }
        /**
         * Needs to be on restart loader, else it will not call 'onStartLoading()' when the options items are pressed
         */
        LoaderManager.getInstance(this).restartLoader(SYNC_DISCOVERY_CACHE_LOADER_ID,null,mLoaderCallbback);
    }

    private void loadImages(MovieCover[] data)
    {
        if(data == null || data.length == 0)
        {
            // TODO: 09.04.2020 does this actually happen? -> because its only triggerd by observer
            // it should always be there
            mGrid.setVisibility(View.GONE);
            mErrorMessageText.setVisibility(View.VISIBLE);
            mErrorMessageText.setText(R.string.error_not_loaded_from_db);
        }
        else
        {
            MovieCoverAdapter adapter = new MovieCoverAdapter(mGrid.getContext(),mGrid,data);
            mErrorMessageText.setVisibility(View.GONE);
            mGrid.setVisibility(View.VISIBLE);
            mGrid.setAdapter(adapter);
            mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    MovieCover info = (MovieCover) adapterView.getItemAtPosition(i);
                    startDetailActivity(info);
                }

            });
            adapter.notifyDataSetChanged();
        }
    }

    private void startDetailActivity(MovieCover data)
    {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_ID_KEY,data.getMovieId());

        startActivity(intent);
    }
}
