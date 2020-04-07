package com.udacity.popularmovies.themoviedb.api.data;

import com.google.gson.Gson;

import java.io.Serializable;

public class MovieInfo implements Serializable
{
    public int id;
    public String title;
    public String poster_path;
    public String backdrop_path;
    public String overview;
    public float vote_average;
    public String original_title;
    public String release_date;
    public int runtime;
    public boolean video;
    public String status;
    public String tagline;
    public int revenue;


    public static MovieInfo parseJson(String json) { return new Gson().fromJson(json,MovieInfo.class); }
    public static MovieInfo[] parseJsonArray(String json){return new Gson().fromJson(json,MovieInfo[].class);}
    public static String toJson(MovieInfo info)
    {
        return new Gson().toJson(info,MovieInfo.class);
    }
    public static String toJson(MovieInfo[] movies){return new Gson().toJson(movies,MovieInfo[].class);}
}
