package com.udacity.popularmovies.themoviedb.api.data;

import com.google.gson.Gson;

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

    public static MovieReview[] reviewsFromJson(String collectionJson)
    {
        return new Gson().fromJson(collectionJson,ReviewCollection.class).results;
    }
}
