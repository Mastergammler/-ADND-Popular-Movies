package com.udacity.popularmovies.ui.main;

/**
 * The the movies get discovered by
 */
public enum DiscoveryMode
{
    POPULAR_DESC,
    USER_RATING_DESC,
    FAVOURITES;

    public static DiscoveryMode of(int value)
    {
        return DiscoveryMode.values()[value];
    }
}
