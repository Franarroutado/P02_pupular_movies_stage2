package com.xabarin.app.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.xabarin.app.popularmovies.data.PopularMoviesContract.CONTENT_AUTHORITY;
import static com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import static com.xabarin.app.popularmovies.data.PopularMoviesContract.PATH_MOVIE;

/**
 * Created by francisco on 12/08/15.
 */
public class PopularMoviesProvider extends ContentProvider {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int POPULAR_MOVIES = 100;
    static final int POPULAR_MOVIES_ID = 101;



    // ORDER BY movie.popularity
    private static final String mOrderByPopularity =
            new StringBuilder()
                    .append(" ORDER BY ")
                    .append(MovieEntry.TABLE_NAME)
                    .append(".")
                    .append(MovieEntry.COLUMN_POPULARITY).toString();


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
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPULAR_MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case POPULAR_MOVIES_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor myCursor;

        switch (sUriMatcher.match(uri)) {
            // "movie"
            case POPULAR_MOVIES: {
                myCursor = getPopularMovies(projection, selection, selectionArgs, sortOrder);
                break;
            }
            // "movie/#"
            case POPULAR_MOVIES_ID: {
                String strWhere = " _id=" + uri.getLastPathSegment();
                myCursor = getPopularMovies(projection, strWhere, selectionArgs, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        myCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return myCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case POPULAR_MOVIES: {
                normalizeDate(values);
                long _id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieEntry.buildMoviesUri(_id);
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
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase(); // get the db

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match) {
            case POPULAR_MOVIES:
                rowsDeleted = db.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
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

        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase(); // get the db

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match) {
            case POPULAR_MOVIES:
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
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
        sURIMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE + "/#", POPULAR_MOVIES_ID);

        // 3) Return the new matcher!
        return sURIMatcher;
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(MovieEntry.COLUMN_RELEASE_DATE)) {
            long dateValue = values.getAsLong(MovieEntry.COLUMN_RELEASE_DATE);
            values.put(MovieEntry.COLUMN_RELEASE_DATE, PopularMoviesContract.normalizeDate(dateValue));
        }
    }

    private Cursor getPopularMovies(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mMoviesDBHelper.getReadableDatabase().query(
                MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
