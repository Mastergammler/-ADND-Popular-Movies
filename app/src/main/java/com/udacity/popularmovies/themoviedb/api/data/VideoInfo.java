package com.udacity.popularmovies.themoviedb.api.data;

import android.net.Uri;

/**
 * Object to store the video info result from the api request
 * Also handles creating a url for the video site
 */
public class VideoInfo
{
    private static final String URL_START = "https://www.";

    public VideoInfo(String id,String key, String site, String name, String type)
    {
        this.id = id;
        this.key = key;
        this.site = site;
        this.name = name;
        this.type =type;
        this.typeEnum = VideoType.toEnum(type);
    }

    public String id;
    public String key;
    public String site;
    public String name;
    private String type;
    public VideoType typeEnum;

    // TODO: 07.04.2020 check if that works for all trailer sites?
    public Uri buildVideoUrl()
    {
        return Uri.parse(URL_START + site + ".com/watch?v=" + key);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeEnum = VideoType.toEnum(type);
    }
}
