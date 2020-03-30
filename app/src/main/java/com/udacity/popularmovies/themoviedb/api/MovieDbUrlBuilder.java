package com.udacity.popularmovies.themoviedb.api;

import android.net.Uri;

import com.udacity.popularmovies.networking.NetworkingUtil;

import java.net.URL;

/**
 * Class for building the api urls
 */
public class MovieDbUrlBuilder implements ApiConfiguration, ApiConstants
{
    public static URL getMoviesByPopularityURL()
    {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .path(BASE_PATH + DISCOVER_PARAM)
                .appendQueryParameter(API_KEY_QUERY,API_KEY)
                .appendQueryParameter(SORT_BY_QUERY,POPULARITY).build();
        return NetworkingUtil.parseUri(uri);
    }
}
