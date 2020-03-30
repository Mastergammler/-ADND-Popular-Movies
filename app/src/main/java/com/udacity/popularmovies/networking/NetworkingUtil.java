package com.udacity.popularmovies.networking;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkingUtil
{

    public static URL parseUri(Uri uri)
    {
        URL url = null;
        try
        {
            url = new URL(uri.toString());
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpRequest(URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Log.i(NetworkingUtil.class.getSimpleName(),"Request for: " + url.toString());
        try
        {
            InputStream in = connection.getInputStream();

            Scanner s = new Scanner(in);
            s.useDelimiter("\\A");

            if(s.hasNext())
                return s.next();
            else
                return null;
        }
        finally
        {
            connection.disconnect();
        }
    }




}
