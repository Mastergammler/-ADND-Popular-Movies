package com.udacity.popularmovies.sync;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.udacity.popularmovies.DiscoveryMode;
import com.udacity.popularmovies.settings.AppPreferences;
import com.udacity.popularmovies.settings.DiscoveryCache;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

/**
 * Tasks for the sync of the cached data for the movie discovery
 * Also handles weather to load data from api or from the db (favourites)
 */
public class SyncDiscoveryTask
{
    public static synchronized void syncCachedDiscoveryData(Context context)
    {
        DiscoveryMode mode = AppPreferences.getLatestDiscoveryMode(context);
        MovieApi api = new MovieApi();

        if(mode == DiscoveryMode.FAVOURITES)
        {
            // todo use database to pull the data
        }
        else
        {
            MovieInfo[] popularMovies = api.getMoviesByPopularity();
            MovieInfo[] bestRatedMovies = api.getMoviesByRating();

            // TODO: 06.04.2020 fix this when api returns strings
            DiscoveryCache cache = new DiscoveryCache(
                    new Gson().toJson(popularMovies),
                    new Gson().toJson(bestRatedMovies)
            );
            AppPreferences.updateDiscoveryCache(context,cache);
        }
    }

}
