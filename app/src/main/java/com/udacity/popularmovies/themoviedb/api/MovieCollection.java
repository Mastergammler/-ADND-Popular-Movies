package com.udacity.popularmovies.themoviedb.api;

import com.google.gson.Gson;

/**
 * Data object vor the movie discovery result
 */
public class MovieCollection
{
    public int page;
    public int total_results;
    public int total_pages;
    public MovieInfo[] results;

    public static MovieCollection parseJson(String json)
    {
        return new Gson().fromJson(json,MovieCollection.class);
    }

}
