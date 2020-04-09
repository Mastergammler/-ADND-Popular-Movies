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

    /**
     * Searches the current pulled collection from the database for one specific value
     * Function is used to fill the Detail Activity view for favourited movies
     * Theoretically it should never happen that the collection is null when this function is called
     * because the view model data gets pulled as soon as the app starts
     * @param movieId id of the movie
     * @return null if none is found or the data is not loaded from db yet
     */
    public FullMovieInfo getInfoFor(int movieId)
    {
        if(favouriteMovies.getValue() == null) return null;

        for(FullMovieInfo info : favouriteMovies.getValue())
        {
            if(info.movieData.getMovie_id() == movieId) return info;
        }
        return null;
    }
}
