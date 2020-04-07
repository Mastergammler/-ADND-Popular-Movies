package com.udacity.popularmovies.themoviedb.api.data;

/**
 * Object to store the video info result from the api request
 * Also handles creating a url for the video site
 */
public class VideoInfo
{
    private static final String URL_START = "https://www.";

    String id;
    String key;
    String site;
    VideoType type;

    // TODO: 07.04.2020 check if that works for all trailer sites?
    public String buildVideoUrl()
    {
        return URL_START + site + ".com/" + key;
    }
}
