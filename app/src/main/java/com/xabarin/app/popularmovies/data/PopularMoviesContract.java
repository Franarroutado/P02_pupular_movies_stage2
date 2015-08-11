package com.xabarin.app.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by francisco on 10/08/15.
 */
public class PopularMoviesContract {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.xabarin.apps.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movie";

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URY =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // SQLite table name
        public static final String TABLE_NAME =            "movie";

        // SQLite columns name
        public static final String COLUMN_TITLE =          "title";
        public static final String COLUMN_POSTER_URL =     "poster";
        public static final String COLUMN_OVERVIEW =       "overview";
        public static final String COLUMN_VOTE_AVERAGE =   "vote_average";
        public static final String COLUMN_RELEASE_DATE =   "release_date";

    }
}
