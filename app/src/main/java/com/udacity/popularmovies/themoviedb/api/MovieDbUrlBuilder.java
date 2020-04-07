package com.udacity.popularmovies.themoviedb.api;

import android.net.Uri;

import com.udacity.popularmovies.sync.NetworkingUtil;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;

import java.net.URL;

/**
 * Class for building the api urls
 */
class MovieDbUrlBuilder implements ApiConfiguration, ApiConstants
{
    //#################
    //##  FUNCTIONS  ##
    //#################

    //---------------
    //  Discovery
    //---------------

    static URL getMoviesByPopularityURL()
    {
        return getDiscoveryUrlBy(POPULARITY);
    }

    static URL getMoviesByUserRatingURL()
    {
        return getDiscoveryUrlBy(VOTE_AVERAGE);
    }

    //------------------
    //  Movie Details
    //------------------

    static URL getMovieDetailURL(int id)
    {
        if(id == 0) return null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .path(BASE_PATH)
                .appendPath(MOVIE_PATH)
                .appendPath(String.valueOf(id))
                .appendQueryParameter(API_KEY_QUERY,API_KEY).build();
        return NetworkingUtil.parseUri(uri);
    }



    //-------------
    //  Images
    //-------------

    public static Uri getMovieImageURL(String imagePath)
    {
        return getMovieImageURL(imagePath, ImageSize.IMAGE_MEDIUM);
    }

    static Uri getMovieImageURL(String imagePath,ImageSize size)
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
                .path(BASE_PATH + DISCOVERY_PATH)
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
