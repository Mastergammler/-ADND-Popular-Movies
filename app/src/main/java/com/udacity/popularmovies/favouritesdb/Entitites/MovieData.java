package com.udacity.popularmovies.favouritesdb.Entitites;

import android.graphics.Bitmap;

import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_data")
public class MovieData
{
    public static final int MILLION = 1000000;
    @PrimaryKey
    private int movie_id;
    private int runtime;
    private int revenue;
    private int budget;

    private float user_rating;

    private String title;
    private String tagline;
    private String overview;
    private String release_date;

    private Bitmap movie_poster_w185;
    private String movie_poster_path;

    /**
     * Constructor for user
     * @param info - The movie info that holds ALL the updated information (gotten via movie/id)
     * @param moviePoster - The bitmap of the image loaded from the internet (w185 version)
     */
    @Ignore
    public MovieData(MovieInfo info,Bitmap moviePoster)
    {
        movie_id = info.id;
        runtime = info.runtime;
        user_rating = info.vote_average;
        revenue = info.revenue;
        budget = info.budget;

        title = info.title;
        tagline = info.tagline;
        overview = info.overview;
        release_date = info.release_date;

        movie_poster_w185 = moviePoster;
        movie_poster_path = info.poster_path;
    }

    /**
     * Constructor for Room
     */
    public MovieData(int movie_id,int runtime,float user_rating,int revenue,int budget,
                     String title,String tagline,String overview,String release_date,
                     Bitmap movie_poster_w185,String movie_poster_path)
    {
        this.movie_id = movie_id;
        this.runtime = runtime;
        this.user_rating = user_rating;
        this.revenue = revenue;
        this.budget = budget;
        this.title = title;
        this.tagline = tagline;
        this.overview = overview;
        this.release_date = release_date;
        this.movie_poster_w185 = movie_poster_w185;
        this.movie_poster_path = movie_poster_path;
    }


    //-------------
    //  Getter
    //-------------

    public int getMovie_id() {
        return movie_id;
    }

    public int getRuntime() {
        return runtime;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getBudget() {
        return budget;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Bitmap getMovie_poster_w185() {
        return movie_poster_w185;
    }

    public String getMovie_poster_path() {
        return movie_poster_path;
    }
}
