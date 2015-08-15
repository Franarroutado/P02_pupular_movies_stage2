package com.xabarin.app.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;


/**
 * Created by francisco on 10/08/15.
 */
public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE ";
    private static final String SQL_TEXT_NOT_NULL = " TEXT NOT NULL ";
    private static final String SQL_INTEGER_NOT_NULL = " INTEGER NOT NULL ";
    private static final String SQL_INTEGER_PK_AUTOINCREMENT = "  INTEGER PRIMARY KEY AUTOINCREMENT ";

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String COMMA = ",";
        final String SQL_CREATE_MOVIE_TABLE =
                SQL_CREATE_TABLE + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + SQL_INTEGER_PK_AUTOINCREMENT + COMMA +
                        MovieEntry.COLUMN_TITLE + SQL_TEXT_NOT_NULL + COMMA +
                        MovieEntry.COLUMN_OVERVIEW + SQL_TEXT_NOT_NULL + COMMA +
                        MovieEntry.COLUMN_POSTER_URL + SQL_TEXT_NOT_NULL + COMMA +
                        MovieEntry.COLUMN_RELEASE_DATE + SQL_INTEGER_NOT_NULL + COMMA +
                        MovieEntry.COLUMN_POPULARITY + SQL_INTEGER_NOT_NULL + COMMA +
                        MovieEntry.COLUMN_VOTE_AVERAGE + SQL_INTEGER_NOT_NULL + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL(SQL_DROP_TABLE + PopularMoviesContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
