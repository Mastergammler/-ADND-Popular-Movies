package com.udacity.popularmovies.favouritesdb.Entitites;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entity that holds only the bitmap of the movie cover
 * Because on main view we don't need other information
 */
@Entity(tableName = "movie_posters")
public class MovieCover
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "movie_poster_w185")
    private Bitmap moviePosterW185;

    /**
     * User constructor
     */
    @Ignore
    public MovieCover(int movieId,Bitmap image)
    {
        this.movieId = movieId;
        moviePosterW185 = image;
    }

    /**
     * Constructor for room
     */
    public MovieCover(int id,int movieId, Bitmap moviePosterW185)
    {
        this.id = id;
        this.movieId = movieId;
        this.moviePosterW185 = moviePosterW185;
    }


    public int getMovieId()
    {
        return movieId;
    }

    public Bitmap getMoviePosterW185()
    {
        return moviePosterW185;
    }

    public int getId() {
        return id;
    }
}
