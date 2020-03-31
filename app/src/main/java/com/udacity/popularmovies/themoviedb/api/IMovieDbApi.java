package com.udacity.popularmovies.themoviedb.api;

import android.net.Uri;

import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

import java.util.List;

/**
 * Api access for themoviedb api
 */
public interface IMovieDbApi
{
    /**
     * Returns the list of movies, sorted by popularity
     */
    MovieInfo[] getMoviesByPopularity();

    /**
     * Returns the list of movies, sorted by rating
     */
    MovieInfo[] getMoviesByRating();

    /**
     * Gets detailed info for a specific movie
     */
    MovieInfo getMovieDetails();

    /**
     * Builds the uri for accessing the poster image of a specific movie
     * @param info The movie data object
     * @param size The size of the poster to load ( SMALL | MEDIUM | BIG | ORIGINAL )
     * @return Uri of the poster image
     */
    Uri getMoviePoster(MovieInfo info, ImageSize size);

    /**
     * Builds the uri for accessing the poster image of a specific movie
     * @param imagePath The relative image path within the api
     * @param size The size of the poster to load ( SMALL | MEDIUM | BIG | ORIGINAL )
     * @return Uri of the poster image
     */
    Uri getMoviePoster(String imagePath, ImageSize size);

}
