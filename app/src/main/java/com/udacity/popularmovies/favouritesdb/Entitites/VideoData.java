package com.udacity.popularmovies.favouritesdb.Entitites;

import android.net.Uri;

import com.udacity.popularmovies.themoviedb.api.data.VideoInfo;
import com.udacity.popularmovies.themoviedb.api.data.VideoType;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "video_data")
public class VideoData
{
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private int movie_id;
    private VideoType type;
    private String title;
    private Uri uri;

    @Ignore
    public VideoData(int movieId,VideoInfo info)
    {
        movie_id = movieId;
        type = VideoType.toEnum(info.getType());
        title = info.name;
        uri = info.buildVideoUrl();
    }

    /**
     * Room constructor
     * @param uid
     * @param movie_id
     * @param type
     * @param title
     * @param uri
     */
    public VideoData(int uid, int movie_id, VideoType type, String title, Uri uri)
    {
        this.uid = uid;
        this.movie_id = movie_id;
        this.type = type;
        this.title = title;
        this.uri = uri;
    }

    public int getUid() {
        return uid;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public VideoType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }
}
