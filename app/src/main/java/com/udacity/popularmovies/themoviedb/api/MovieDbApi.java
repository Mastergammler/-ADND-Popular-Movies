package com.udacity.popularmovies.themoviedb.api;

import java.util.List;

/**
 * Api access for themoviedb api
 */
public interface MovieDbApi
{
    /**
     * Returns the list of movies, sorted by popularity
     */
    List<MovieInfo> getMoviesByPopularity();

    /**
     * Returns the list of movies, sorted by rating
     */
    List<MovieInfo> getMoviesByRating();

    /**
     * Gets detailed info to a specific movie
     */
    MovieInfo getMovieDetails();
}
