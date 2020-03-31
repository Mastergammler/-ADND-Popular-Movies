package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

public class MainActivity extends AppCompatActivity{

    //------------
    //  Members
    //------------

    private GridView mGrid;
    private TextView mErorreMessageText;

    //------------
    //  Android
    //------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.gv_main_view);
        mErorreMessageText = findViewById(R.id.tv_error_message);
        loadMoviesFor(DiscoveryMode.POPULAR_DESC);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_sort_by_popular:
                loadMoviesFor(DiscoveryMode.POPULAR_DESC);
                return true;
            case R.id.action_sort_by_rating:
                loadMoviesFor(DiscoveryMode.USER_RATING_DESC);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }






    private void loadMoviesFor(DiscoveryMode mode)
    {
        switch (mode)
        {
            case POPULAR_DESC:
                setTitle(R.string.popular_movies);
                break;
            case USER_RATING_DESC:
                setTitle(R.string.highes_rated_movies);
                break;
        }
        new DiscoverMoviesTask().execute(mode);
    }

    private void loadImages(MovieCollection collection)
    {
        if(collection == null)
        {
            mGrid.setVisibility(View.GONE);
            mErorreMessageText.setVisibility(View.VISIBLE);
        }
        else
        {
            ImageViewAdapter adapter = new ImageViewAdapter(mGrid.getContext(),collection);
            mErorreMessageText.setVisibility(View.GONE);
            mGrid.setVisibility(View.VISIBLE);
            mGrid.setAdapter(adapter);
            mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    MovieInfo info = (MovieInfo) adapterView.getItemAtPosition(i);
                    stortDetailActivity(info);
                }

            });
            adapter.notifyDataSetChanged();
        }
    }

    private void stortDetailActivity(MovieInfo info)
    {
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra(DetailActivity.TITLE_KEY,info.title);
        intent.putExtra(DetailActivity.OVERVIEW_KEY,info.overview);
        intent.putExtra(DetailActivity.RELEASE_KEY,info.release_date);
        intent.putExtra(DetailActivity.IMAGE_PATH_KEY,info.poster_path);
        intent.putExtra(DetailActivity.RATING_KEY,info.vote_average);
        intent.putExtra(DetailActivity.RUNTIME_KEY,info.runtime);
        startActivity(intent);
    }

    //------------------
    //  View Adapter
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
                iv.setMaxHeight(500);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setBackgroundColor(Color.parseColor("#115517"));
            }
            else
            {
                iv = (ImageView) view;
            }

            Uri imageUri = MovieDbUrlBuilder.getMovieImageURL(mItemColleciton.results[i].poster_path, ImageSize.IMAGE_MEDIUM);
            if(imageUri != null)
            {
                RequestCreator req = Picasso.get().load(imageUri);
                req.into(iv);

            }
            else
            {
                iv.setBackgroundColor(Color.parseColor("#999999"));
                iv.setImageResource(R.drawable.placeholder);
            }

            iv.setAdjustViewBounds(true);

            return iv;
        }
    }

    //------------------
    //  Network Loader
    //------------------

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

            MovieCollection coll = null;
            if(jsonResult != null)
            {
                coll = MovieCollection.parseJson(jsonResult);
            }
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
