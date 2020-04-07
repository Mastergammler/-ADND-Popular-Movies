package com.udacity.popularmovies.themoviedb.api.data;

/**
 * Java object for the Movie Review data
 * This class also parses the content data into more usable format
 */
public class MovieReview
{
    public MovieReview(String id,String author, String content, String url)
    {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    //--------------
    //  Api Values
    //--------------

    public String id;
    public String author;
    public String content;
    public String url;

    public String getContentPreview()
    {
        return content.substring(0,15) + " ...";
    }


}
