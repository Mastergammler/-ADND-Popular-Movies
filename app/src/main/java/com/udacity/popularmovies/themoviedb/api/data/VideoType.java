package com.udacity.popularmovies.themoviedb.api.data;

import androidx.annotation.NonNull;

//Math.round((float)info.revenue / MILLION);
public enum VideoType
{
    TRAILER,
    TEASER,
    CLIP,
    FEATURETTE,
    BEHIND_THE_SCENES,
    BLOOPERS;

    public static final String TEASER_STR = "Teaser";
    public static final String CLIP_STR = "Clip";
    public static final String TRAILER_STR = "Trailer";
    public static final String BLOOPERS_STR = "Bloopers";
    public static final String FEATURETTE_STR = "Featurette";
    public static final String BEHIND_THE_SCENES_STR = "Behind the scenes";

    public static VideoType of(int ordinal)
    {
        return values()[ordinal];
    }

    @NonNull
    @Override
    public String toString()
    {
        String converted = super.toString();

        switch (this)
        {
            case TEASER: converted = TEASER_STR; break;
            case CLIP: converted = CLIP_STR; break;
            case TRAILER: converted = TRAILER_STR; break;
            case BLOOPERS: converted = BLOOPERS_STR; break;
            case FEATURETTE: converted = FEATURETTE_STR; break;
            case BEHIND_THE_SCENES: converted = BEHIND_THE_SCENES_STR; break;
        }

        return converted;
    }

    public static VideoType toEnum(String name)
    {
        switch (name)
        {
            case TEASER_STR: return TEASER;
            case CLIP_STR: return CLIP;
            case TRAILER_STR: return TRAILER;
            case BLOOPERS_STR: return BLOOPERS;
            case FEATURETTE_STR: return FEATURETTE;
            case BEHIND_THE_SCENES_STR: return BEHIND_THE_SCENES;
            default: return TRAILER;
        }
    }
}
