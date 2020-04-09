package com.udacity.popularmovies.favouritesdb;

import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieData;
import com.udacity.popularmovies.favouritesdb.Entitites.ReviewData;
import com.udacity.popularmovies.favouritesdb.Entitites.VideoData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDataDao
{
    @Query("SELECT * FROM movie_data ORDER BY user_rating")
    List<FullMovieInfo> getFavouriteMovies();

    @Insert
    void saveMovieAsFavourite(MovieData info);
    @Insert
    void saveReviews(ReviewData data);
    @Insert
    void saveVideos(VideoData data);

    @Query("DELETE FROM movie_data WHERE movie_id = :movieId")
    int deleteMovieFromFavourites(int movieId);
    @Query("DELETE FROM review_data WHERE movie_id = :movieId")
    int deleteReviews(int movieId);
    @Query("DELETE FROM video_data WHERE movie_id = :movieId")
    int deleteVideos(int movieId);
}
