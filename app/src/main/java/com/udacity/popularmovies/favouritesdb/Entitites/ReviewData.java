package com.udacity.popularmovies.favouritesdb.Entitites;

import com.udacity.popularmovies.themoviedb.api.data.MovieReview;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "review_data")
public class ReviewData
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movie_id;
    private String author;
    private String content;

    /**
     * Developer constructor
     * @param movieId
     * @param review
     */
    @Ignore
    public ReviewData(int movieId, MovieReview review)
    {
        movie_id = movieId;
        author = review.author;
        content = review.content;
    }

    /**
     * Room constructor
     */
    public ReviewData(int id, int movie_id, String author, String content)
    {
        this.id = id;
        this.movie_id = movie_id;
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
