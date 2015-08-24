package com.xabarin.app.popularmovies.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.FavMovieEntry;

import java.util.HashSet;

/**
 * Created by francisco on 10/08/15.
 */
public class TestDb extends AndroidTestCase {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    public static final String LOG_TAG = TestDb.class.getSimpleName();

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

    // Since we want each deleteMe to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(PopularMoviesDBHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each deleteMe is executed to delete the database.  This makes
        sure that we always have a clean deleteMe.
    */
    public void setUp() {
        deleteTheDatabase();
        createDb();
    }

    public void createDb() {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieEntry.TABLE_NAME);
        tableNameHashSet.add(FavMovieEntry.TABLE_NAME);

        deleteTheDatabase();
        SQLiteDatabase db = new PopularMoviesDBHelper(mContext).getWritableDatabase();

        // Validate the DB is open
        assertEquals(true, db.isOpen());

        // Validate there are tables created
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error, This means that the database has not been created correctly",
                c.moveToFirst());

        // Validate that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while ( c.moveToNext() );

        assertTrue("ERROR: The database without movie table.",
                tableNameHashSet.isEmpty());

        //
        //
        //  TESTING For movie TABLE
        //


        // now,do the tables contains the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieEntry.TABLE_NAME + ")",
                null);


        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MovieEntry._ID);
        locationColumnHashSet.add(MovieEntry.COLUMN_TITLE);
        locationColumnHashSet.add(MovieEntry.COLUMN_OVERVIEW);
        locationColumnHashSet.add(MovieEntry.COLUMN_POSTER_URL);
        locationColumnHashSet.add(MovieEntry.COLUMN_RELEASE_DATE);
        locationColumnHashSet.add(MovieEntry.COLUMN_VOTE_AVERAGE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());


        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    public void testMovieTableExists() {
        SQLiteDatabase db = new PopularMoviesDBHelper(mContext).getReadableDatabase();


        // now,do the tables contains the correct columns?
        Cursor c = db.rawQuery("PRAGMA table_info(" + MovieEntry.TABLE_NAME + ")",
                null);


        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MovieEntry._ID);
        locationColumnHashSet.add(MovieEntry.COLUMN_TITLE);
        locationColumnHashSet.add(MovieEntry.COLUMN_OVERVIEW);
        locationColumnHashSet.add(MovieEntry.COLUMN_POSTER_URL);
        locationColumnHashSet.add(MovieEntry.COLUMN_RELEASE_DATE);
        locationColumnHashSet.add(MovieEntry.COLUMN_VOTE_AVERAGE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());


        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    public void testFavMovieTableExists() {
        SQLiteDatabase db = new PopularMoviesDBHelper(mContext).getReadableDatabase();


        // now,do the tables contains the correct columns?
        Cursor c = db.rawQuery("PRAGMA table_info(" + FavMovieEntry.TABLE_NAME + ")",
                null);


        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MovieEntry._ID);
        locationColumnHashSet.add(MovieEntry.COLUMN_TITLE);
        locationColumnHashSet.add(MovieEntry.COLUMN_OVERVIEW);
        locationColumnHashSet.add(MovieEntry.COLUMN_POSTER_URL);
        locationColumnHashSet.add(MovieEntry.COLUMN_RELEASE_DATE);
        locationColumnHashSet.add(MovieEntry.COLUMN_VOTE_AVERAGE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());


        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
