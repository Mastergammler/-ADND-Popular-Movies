package com.udacity.popularmovies.themoviedb.api.data;

import com.google.gson.Gson;

/**
 * Corresponding json object to the api movie/id/videos call
 */
public class VideoCollection
{
    int id;
    VideoInfo[] results;

    public static VideoInfo[] videosFromJson(String collectionString)
    {
        return new Gson().fromJson(collectionString,VideoCollection.class).results;
    }
}
