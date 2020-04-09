package com.udacity.popularmovies.favouritesdb.Entitites;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class FullMovieInfo
{
    @Embedded
    public MovieData movieData;
    @Relation(parentColumn = "movie_id",entityColumn = "movie_id",entity = MovieCover.class)
    public MovieCover cover;
    @Relation(parentColumn = "movie_id",entityColumn = "movie_id",entity = ReviewData.class)
    public List<ReviewData> reviews;
    @Relation(parentColumn = "movie_id",entityColumn = "movie_id",entity = VideoData.class)
    public List<VideoData> videos;
}
