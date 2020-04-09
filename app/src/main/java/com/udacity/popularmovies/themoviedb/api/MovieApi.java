package com.udacity.popularmovies.themoviedb.api;


import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies.sync.NetworkingUtil;
import com.udacity.popularmovies.themoviedb.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieCollection;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;
import com.udacity.popularmovies.themoviedb.api.data.MovieReview;
import com.udacity.popularmovies.themoviedb.api.data.ReviewCollection;
import com.udacity.popularmovies.themoviedb.api.data.VideoCollection;
import com.udacity.popularmovies.themoviedb.api.data.VideoInfo;

import java.io.IOException;
import java.net.URL;

/**
 * Implementation for the IMovieDBApi interface
 * This class will access the api over a network connections
 * Calls are not threaded via AsyncTasks etc, this has to be done by the calling instance
 */
public class MovieApi implements IMovieDbApi
{

    public MovieApi() {}

    //-------------------
    //  I MOVIE DB API
    //-------------------

    @Override
    public MovieInfo[] getMoviesByPopularity()
    {
        URL url = MovieDbUrlBuilder.getMoviesByPopularityURL();
        return getMovies(url);
    }

    @Override
    public String getMoviesByPopularityJson()
    {
        URL url = MovieDbUrlBuilder.getMoviesByPopularityURL();
        return getMoviesArrayJson(url);
    }

    @Override
    public MovieInfo[] getMoviesByRating()
    {
        URL url = MovieDbUrlBuilder.getMoviesByUserRatingURL();
        return getMovies(url);
    }

    @Override
    public String getMoviesByRatingJson()
    {
        URL url = MovieDbUrlBuilder.getMoviesByUserRatingURL();
        return getMoviesArrayJson(url);
    }

    @Override
    public MovieInfo getMovieDetails(int movieId)
    {
        URL url = MovieDbUrlBuilder.getMovieDetailURL(movieId);
        String json = getNetworkResponse(url);
        return MovieInfo.parseJson(json);
    }

    @Override
    public VideoInfo[] getVideoLinks(int movieId, boolean trailersOnly)
    {
        URL url = MovieDbUrlBuilder.getVideosURL(movieId);
        String json = getNetworkResponse(url);
        VideoInfo[] infos = VideoCollection.videosFromJson(json);
        infos = trailersOnly ? filterTrailers(infos) : infos;

        return infos;
    }

    @Override
    public MovieReview[] getMovieReviews(int movieId)
    {
        URL url = MovieDbUrlBuilder.getReviewsURL(movieId);
        String json = getNetworkResponse(url);
        MovieReview[] reviews = ReviewCollection.reviewsFromJson(json);
        return reviews;
    }

    @Override
    public Uri getMoviePoster(MovieInfo info, ImageSize size)
    {
        return getMoviePoster(info.poster_path,size);
    }

    @Override
    public Uri getMoviePoster(String imagePath, ImageSize size)
    {
        return MovieDbUrlBuilder.getMovieImageURL(imagePath,size);
    }

    //------------
    //  HELPERS
    //------------

    private Uri[] getVideoUris(VideoInfo[] videoInfos)
    {
        Uri[] uris = new Uri[videoInfos.length];
        for(int i = 0; i < uris.length; i++)
        {
            uris[i] = videoInfos[i].buildVideoUrl();
        }
        return uris;
    }

    private VideoInfo[] filterTrailers(VideoInfo[] foundVideos)
    {
        // TODO: 07.04.2020 implement actual filter
        return foundVideos;
    }

    private MovieInfo[] getMovies(URL url)
    {
        String jsonResult = getNetworkResponse(url);
        MovieCollection collection = parseDiscoveryResult(jsonResult);

        if(collection != null) return collection.results;
        return null;
    }
    private String getMoviesArrayJson(URL url)
    {
        return MovieInfo.toJson(getMovies(url));
    }

    private String getNetworkResponse(URL url)
    {
        String jsonResult = null;

        try
        {
            jsonResult = NetworkingUtil.getResponseFromHttpRequest(url);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return jsonResult;
    }
    private MovieCollection parseDiscoveryResult(String jsonDiscoveryResult)
    {
        MovieCollection collection = null;
        try
        {
            collection = MovieCollection.parseJson(jsonDiscoveryResult);
        }
        catch(Exception e)
        {
            // general exception to catch all kinds of errors, not just when json string is null
            // although its not expected that the api would return invalid json
            Log.w(MovieApi.class.getSimpleName(),"Failed to parse json data!");
            e.printStackTrace();
        }
        return collection;
    }
}
