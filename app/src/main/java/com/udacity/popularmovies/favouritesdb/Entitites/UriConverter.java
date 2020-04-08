package com.udacity.popularmovies.favouritesdb.Entitites;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriConverter
{
    @TypeConverter
    public Uri convertString(String uriString)
    {
        return Uri.parse(uriString);
    }

    @TypeConverter
    public String convertUri(Uri uri)
    {
        return uri.toString();
    }
}
