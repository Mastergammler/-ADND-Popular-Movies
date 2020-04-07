package com.udacity.popularmovies.themoviedb.api;

/**
 * Holds the constant values for accessing themoviedb api
 */
public interface ApiConstants
{
    String BASE_URL = "https://api.themoviedb.org/";
    String BASE_PATH = "3/";
    String API_KEY_QUERY = "api_key";

    //-------------
    //  Discovery
    //-------------

    String DISCOVERY_PATH = "discover/movie";
    String SORT_BY_QUERY = "sort_by";
    String POPULARITY = "popularity.desc";
    String VOTE_AVERAGE = "vote_average.desc";

    //-----------------
    //  Movie Details
    //-----------------

    String MOVIE_PATH = "movie";
    String MOVIE_VIDEO_PATH = "videos";
    String MOVIE_REVIEW_PATH = "reviews";

    //**************
    //**  IMAGES  **
    //**************

    String IMAGE_BASE = "https://image.tmdb.org/t/p/";
    String IMAGE_SMALL = "w92";
    String IMAGE_MEDIUM = "w185";
    String IMAGE_BIG = "w500";
    String IMAGE_ORIGINAL = "original";
}
