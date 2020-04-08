package com.udacity.popularmovies.favouritesdb.Entitites;

import com.udacity.popularmovies.themoviedb.api.data.VideoType;

import androidx.room.TypeConverter;

public class VideoTypeConverter
{
    @TypeConverter
    public int convertType(VideoType type)
    {
        return type.ordinal();
    }

    @TypeConverter
    public VideoType convertOrdinal(int ordinal)
    {
        return VideoType.of(ordinal);
    }
}
