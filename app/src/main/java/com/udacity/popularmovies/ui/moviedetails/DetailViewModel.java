package com.udacity.popularmovies.ui.moviedetails;

import android.util.Log;

import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel
{
    private LiveData<FullMovieInfo> mMovieData;
    private static final String TAG = DetailViewModel.class.getSimpleName();

    public DetailViewModel(FavouritesDatabase db, int movieId)
    {
        Log.d(TAG,"Actively retrieving data from db");
        mMovieData = db.favouritesDao().getMovieById(movieId);
    }

    public LiveData<FullMovieInfo> getMovieData()
    {
        return mMovieData;
    }
}
