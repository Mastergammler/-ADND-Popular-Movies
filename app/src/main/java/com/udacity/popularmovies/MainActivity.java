package com.udacity.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.DragEvent;
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

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.popularmovies.settings.AppPreferences;
import com.udacity.popularmovies.sync.SyncDiscoveryTask;
import com.udacity.popularmovies.themoviedb.IMovieDbApi;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.ImageSize;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieInfo[]>
{
    //#################
    //##  CONSTANTS  ##
    //#################

    private static final int SYNC_DISCOVERY_CACHE_LOADER_ID = 344382;
    private static final String GRID_VIEW_SCROLL_POSITION_KEY = "grid-view-scroll-state";

    //------------
    //  Members
    //------------

    private GridView mGrid;
    private TextView mErrorMessageText;
    private ProgressBar mLoadingIndicator;

    private DragEvent mLastEvent;

    private IMovieDbApi mMovieApi;

    //----------------
    //  Android Init
    //----------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.gv_main_view);
        if(savedInstanceState != null && savedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY) != null)
        {
            mGrid.onRestoreInstanceState(savedInstanceState.getParcelable(GRID_VIEW_SCROLL_POSITION_KEY));
        }

        mErrorMessageText = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieApi = new MovieApi();

        // TEST ONLY
        AppPreferences.setPreferredGrid(this,DisplayMode.GRID_3x3);

        setPreferredGridView();
        loadMoviesFor(AppPreferences.getLatestDiscoveryMode(this));
        SyncDiscoveryTask.initialize(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GRID_VIEW_SCROLL_POSITION_KEY,mGrid.onSaveInstanceState());
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
        DiscoveryMode selectedMode;

        switch (item.getItemId())
        {
            case R.id.action_sort_by_popular:
                selectedMode = DiscoveryMode.POPULAR_DESC;
                break;
            case R.id.action_sort_by_rating:
                selectedMode = DiscoveryMode.USER_RATING_DESC;
                break;
            default: return super.onOptionsItemSelected(item);
        }

        AppPreferences.updateLatestDiscoveryMode(this,selectedMode);
        loadMoviesFor(selectedMode);
        return true;
    }

    //------------
    //  Methods
    //------------

    private void setPreferredGridView()
    {
        DisplayMode mode = AppPreferences.getPreferredGrid(this);
        int currentOrientation = getResources().getConfiguration().orientation;

        if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            mGrid.setNumColumns((mode.ordinal() + 1) * 2);
        }
        else
        {
            mGrid.setNumColumns((mode.ordinal()+1));
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
                setTitle(R.string.highest_rated_movies);
                break;
        }
        LoaderManager.getInstance(this).restartLoader(SYNC_DISCOVERY_CACHE_LOADER_ID,null,this);
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
        private MovieInfo[] mMovieItems;

        ImageViewAdapter(Context context, MovieInfo[] movies)
        {
            mContext = context;
            mMovieItems = movies;
        }

        @Override
        public int getCount() {
            return mMovieItems.length;
        }
        @Override
        public MovieInfo getItem(int i) {
            return mMovieItems[i];
        }
        @Override
        public long getItemId(int i) {
            return mMovieItems[i].id;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ImageView imageView = createOrCastImageView(view);
            updateImage(imageView,i);

            return imageView;
        }

        private ImageView createOrCastImageView(View view)
        {
            if(view != null) return (ImageView) view;

            ImageView iv = new ImageView(mContext);
            int newWidth = mGrid.getColumnWidth();
            iv.setLayoutParams(new ViewGroup.LayoutParams(newWidth,(int)(1.5*newWidth)));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            return iv;
        }
        private void updateImage(ImageView iv, int itemIndex)
        {
            Uri imageUri = mMovieApi.getMoviePoster(mMovieItems[itemIndex], ImageSize.IMAGE_MEDIUM);
            if(imageUri != null)
            {
                RequestCreator req = Picasso.get().load(imageUri);
                req.into(iv);
            }
            else
            {
                iv.setImageResource(R.drawable.placeholder);
            }
        }
    }

    //------------------
    //  Network Loader
    //------------------

    /**
     * The loader to load the discovery data from the movie api
     * The loader should only be called the first time the app starts
     * After that the data should be cached and updated by a scheduled service
     * !! ONLY CALL WHEN CACHE IS NULL !!
     */
    @NonNull
    @Override
    public Loader<MovieInfo[]> onCreateLoader(int id, @Nullable Bundle args)
    {
        return new AsyncTaskLoader<MovieInfo[]>(this)
        {
            // TODO: 06.04.2020 Loader runs always, checks for null by himself
            
            @Override
            protected void onStartLoading() {
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mGrid.setVisibility(View.INVISIBLE);

                String discovery  = AppPreferences.getCurrentDiscoveryCache(MainActivity.this);
                if(discovery == null)
                {
                    forceLoad();
                }
                else
                {
                    MovieInfo[] movies = new Gson().fromJson(discovery,MovieInfo[].class);
                    deliverResult(movies);
                }
            }

            @Nullable
            @Override
            public MovieInfo[] loadInBackground()
            {
                // TODO: 06.04.2020 Does this actually work? the inten service runs on a background so it doesn`t block the loader thread?
                //Intent intent = new Intent(MainActivity.this, SyncDiscoveryDataIntentService.class);
                //startService(intent);
                SyncDiscoveryTask.syncCachedDiscoveryData(MainActivity.this);
                String discovery = AppPreferences.getCurrentDiscoveryCache(MainActivity.this);
                MovieInfo[] movies = new Gson().fromJson(discovery,MovieInfo[].class);
                return movies;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieInfo[]> loader, MovieInfo[] data)
    {
        mLoadingIndicator.setVisibility(View.GONE);
        loadImages(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieInfo[]> loader) {}

}
