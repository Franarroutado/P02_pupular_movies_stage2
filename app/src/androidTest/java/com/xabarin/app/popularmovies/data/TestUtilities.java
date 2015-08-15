package com.xabarin.app.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by francisco on 15/08/15.
 */
public class TestUtilities extends AndroidTestCase {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

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

    static ContentValues createPopularMovieRecord() {

        ContentValues testValues = new ContentValues();
        testValues.put(MovieEntry.COLUMN_RELEASE_DATE, TEST_DATE);
        testValues.put(MovieEntry.COLUMN_OVERVIEW, "Overview test movie");
        testValues.put(MovieEntry.COLUMN_POPULARITY, "22");
        testValues.put(MovieEntry.COLUMN_POSTER_URL, "www.google.com");
        testValues.put(MovieEntry.COLUMN_TITLE, "Popular movie title");
        testValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, "33");

        return testValues;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static Long insertPopularMovieValues(Context context) {
        // insert our test records into the database
        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = createPopularMovieRecord();

        long popularMovieRowId =
                db.insert(MovieEntry.TABLE_NAME, null, testValues);

        // Verify we have a row back
        assertTrue("Error: Failure to insert test PopularMovie record", popularMovieRowId != -1);

        return popularMovieRowId;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
