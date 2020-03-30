package com.udacity.popularmovies.themoviedb.api;

import com.google.gson.Gson;

public class MovieInfo
{
    public int id;
    public String title;
    public String poster_path;
    public String backdrop_path;
    public String overview;
    public int vote_average;
    public String original_title;
    public String release_date;

    public static MovieInfo parseJson(String json)
    {
        return new Gson().fromJson(json,MovieInfo.class);
    }
}
