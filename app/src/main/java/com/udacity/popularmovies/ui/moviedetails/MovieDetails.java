package com.udacity.popularmovies.ui.moviedetails;

import android.net.Uri;

import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;
import com.udacity.popularmovies.themoviedb.api.data.MovieReview;
import com.udacity.popularmovies.themoviedb.api.data.VideoInfo;

/**
 * Java parameter wrapper object for the load details result
 */
public class MovieDetails
{
    public MovieInfo movieInfo;
    public MovieReview[] movieReviews;
    public VideoInfo[] movieTrailers;

    public MovieDetails(MovieInfo info, MovieReview[] reviews, VideoInfo[] trailers)
    {
        movieInfo = info;
        movieReviews = reviews;
        movieTrailers = trailers;
    }
}
