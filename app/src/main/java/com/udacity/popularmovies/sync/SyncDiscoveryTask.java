package com.udacity.popularmovies.sync;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.google.gson.Gson;
import com.udacity.popularmovies.ui.DiscoveryMode;
import com.udacity.popularmovies.settings.AppPreferences;
import com.udacity.popularmovies.settings.DiscoveryCache;
import com.udacity.popularmovies.themoviedb.api.MovieApi;
import com.udacity.popularmovies.themoviedb.api.data.MovieInfo;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

/**
 * Tasks for the sync of the cached data for the movie discovery
 * Also handles weather to load data from api or from the db (favourites)
 */
public class SyncDiscoveryTask
{

    // TODO: 06.04.2020 set to real time
    private static final int TIME_INTERVAL_MIN_HOURS = 12;
    private static final int TIME_INTERVAL_MAX_HOURS = 24;
    private static final TimeUnit UNIT_FOR_SCHEDULER = TimeUnit.MINUTES;

    private static final String SCHEDULED_SYNC_TASK_TAG = "scheduled-cache-sync";
    private static boolean sInitialized;

    public static synchronized void initialize(Context context)
    {
        if(sInitialized) return;

        sInitialized = true;

        scheduleReoccurringUpdates(context);
    }

    public static synchronized void scheduleReoccurringUpdates(Context context)
    {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                UpdateCacheWorker.class,
                TIME_INTERVAL_MIN_HOURS, UNIT_FOR_SCHEDULER,
                TIME_INTERVAL_MAX_HOURS,UNIT_FOR_SCHEDULER)
                .setConstraints(constraints)
                .addTag(SCHEDULED_SYNC_TASK_TAG)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(SCHEDULED_SYNC_TASK_TAG, ExistingPeriodicWorkPolicy.KEEP,workRequest);
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.getId()).observe(getLifecycleOwner(context), new Observer<WorkInfo>(){
            @Override
            public void onChanged(WorkInfo workInfo) {
                Log.d(SCHEDULED_SYNC_TASK_TAG,"Worker state changed: " + workInfo.getState().toString());
            }
        });
    }

    private static LifecycleOwner getLifecycleOwner(Context context)
    {
        while (!(context instanceof LifecycleOwner))
        {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (LifecycleOwner) context;
    }

    public static synchronized void syncCachedDiscoveryData(Context context)
    {
        DiscoveryMode mode = AppPreferences.getLatestDiscoveryMode(context);
        MovieApi api = new MovieApi();

        if(mode == DiscoveryMode.FAVOURITES)
        {
            // todo use database to pull the data
        }
        else
        {
            String popularMovies = api.getMoviesByPopularityJson();
            String bestRatedMovies = api.getMoviesByRatingJson();

            /**
             * The json string can be 'null' when an http error occurs (IO exception)
             * That can lead to falsely write data into the preferences
             * and prevent updating the movies At the first start
             */
            if(popularMovies == null || popularMovies.equals("null")
                || bestRatedMovies == null || bestRatedMovies.equals("null"))
            {
                return;
            }

            DiscoveryCache cache = new DiscoveryCache(popularMovies,bestRatedMovies);
            AppPreferences.updateDiscoveryCache(context,cache);
        }
    }

}
