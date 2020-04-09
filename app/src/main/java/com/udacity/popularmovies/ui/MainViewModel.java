package com.udacity.popularmovies.ui;

import android.app.Application;
import android.util.Log;

import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * View model to hold the favourites
 * And also the currently cached movie discovery?
 */
public class MainViewModel extends AndroidViewModel
{
    private LiveData<List<FullMovieInfo>> favouriteMovies;
    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(Application app)
    {
        super(app);
        FavouritesDatabase db = FavouritesDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG,"Actively retrieving tasks from database.");
        favouriteMovies = db.favouritesDao().getFavouriteMovies();
    }

    public LiveData<List<FullMovieInfo>> getFavourites()
    {
        return favouriteMovies;
    }
}
