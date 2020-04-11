package com.udacity.popularmovies.ui.moviedetails;

/**
 * Utility class for formatting texts
 */
public class DisplayUtils
{
    public static String parseMovieLengthText(int movieLength)
    {
        if(movieLength == 0) return "";
        return movieLength + " min";
    }
    public static String formatDateText(String dateString)
    {
        if(dateString == null) return "";
        String parsed = "";

        String[] date = dateString.split("-");
        if(date.length>1)
        {
            int monthNumber = Integer.parseInt(date[1]);
            parsed += parseMonth(monthNumber);
        }
        parsed += " ";
        parsed += date[0];

        return parsed;
    }
    public static String parseMonth(int monthNumber)
    {
        switch(monthNumber)
        {
            default:return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
        }
    }
    public static String formatRatingText(float rating)
    {
        if(rating == 0) return "?";

        if(rating >= 10)
            return String.valueOf((int)rating);
        else
            return String.valueOf(rating);
    }
}
