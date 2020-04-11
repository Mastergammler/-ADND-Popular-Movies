package com.udacity.popularmovies.ui.main;

import android.app.Application;
import android.util.Log;

import com.udacity.popularmovies.favouritesdb.Entitites.MovieCover;
import com.udacity.popularmovies.favouritesdb.FavouritesDatabase;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * View model to hold the covers of the current favourites
 * And also the currently cached movie discovery?
 */
public class MainViewModel extends AndroidViewModel
{
    private LiveData<List<MovieCover>> favouriteMovies;
    private static final String LOG_TAG = MainViewModel.class.getSimpleName();

    public MainViewModel(Application app)
    {
        super(app);
        FavouritesDatabase db = FavouritesDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG,"Actively retrieving movie covers from database.");
        favouriteMovies = db.favouritesDao().getMovieCovers();
    }

    public LiveData<List<MovieCover>> getFavourites()
    {
        return favouriteMovies;
    }
}
