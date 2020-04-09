package com.udacity.popularmovies.favouritesdb;

import android.content.Context;
import android.util.Log;

import com.udacity.popularmovies.favouritesdb.Entitites.ImageConverter;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieCover;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieData;
import com.udacity.popularmovies.favouritesdb.Entitites.ReviewData;
import com.udacity.popularmovies.favouritesdb.Entitites.UriConverter;
import com.udacity.popularmovies.favouritesdb.Entitites.VideoData;
import com.udacity.popularmovies.favouritesdb.Entitites.VideoTypeConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@TypeConverters({ImageConverter.class, UriConverter.class, VideoTypeConverter.class})
@Database(entities = {MovieData.class, VideoData.class, ReviewData.class, MovieCover.class},version = 2,exportSchema = false)
public abstract class FavouritesDatabase extends RoomDatabase
{
    private static final String LOG_TAG = FavouritesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "fav_db";
    private static FavouritesDatabase sInstance;

    public static FavouritesDatabase getInstance(Context context)
    {
        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                Log.d(LOG_TAG,"Creating database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouritesDatabase.class,
                        FavouritesDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG,"Getting database instance");
        return sInstance;
    }


    public abstract MovieDataDao favouritesDao();

}
