package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.popularmovies.themoviedb.api.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

public class MainActivity extends AppCompatActivity{

    //------------
    //  Members
    //------------

    private GridView mGrid;
    private TextView mErrorMessageText;
    private ProgressBar mLoadingIndicator;

    private IMovieDbApi mMovieApi;

    //----------------
    //  Android Init
    //----------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.gv_main_view);
        mErrorMessageText = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieApi = new MovieApi();

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

    //------------
    //  Methods
    //------------

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
    private void loadImages(MovieInfo[] movies)
    {
        if(movies == null)
        {
            mGrid.setVisibility(View.GONE);
            mErrorMessageText.setVisibility(View.VISIBLE);
        }
        else
        {
            ImageViewAdapter adapter = new ImageViewAdapter(mGrid.getContext(),movies);
            mErrorMessageText.setVisibility(View.GONE);
            mGrid.setVisibility(View.VISIBLE);
            mGrid.setAdapter(adapter);
            mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    MovieInfo info = (MovieInfo) adapterView.getItemAtPosition(i);
                    startDetailActivity(info);
                }

            });
            adapter.notifyDataSetChanged();
        }
    }
    private void startDetailActivity(MovieInfo info)
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
    //    View Adapter
    //------------------

    class ImageViewAdapter extends BaseAdapter
    {
        private Context mContext;
        private MovieInfo[] mItemColleciton;

        ImageViewAdapter(Context context, MovieInfo[] movies)
        {
            mContext = context;
            mItemColleciton = movies;
        }

        @Override
        public int getCount() {
            return mItemColleciton.length;
        }

        @Override
        public MovieInfo getItem(int i) {
            return mItemColleciton[i];
        }

        @Override
        public long getItemId(int i) {
            return mItemColleciton[i].id;
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

            Uri imageUri = mMovieApi.getMoviePoster(mItemColleciton[i], ImageSize.IMAGE_MEDIUM);
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

    class DiscoverMoviesTask extends AsyncTask<DiscoveryMode,Void,MovieInfo[]>
    {
        @Override
        protected void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mGrid.setVisibility(View.INVISIBLE);
        }

        @Override
        protected MovieInfo[] doInBackground(DiscoveryMode... discoveryModes)
        {
            switch (discoveryModes[0])
            {
                case USER_RATING_DESC:
                    return mMovieApi.getMoviesByRating();
                default:return mMovieApi.getMoviesByPopularity();
            }
        }

        @Override
        protected void onPostExecute(MovieInfo[] movies) {
            mLoadingIndicator.setVisibility(View.GONE);
            loadImages(movies);
        }
    }

}
