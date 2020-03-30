package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.popularmovies.networking.NetworkingUtil;
import com.udacity.popularmovies.themoviedb.api.ImageSize;
import com.udacity.popularmovies.themoviedb.api.MovieCollection;
import com.udacity.popularmovies.themoviedb.api.MovieDbUrlBuilder;
import com.udacity.popularmovies.themoviedb.api.MovieInfo;

import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity{

    private GridView mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.gv_main_view);
        new DiscoverMoviesTask().execute(DiscoveryMode.POPULAR_DESC);
    }

    private void loadImages(MovieCollection collection)
    {
        ImageViewAdapter adapter = new ImageViewAdapter(mGrid.getContext(),collection);
        mGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //------------------
    //  LOAD JSON TASK
    //------------------

    class ImageViewAdapter extends BaseAdapter
    {

        private Context mContext;
        private MovieCollection mItemColleciton;

        public ImageViewAdapter(Context context, MovieCollection collection)
        {
            mContext = context;
            mItemColleciton = collection;
        }

        @Override
        public int getCount() {
            return mItemColleciton.results.length;
        }

        @Override
        public MovieInfo getItem(int i) {
            return mItemColleciton.results[i];
        }

        @Override
        public long getItemId(int i) {
            return mItemColleciton.results[i].id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            ImageView iv;
            if(view == null)
            {
                iv = new ImageView(mContext);
                //iv.setLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            }
            else
            {
                iv = (ImageView) view;
            }

            Uri imageUri = MovieDbUrlBuilder.getMovieImageURL(mItemColleciton.results[i].poster_path, ImageSize.IMAGE_BIG);
            RequestCreator req = Picasso.get().load(imageUri);
            req.into(iv);
            iv.setAdjustViewBounds(true);

            return iv;
        }
    }


    class DiscoverMoviesTask extends AsyncTask<DiscoveryMode,Void,MovieCollection>
    {
        @Override
        protected MovieCollection doInBackground(DiscoveryMode... discoveryModes) {
            String jsonResult = null;
            URL url = getUrlForMode(discoveryModes[0]);
            try
            {
                jsonResult = NetworkingUtil.getResponseFromHttpRequest(url);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            // TODO: 30.03.2020 check for null
            MovieCollection coll = MovieCollection.parseJson(jsonResult);
            return coll;
        }

        @Override
        protected void onPostExecute(MovieCollection movieCollection) {
            loadImages(movieCollection);
        }

        private URL getUrlForMode(DiscoveryMode mode)
        {
            switch (mode)
            {
                case USER_RATING_DESC:
                    return MovieDbUrlBuilder.getMoviesByUserRatingURL();
                default: return MovieDbUrlBuilder.getMoviesByPopularityURL();
            }
        }
    }

}
