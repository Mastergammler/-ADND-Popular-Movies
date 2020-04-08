package com.udacity.popularmovies.favouritesdb.Entitites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import androidx.room.TypeConverter;

public class ImageConverter
{
    @TypeConverter
    public String convertImage(Bitmap image)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100,output);
        return Base64.encodeToString(output.toByteArray(),Base64.DEFAULT);
    }

    @TypeConverter
    public Bitmap convertString(String base64Image)
    {
        byte[] imageBytes = Base64.decode(base64Image,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
    }
}
