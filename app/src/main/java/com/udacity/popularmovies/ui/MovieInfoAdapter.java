package com.udacity.popularmovies.ui;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

public class MovieInfoAdapter extends MovieAdapter<MovieInfo>
{
    private MovieApi mMovieApi;

    public MovieInfoAdapter(Context context, GridView grid,MovieInfo[] movies)
    {
        super(context,grid,movies);
        mMovieApi = new MovieApi();
    }

    @Override
    public long getItemId(int i) {
        return mMovieItems[i].id;
    }
    @Override
    protected void updateImage(ImageView iv, int itemIndex)
    {
        Uri imageUri = mMovieApi.getMoviePoster(mMovieItems[itemIndex], ImageSize.IMAGE_MEDIUM);
        if(imageUri != null)
        {
            RequestCreator req = Picasso.get().load(imageUri).error(R.drawable.placeholder);
            req.into(iv);
        }
        else
        {
            iv.setImageResource(R.drawable.placeholder);
        }
    }
}
