package com.xabarin.app.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.FavMovieEntry;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by francisco on 15/08/15.
 */
public class TestUtilities extends AndroidTestCase {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

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
        testValues.put(MovieEntry.COLUMN_ID, randInt(100000, 999999));
        testValues.put(MovieEntry.COLUMN_RELEASE_DATE, "2015-06-12");
        testValues.put(MovieEntry.COLUMN_OVERVIEW, "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.");
        testValues.put(MovieEntry.COLUMN_POSTER_URL, "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg");
        testValues.put(MovieEntry.COLUMN_TITLE, "Jurassic World");
        testValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, 7.1);

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
            Log.v("xx", entry.getValue().toString() + " is equas " + valueCursor.getString(idx));
        }
    }

    static Long insertPopularMovieValues(Context context, ContentValues contentValues) {
        // insert our test records into the database
        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        long popularMovieRowId =
                db.insert(MovieEntry.TABLE_NAME, null, contentValues);

        // Verify we have a row back
        assertTrue("Error: Failure to insert test PopularMovie record", popularMovieRowId != -1);

        db.close();

        return popularMovieRowId;
    }

    static Long insertPopularFavMovieValues(Context context, ContentValues contentValues) {
        // insert our test records into the database
        PopularMoviesDBHelper dbHelper = new PopularMoviesDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long popularMovieRowId =
                db.insert(FavMovieEntry.TABLE_NAME, null, contentValues);

        // Verify we have a row back
        assertTrue("Error: Failure to insert test PopularMovie record", popularMovieRowId != -1);

        db.close();

        return popularMovieRowId;
    }

    /**
     * This aux function help to create random ID when testing bulks inserts
     * @param min
     * @param max
     * @return
     */
    private static int randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
