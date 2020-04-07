package com.udacity.popularmovies.moviedetails;

import android.net.Uri;

import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;
import com.udacity.popularmovies.themoviedb.api.data.MovieReview;

/**
 * Java parameter wrapper object for the load details result
 */
public class MovieDetails
{
    public MovieInfo movieInfo;
    public MovieReview[] movieReviews;
    public Uri[] movieTrailers;

    public MovieDetails(MovieInfo info, MovieReview[] reviews, Uri[] trailers)
    {
        movieInfo = info;
        movieReviews = reviews;
        movieTrailers = trailers;
    }
}
