package com.udacity.popularmovies.ui.moviedetails;

import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory
{

    private final FavouritesDatabase mDb;
    private final int mMovieId;

    DetailViewModelFactory(FavouritesDatabase db, int movieId)
    {
        mDb = db;
        mMovieId = movieId;
    }

    public <T extends ViewModel> T create(Class<T> modelClass)
    {
        return (T) new DetailViewModel(mDb,mMovieId);
    }

}
