package com.xabarin.app.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.xabarin.app.popularmovies.data.PopularMoviesContract.FavMovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;

import static com.xabarin.app.popularmovies.data.PopularMoviesContract.CONTENT_AUTHORITY;
import static com.xabarin.app.popularmovies.data.PopularMoviesContract.PATH_FAV_MOVIE;
import static com.xabarin.app.popularmovies.data.PopularMoviesContract.PATH_MOVIE;

/**
 * Created by francisco on 23/08/15.
 */
public class PopularMoviesProvider extends ContentProvider {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String LOG_TAG = PopularMoviesProvider.class.getSimpleName();

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int POPULAR_MOVIES = 100;
    static final int POPULAR_MOVIES_BY_ID = 101;
    static final int POPULAR_FAV_MOVIES = 200;
    static final int POPULAR_FAV_MOVIES_BY_ID = 201;


    /**
     * Represents the MOVIE._ID = ?
     */
    private static final String sMovieSelectionByID =
        MovieEntry.TABLE_NAME + "." + MovieEntry._ID + " = ?";

    /**
     * Represents the FAV_MOVIE._ID = ?
     */
    private static final String sFavMovieSelectionByID =
            FavMovieEntry.TABLE_NAME + "." + FavMovieEntry._ID + " = ?";

    // ===========================================================
    // Fields
    // ===========================================================

    private PopularMoviesDBHelper mMoviesDBHelper;

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
    public boolean onCreate() {
        mMoviesDBHelper = new PopularMoviesDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        switch (sUriMatcher.match(uri)) {
            case POPULAR_MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case POPULAR_FAV_MOVIES:
                return FavMovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor myCursor;
        switch (sUriMatcher.match(uri)) {
            // "movie"
            case POPULAR_MOVIES :
                myCursor = mMoviesDBHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case POPULAR_MOVIES_BY_ID :
                myCursor = getMovieById(uri, projection, sortOrder);
                break;
            // "fav_movie"
            case POPULAR_FAV_MOVIES :
                myCursor = mMoviesDBHelper.getReadableDatabase().query(
                        FavMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder);
                break;
            case POPULAR_FAV_MOVIES_BY_ID :
                myCursor = getFavMovieById(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        myCursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.v(LOG_TAG, "Query successfully contains " + myCursor.getCount()+ " rows!.");
        return myCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            // "movie"
            case POPULAR_MOVIES : {
                long _id = mMoviesDBHelper.getWritableDatabase().insert(
                        MovieEntry.TABLE_NAME,
                        null,
                        values);
                if (_id > 0) {
                    returnUri = FavMovieEntry.buildMoviesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            // "fav_movie"
            case POPULAR_FAV_MOVIES : {
                long _id = mMoviesDBHelper.getWritableDatabase().insert(
                        FavMovieEntry.TABLE_NAME,
                        null,
                        values);
                if (_id > 0) {
                    returnUri = FavMovieEntry.buildMoviesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (sUriMatcher.match(uri)) {
            // "movie"
            case POPULAR_MOVIES : {
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().update(
                        MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            // "fav_movie"
            case POPULAR_FAV_MOVIES : {
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().update(
                        FavMovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("The delete opertaion not supported: " + uri);
        }

        // A null value deletes all rows. In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case POPULAR_MOVIES:
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().delete(
                        MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case POPULAR_MOVIES_BY_ID:
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().delete(
                        FavMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case POPULAR_FAV_MOVIES:
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().delete(
                        FavMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case POPULAR_FAV_MOVIES_BY_ID:
                rowsDeleted = mMoviesDBHelper.getWritableDatabase().delete(
                        FavMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("The delete operation not supported: " + uri);
        }

        // A null value deletes all rows. In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case POPULAR_MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                Log.v(LOG_TAG, "BulkInsert successfully inserted " + returnCount + " rows!.");
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mMoviesDBHelper.close();
        super.shutdown();
    }


    // ===========================================================
    // Methods
    // ===========================================================

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // PopularMoviesContract to help define the types to the UriMatcher.
        sURIMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE, POPULAR_MOVIES);
        sURIMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE + "/#", POPULAR_MOVIES_BY_ID);
        sURIMatcher.addURI(CONTENT_AUTHORITY, PATH_FAV_MOVIE + "/#", POPULAR_FAV_MOVIES_BY_ID);
        sURIMatcher.addURI(CONTENT_AUTHORITY, PATH_FAV_MOVIE, POPULAR_FAV_MOVIES);

        // 3) Return the new matcher!
        return sURIMatcher;
    }

    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder) {
        Long lngMovieID = MovieEntry.getIdFromUri(uri);

        String selection;
        String[] selectionArgs;
        if (lngMovieID == 0) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = sMovieSelectionByID;
            selectionArgs = new String[]{ lngMovieID.toString() };
        }

        return mMoviesDBHelper.getReadableDatabase().query(
                MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, sortOrder);
    }

    private Cursor getFavMovieById(Uri uri, String[] projection, String sortOrder) {
        Long lngMovieID = FavMovieEntry.getIdFromUri(uri);

        String selection;
        String[] selectionArgs;
        if (lngMovieID == 0) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = sFavMovieSelectionByID;
            selectionArgs = new String[]{ lngMovieID.toString() };
        }

        return mMoviesDBHelper.getReadableDatabase().query(
                FavMovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, sortOrder);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
