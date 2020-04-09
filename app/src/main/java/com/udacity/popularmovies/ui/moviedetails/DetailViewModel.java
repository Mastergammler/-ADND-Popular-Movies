package com.udacity.popularmovies.ui.moviedetails;

import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel
{
    private LiveData<FullMovieInfo> mMovieData;

    public DetailViewModel(FavouritesDatabase db, int movieId)
    {
        mMovieData = db.favouritesDao().getMovieById(movieId);
    }

    public LiveData<FullMovieInfo> getMovieData()
    {
        return mMovieData;
    }
}
