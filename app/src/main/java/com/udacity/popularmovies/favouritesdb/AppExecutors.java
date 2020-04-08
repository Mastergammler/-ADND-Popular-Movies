package com.udacity.popularmovies.favouritesdb;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executors for handling the db events
 */
public class AppExecutors
{
    private static AppExecutors sInstance;
    private static final Object LOCK = new Object();

    private Executor mDiskExecutor;
    private Executor mMainThreadExecutor;

    private AppExecutors(Executor discIO, Executor mainThread)
    {
        mDiskExecutor = discIO;
        mMainThreadExecutor = mainThread;
    }

    public static AppExecutors getInstance()
    {
        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                sInstance = new AppExecutors(
                        Executors.newSingleThreadExecutor(),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public void runOnDiskIOThread(Runnable command)
    {
        mDiskExecutor.execute(command);
    }

    public void runOnMainThread(Runnable command)
    {
        mMainThreadExecutor.execute(command);
    }

    private static class MainThreadExecutor implements Executor
    {
        private android.os.Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
