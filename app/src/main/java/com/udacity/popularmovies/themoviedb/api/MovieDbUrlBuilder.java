package com.udacity.popularmovies.themoviedb.api;

import android.net.Uri;

import com.udacity.popularmovies.networking.NetworkingUtil;

import java.net.URL;

/**
 * Class for building the api urls
 */
public class MovieDbUrlBuilder implements ApiConfiguration, ApiConstants
{
    //--------------
    //  FUNCTIONS
    //--------------

    public static URL getMoviesByPopularityURL()
    {
        return getDiscoveryUrlBy(POPULARITY);
    }

    public static URL getMoviesByUserRatingURL()
    {
        return getDiscoveryUrlBy(VOTE_AVERAGE);
    }

    public static Uri getMovieImageURL(String imagePath)
    {
        return getMovieImageURL(imagePath,ImageSize.IMAGE_MEDIUM);
    }

    public static Uri getMovieImageURL(String imagePath,ImageSize size)
    {
        if(imagePath == null || imagePath.equals("")) return null;
        Uri uri = Uri.parse(IMAGE_BASE).buildUpon()
                .appendPath(getImageSizePath(size))
                .appendPath(imagePath.substring(1))
                .build();
        return uri;
    }

    public static Uri getMovieBackdropUri(String imagePath)
    {
        if(imagePath == null) return null;
        Uri uri = Uri.parse(IMAGE_BASE).buildUpon()
                .appendPath("w300")
                .appendPath(imagePath.substring(1))
                .build();
        return uri;
    }

    //------------
    //  HELPERS
    //------------

    private static URL getDiscoveryUrlBy(String discoveryMode)
    {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .path(BASE_PATH + DISCOVER_PARAM)
                .appendQueryParameter(API_KEY_QUERY,API_KEY)
                .appendQueryParameter(SORT_BY_QUERY,discoveryMode).build();
        return NetworkingUtil.parseUri(uri);
    }

    private static String getImageSizePath(ImageSize size)
    {
        switch (size)
        {
            case IMAGE_SMALL: return IMAGE_SMALL;
            case IMAGE_BIG: return IMAGE_BIG;
            case IMAGE_ORIGINAL: return IMAGE_ORIGINAL;
            default: return IMAGE_MEDIUM;
        }
    }
}
