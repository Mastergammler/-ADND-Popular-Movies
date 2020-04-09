package com.udacity.popularmovies.favouritesdb;

import com.udacity.popularmovies.favouritesdb.Entitites.FullMovieInfo;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieCover;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieData;
import com.udacity.popularmovies.favouritesdb.Entitites.ReviewData;
import com.udacity.popularmovies.favouritesdb.Entitites.VideoData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDataDao
{
    @Query("SELECT * FROM movie_data ORDER BY user_rating")
    LiveData<List<FullMovieInfo>> getFavouriteMovies();
    @Query("SELECT * FROM movie_data WHERE movie_id = :movieId")
    LiveData<FullMovieInfo> getMovieById(int movieId);
    @Query("SELECT * FROM movie_posters")
    LiveData<List<MovieCover>> getMovieCovers();

    @Insert
    void saveMovieAsFavourite(MovieData info);
    @Insert
    void saveReviews(ReviewData[] data);
    @Insert
    void saveVideos(VideoData[] data);
    @Insert
    void saveCover(MovieCover cover);

    @Query("DELETE FROM movie_data WHERE movie_id = :movieId")
    int deleteMovieFromFavourites(int movieId);
    @Query("DELETE FROM review_data WHERE movie_id = :movieId")
    int deleteReviews(int movieId);
    @Query("DELETE FROM video_data WHERE movie_id = :movieId")
    int deleteVideos(int movieId);
    @Query("DELETE FROM movie_posters WHERE movie_id = :movieId")
    int deleteCover(int movieId);

}
