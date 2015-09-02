package com.xabarin.app.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

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
    public static final String CONTENT_AUTHORITY = "com.xabarin.app.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = MovieEntry.TABLE_NAME;
    public static final String PATH_FAV_MOVIE = FavMovieEntry.TABLE_NAME;

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
//    public static long normalizeDate(long startDate) {
//        // normalize the start date to the beginning of the (UTC) day
//        Time time = new Time();
//        time.set(startDate);
//        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
//        return time.setJulianDay(julianDay);
//    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public static final class MovieEntry  implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // Thist two constants stores the MIME type used by getType function in the custom Content
        // provider to identify the type of content this provider replies.
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;

        // SQLite table name
        public static final String TABLE_NAME =            "movie";

        // SQLite columns name
        public static final String COLUMN_ID =             _ID;
        public static final String COLUMN_TITLE =          "title";
        public static final String COLUMN_POSTER_URL =     "poster";
        public static final String COLUMN_OVERVIEW =       "overview";
        public static final String COLUMN_VOTE_AVERAGE =   "vote_average";
        public static final String COLUMN_RELEASE_DATE =   "release_date";

        // This is the order for the queries function
        public static final String[] MOVIES_COLUMNS = {
                _ID,
                COLUMN_TITLE,
                COLUMN_POSTER_URL,
                COLUMN_OVERVIEW,
                COLUMN_VOTE_AVERAGE,
                COLUMN_RELEASE_DATE
        };

        // These indices are tied to MOVIES_COLUMNS.  If MOVIES_COLUMNS changes, these
        // must change.
        public static final int CURSOR_COLUMN_INDEX_FOR_ID =            0;
        public static final int CURSOR_COLUMN_INDEX_FOR_TITLE =         1;
        public static final int CURSOR_COLUMN_INDEX_FOR_POSTER_URL =    2;
        public static final int CURSOR_COLUMN_INDEX_FOR_OVERVIEW =      3;
        public static final int CURSOR_COLUMN_INDEX_FOR_VOTE_AVERAGE =  4;
        public static final int CURSOR_COLUMN_INDEX_FOR_RELEASE_DATE =  5;


        /**
         *  Return the Uri used by the ContentProvider "insert" method
         * @param id
         * @return Uri
         */
        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Returns the Movie ID value
         * @param uri
         * @return Long
         */
        public static Long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }


    public static final class FavMovieEntry implements BaseColumns {

        // Ordering code based on github contributor https://github.com/sockeqwe
        // ===========================================================
        // Final Fields
        // ===========================================================

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIE).build();


        // This two constants stores the MIME type used by getType function in the custom Content
        // provider to identify the type of content this provider replies.
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV_MOVIE;

        // SQLite table name
        public static final String TABLE_NAME =            "fav_movie";

        // SQLite columns name
        public static final String COLUMN_TITLE =          "title";
        public static final String COLUMN_POSTER_URL =     "poster";
        public static final String COLUMN_OVERVIEW =       "overview";
        public static final String COLUMN_VOTE_AVERAGE =   "vote_average";
        public static final String COLUMN_RELEASE_DATE =   "release_date";

        // This is the order for the queries function
        public static final String[] MOVIES_COLUMNS = {
                _ID,
                COLUMN_TITLE,
                COLUMN_POSTER_URL,
                COLUMN_OVERVIEW,
                COLUMN_VOTE_AVERAGE,
                COLUMN_RELEASE_DATE
        };

        // These indices are tied to MOVIES_COLUMNS.  If MOVIES_COLUMNS changes, these
        // must change.
        public static final int CURSOR_COLUMN_INDEX_FOR_ID =            0;
        public static final int CURSOR_COLUMN_INDEX_FOR_TITLE =         1;
        public static final int CURSOR_COLUMN_INDEX_FOR_POSTER_URL =    2;
        public static final int CURSOR_COLUMN_INDEX_FOR_OVERVIEW =      3;
        public static final int CURSOR_COLUMN_INDEX_FOR_VOTE_AVERAGE =  4;
        public static final int CURSOR_COLUMN_INDEX_FOR_RELEASE_DATE =  5;

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


        /**
         *  Return the Uri used by the ContentProvider "insert" method
         * @param id
         * @return Uri
         */
        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Returns the Movie ID value
         * @param uri
         * @return Long
         */
        public Long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }
}
