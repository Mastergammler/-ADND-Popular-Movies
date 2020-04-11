package com.udacity.popularmovies.ui;

import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.favouritesdb.Entitites.MovieCover;

public class MovieCoverAdapter extends MovieAdapter<MovieCover>
{
    public MovieCoverAdapter(Context context, GridView gridView, MovieCover[] data)
    {
        super(context,gridView,data);
    }

    @Override
    public long getItemId(int i) {
        return mMovieItems[i].getMovieId();
    }
    @Override
    protected void updateImage(ImageView iv, int itemIndex)
    {
        MovieCover currentData = mMovieItems[itemIndex];

        if(currentData.getMoviePosterW185() != null)
        {
            iv.setImageBitmap(currentData.getMoviePosterW185());
        }
        else
        {
            iv.setImageResource(R.drawable.placeholder);
        }
    }
}
