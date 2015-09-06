package com.xabarin.app.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.FavMovieEntry;

/**
 * This class takes care to backing up the movies requested by REST services against http://api.themoviedb.org
 */
public class PopularMoviesDB {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private final static String LOG_TAG = PopularMoviesDB.class.getSimpleName();

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

    private static Long insert(Context context, ContentValues values) {
        // insert our test records into the database
        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long popularMovieRowId =
                db.insert(MovieEntry.TABLE_NAME, null, values);

        db.close();

        return popularMovieRowId;
    }

    public static Long insertFavMovie(Context context, ContentValues values) {
        // insert our test records into the database
        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long popularMovieRowId =
                db.insert(FavMovieEntry.TABLE_NAME, null, values);

        db.close();

        return popularMovieRowId;
    }



    /**
     * This method clears all data from the table. It is the closest thing to the TRUNCATE TABLE.
     * @param context
     */
    private static void truncate(Context context) {

        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + MovieEntry.TABLE_NAME + ";VACUUM;");
        db.close();

    }

    private static Cursor query(Context context) {

        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    public static Boolean existFabMovie(Context context, String idMovie) {

        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.v(LOG_TAG, "Exists " + idMovie);
        String Query = "Select * from " + FavMovieEntry.TABLE_NAME + " where " + FavMovieEntry.COLUMN_ID + "=" + idMovie;
        Log.v(LOG_TAG, "Query " + Query);
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private static int bulkInsert(Context context, ContentValues[] values) {
        final PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, value);
                Log.v(LOG_TAG, "New row inserted" + _id);
                if (_id != -1) returnCount++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return  returnCount;
    }

    public static Cursor getAllMovies(Context context) {
        return query(context);
    }

    public static Long addMovie(Context context, ContentValues movie) {
        return insert(context, movie);
    }

    public static void deleteAllMovies(Context context) {
        truncate(context);
    }


    public static int addMovieCollection(Context context, ContentValues[] contentValues) {
        return context.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, contentValues);
        // return bulkInsert(context, contentValues);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
