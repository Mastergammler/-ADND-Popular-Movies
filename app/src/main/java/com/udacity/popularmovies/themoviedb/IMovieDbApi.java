package com.udacity.popularmovies.themoviedb;

import android.net.Uri;

import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

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
     * Returns a json file of the Movie Array
     * Json for making caching easier
     */
    String getMoviesByPopularityJson();

    /**
     * Returns the list of movies, sorted by rating
     */
    MovieInfo[] getMoviesByRating();

    /**
     * Returns the json of the Movie Array
     * To make caching easier
     */
    String getMoviesByRatingJson();

    /**
     * Gets detailed info for a specific movie
     * This is achieved by accessing the movie via its id
     * And pulling data from movies/id
     */
    MovieInfo getMovieDetails(int movieId);

    /**
     * Gets the video links for the movie with the specified id
     * It calls the api via movies/id/videos
     * @param movieId - tmdb movie id
     * @param trailersOnly - flag weather to filter for trailers
     * @return null if no trailers / videos where found
     */
    String[] getVideoLinks(int movieId, boolean trailersOnly);



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
