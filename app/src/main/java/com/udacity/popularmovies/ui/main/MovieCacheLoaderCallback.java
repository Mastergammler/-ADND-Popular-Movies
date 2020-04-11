package com.udacity.popularmovies.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.settings.AppPreferences;
import com.udacity.popularmovies.sync.SyncDiscoveryTask;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;
import com.udacity.popularmovies.ui.moviedetails.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

public class MovieCacheLoaderCallback implements LoaderManager.LoaderCallbacks<MovieInfo[]>
{
    private final Context mContext;
    private GridView mGridView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageText;

    public MovieCacheLoaderCallback(Context context,GridView gridView, ProgressBar loadingIndicator,TextView errorMessageView)
    {
        mContext = context;
        mGridView = gridView;
        mLoadingIndicator = loadingIndicator;
        mErrorMessageText = errorMessageView;
    }

    @NonNull
    @Override
    public Loader<MovieInfo[]> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<MovieInfo[]>(mContext)
        {
            @Override
            protected void onStartLoading()
            {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.INVISIBLE);

                String discovery  = AppPreferences.getCurrentDiscoveryCache(mContext);

                if(discovery == null)
                {
                    forceLoad();
                }
                else
                {
                    deliverResult(MovieInfo.parseJsonArray(discovery));
                }
            }

            @Nullable
            @Override
            public MovieInfo[] loadInBackground()
            {
                MovieInfo[] movies = null;
                SyncDiscoveryTask.syncCachedDiscoveryData(mContext);
                String discovery = AppPreferences.getCurrentDiscoveryCache(mContext);
                return MovieInfo.parseJsonArray(discovery);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieInfo[]> loader, MovieInfo[] data) {
        mLoadingIndicator.setVisibility(View.GONE);
        loadImages(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieInfo[]> loader) { }

    //-----------
    //  Helpers
    //-----------

    private void loadImages(MovieInfo[] movies)
    {
        if(movies == null)
        {
            mGridView.setVisibility(View.GONE);
            mErrorMessageText.setVisibility(View.VISIBLE);
            mErrorMessageText.setText(R.string.error_message);
        }
        else
        {
            MovieInfoAdapter adapter = new MovieInfoAdapter(mGridView.getContext(),mGridView,movies);
            mErrorMessageText.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    MovieInfo info = (MovieInfo) adapterView.getItemAtPosition(i);
                    startDetailActivity(info);
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

    private void startDetailActivity(MovieInfo info)
    {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_CONTENT_KEY,info);

        mContext.startActivity(intent);
    }
}
