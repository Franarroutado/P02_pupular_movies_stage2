package com.xabarin.app.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

/**
 * Created by francisco on 23/08/15.
 */
public class TestPopularMoviesDB extends AndroidTestCase {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String LOG_TAG = TestPopularMoviesDB.class.getSimpleName();

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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void deleteAllRecordsFromProvider() {

        // insert our test records into the database
        PopularMoviesDB.deleteAllMovies(mContext);

        // Test all rows are properly deleted.
        Cursor cursor = PopularMoviesDB.getAllMovies(mContext);
        assertEquals("Error: Records not deleted from movie table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void testInsertNewMovie() {

        // Delete all records
        deleteAllRecordsFromProvider();

        ContentValues myMockValues = TestUtilities.createPopularMovieRecord();
        PopularMoviesDB.addMovie(mContext, myMockValues);
        Cursor cursor = PopularMoviesDB.getAllMovies(mContext);
        TestUtilities.validateCursor(LOG_TAG, cursor, myMockValues);
    }

    public void testBulkInsertMovies() {

        // Delete all records
        deleteAllRecordsFromProvider();

        ContentValues[] myMockValuesCollection = new ContentValues[10];

        for (int i = 0; i <= 9; i++) {
            myMockValuesCollection[i] = TestUtilities.createPopularMovieRecord();;
        }

        PopularMoviesDB.addMovieCollection(mContext, myMockValuesCollection);
        Cursor cursor = PopularMoviesDB.getAllMovies(mContext);

        assertEquals("Error: Bulk error", myMockValuesCollection.length, cursor.getCount());

        cursor.close();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
