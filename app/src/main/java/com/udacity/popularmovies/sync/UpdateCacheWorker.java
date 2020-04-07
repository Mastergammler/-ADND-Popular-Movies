package com.udacity.popularmovies.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Worker for the scheduled update of the cached movie data
 */
public class UpdateCacheWorker extends Worker
{
    public UpdateCacheWorker(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        SyncDiscoveryTask.syncCachedDiscoveryData(getApplicationContext());
        return Result.success();
    }

}
