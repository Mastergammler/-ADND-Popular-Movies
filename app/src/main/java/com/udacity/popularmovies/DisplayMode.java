package com.udacity.popularmovies;

import androidx.annotation.NonNull;

/**
 * Enum for describing different display view modes
 */
public enum DisplayMode
{
    GRID_1x1,
    GRID_2x2,
    GRID_3x3,
    GRID_4x4;

    @NonNull
    @Override
    public String toString() {
        switch (this)
        {
            case GRID_1x1: return "1x1";
            case GRID_2x2: return "2x2";
            case GRID_3x3: return "3x3";
            case GRID_4x4: return "4x4";
            default: throw new IllegalArgumentException("No string specified for enum: " + super.toString());
        }
    }
}
