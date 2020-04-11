package com.udacity.popularmovies.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public abstract class MovieAdapter<T> extends BaseAdapter
{
    private Context mContext;
    protected T[] mMovieItems;
    private GridView mGrid;

    public MovieAdapter(Context context, GridView grid,T[] movies)
    {
        mContext = context;
        mMovieItems = movies;
        mGrid = grid;
    }

    @Override
    public int getCount() {
        return mMovieItems.length;
    }
    @Override
    public T getItem(int i) {
        return mMovieItems[i];
    }
    @Override
    public abstract long getItemId(int i);

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ImageView imageView = createOrCastImageView(view);
        updateImage(imageView,i);

        return imageView;
    }

    protected ImageView createOrCastImageView(View view)
    {
        if(view != null) return (ImageView) view;

        ImageView iv = new ImageView(mContext);
        int newWidth = mGrid.getColumnWidth();
        iv.setLayoutParams(new ViewGroup.LayoutParams(newWidth,(int)(1.5*newWidth)));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return iv;
    }

    protected abstract void updateImage(ImageView iv, int itemIndex);
}
