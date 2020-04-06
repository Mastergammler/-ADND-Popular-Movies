package com.udacity.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * Intent service for updating the cached discovery data with new one from the Internet
 * Actual API calls are here executed
 */
public class SyncDiscoveryDataIntentService extends IntentService
{
    public static final String SERVICE_NAME = "SyncDiscoveryCacheService";

    public SyncDiscoveryDataIntentService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        SyncDiscoveryTask.syncCachedDiscoveryData(this);
    }
}
