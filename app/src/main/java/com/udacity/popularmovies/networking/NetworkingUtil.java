package com.udacity.popularmovies.networking;

import android.net.Uri;

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
