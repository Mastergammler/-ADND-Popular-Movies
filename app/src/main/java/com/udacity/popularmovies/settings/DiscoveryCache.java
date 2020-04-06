package com.udacity.popularmovies.settings;

import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

/**
 * Data object for all the possible caches that can be chosen as discovery
 * Used as a parameter for the preferences to be easy extendable
 */
public class DiscoveryCache
{
    public String popularMoviesJsonCache;
    public String highestRatedMoviesJsonCache;

    public DiscoveryCache(String popularMovies, String bestRatedMovies)
    {
        this.popularMoviesJsonCache = popularMovies;
        this.highestRatedMoviesJsonCache = bestRatedMovies;
    }
}
