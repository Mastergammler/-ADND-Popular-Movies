package com.udacity.popularmovies.themoviedb.api.data;

/**
 * Review Collection object for the corresponding tmdb json response
 */
public class ReviewCollection
{
    int id;
    int page;
    int total_pages;
    int total_results;
    MovieReview[] results;
}
