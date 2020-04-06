package com.udacity.popularmovies.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.udacity.popularmovies.DiscoveryMode;
import com.udacity.popularmovies.MainActivity;

/**
 * Handles the Shared Preferences for the App
 * Saves some of the current view states (DISCOVER BY - VIEW GRID)
 * And is also responsible for caching the current discovery of movies
 */
public class AppPreferences
{
    //#################
    //##  CONSTANTS  ##
    //#################

    private static final DiscoveryMode DEFAULT_DISCOVERY_MODE = DiscoveryMode.POPULAR_DESC;

    //---------------------
    //  User Preferences
    //---------------------

    private static final String USER_PREFERENCES_ID = "pop-movies-default-pref";

    private static final String UP_LATEST_DISCOVERY_MODE_KEY = "up-discovery-mode";
    private static final String UP_GRID_VIEW_PREF_DEF_KEY = "up-grid-view-default";
    private static final String UP_GRID_VIEW_PREF_LAND_KEY = "up-grid-view-land";
    private static final String UP_SAVE_PICTURES_IN_DB_KEY = "up-save-pictures";

    //------------------------
    //  Caching Preferences
    //------------------------

    private static final String CACHING_PREFERENCES_ID = "pop-movies-discovery-cache";

    private static final String CP_POPULAR_MOVIES_JSON_KEY = "cp-popular";
    private static final String CP_HIGHEST_RATED_MOVIES_JSON_KEY = "cp-highest-rated";

    //#################
    //##  FUNCTIONS  ##
    //#################

    public static SharedPreferences getUserPreferences(Context context)
    {
        return context.getSharedPreferences(USER_PREFERENCES_ID,Context.MODE_PRIVATE);
    }

    public static void updateLatestDiscoveryMode(Context context, DiscoveryMode mode)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREFERENCES_ID,Context.MODE_PRIVATE).edit();
        editor.putInt(UP_LATEST_DISCOVERY_MODE_KEY,mode.ordinal());
        editor.apply();
    }

    public static DiscoveryMode getLatestDiscoveryMode(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(USER_PREFERENCES_ID,Context.MODE_PRIVATE);
        int discoveryOrdinal = prefs.getInt(UP_LATEST_DISCOVERY_MODE_KEY,DEFAULT_DISCOVERY_MODE.ordinal());
        return DiscoveryMode.of(discoveryOrdinal);
    }

    /**
     * Get the cached json string from the preferences
     * @param context Application context to get the SharedPreferences
     * @return null if nothing was saved yet
     */
    public static String getCurrentDiscoveryCache(Context context)
    {
        SharedPreferences userPrefs = context.getSharedPreferences(USER_PREFERENCES_ID,Context.MODE_PRIVATE);
        SharedPreferences cachePrefs = context.getSharedPreferences(CACHING_PREFERENCES_ID,Context.MODE_PRIVATE);
        int discoveryOrdinal = userPrefs.getInt(UP_LATEST_DISCOVERY_MODE_KEY,DEFAULT_DISCOVERY_MODE.ordinal());
        DiscoveryMode currentMode = DiscoveryMode.of(discoveryOrdinal);
        return cachePrefs.getString(keyOf(currentMode),null);
    }

    public static void updateDiscoveryCache(Context context,String popularMoviesJson,String highestRatedJson)
    {
        SharedPreferences prefs = context.getSharedPreferences(CACHING_PREFERENCES_ID,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if( ! isNullOrEmpty(popularMoviesJson))
        {
            editor.putString(CP_POPULAR_MOVIES_JSON_KEY,popularMoviesJson);
        }
        if( ! isNullOrEmpty(highestRatedJson))
        {
            editor.putString(CP_HIGHEST_RATED_MOVIES_JSON_KEY,highestRatedJson);
        }
        editor.apply();
    }

    //-----------
    //  Helper
    //-----------

    private static String keyOf(DiscoveryMode mode)
    {
        switch (mode)
        {
            case POPULAR_DESC: return CP_POPULAR_MOVIES_JSON_KEY;
            case USER_RATING_DESC: return CP_HIGHEST_RATED_MOVIES_JSON_KEY;
            default: throw new IllegalArgumentException("No key specified for mode: " + mode.toString());
        }
    }

    private static boolean isNullOrEmpty(String string)
    {
        return string != null &! string.equals("");
    }

}
