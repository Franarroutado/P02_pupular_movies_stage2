package com.xabarin.app.popularmovies.ui.main;

import android.content.ContentValues;
import android.util.Log;

import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.model.MoviesAPI;
import com.xabarin.app.popularmovies.model.movies.Movie;
import com.xabarin.app.popularmovies.model.movies.MoviesCollection;
import com.xabarin.app.popularmovies.preferences.PopularMoviesPreferences;

import java.util.Vector;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by francisco on 8/08/15.
 */
public class PopularMoviesPresenter {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String BASE_URL = "http://api.themoviedb.org";
    private static final String LOG_TAG = PopularMoviesPresenter.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private PopularMoviesView mView;
    private PopularMoviesPreferences mPreferences;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PopularMoviesPresenter(PopularMoviesView view) {
        mView = view;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================


    public void requestMovies(String sortBy) {

        RestAdapter myRestAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();
        MoviesAPI myMoviesAPI = myRestAdapter.create(MoviesAPI.class);

        myMoviesAPI.getMoviesByHighestRated(new Callback<MoviesCollection>() {
            @Override
            public void success(MoviesCollection moviesCollection, Response response) {
                insertPopularMovies(moviesCollection);
                mView.onRequestMoviesSuccess(moviesCollection);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Error fetching movies. " + error.toString());
            }
        });
    }

    private String getMoviesdbAPI() {
        if (null == mPreferences) {
            mPreferences = new PopularMoviesPreferences(mView.getViewActivity());
        }
        return mPreferences.getMovieDBAPIKey();
    }

    private void insertPopularMovies(MoviesCollection moviesCollection) {

        // Insert the new weather information into the database
        Vector<ContentValues> cVector = new Vector<ContentValues>(moviesCollection.getResults().size());

        int numInsertedRows = 0;

        for (Movie movie : moviesCollection.getResults())
        {
            ContentValues popularMoviesValues = new ContentValues();
            popularMoviesValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, normalizeValue(movie.getVote_average()));
            popularMoviesValues.put(MovieEntry.COLUMN_TITLE, normalizeValue(movie.getTitle()));
            popularMoviesValues.put(MovieEntry.COLUMN_POSTER_URL, normalizeValue(movie.getPoster_path()));
            popularMoviesValues.put(MovieEntry.COLUMN_POPULARITY, normalizeValue(movie.getPopularity()));
            popularMoviesValues.put(MovieEntry.COLUMN_OVERVIEW, normalizeValue(movie.getOverview()));
            popularMoviesValues.put(MovieEntry.COLUMN_RELEASE_DATE, normalizeValue(movie.getRelease_date()));

            cVector.add(popularMoviesValues);
        }

        // add to database
        if ( cVector.size() > 0) {
            ContentValues [] cvArray = new ContentValues[cVector.size()];
            cVector.toArray(cvArray);
            numInsertedRows = mView.getViewActivity().getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);

            if (numInsertedRows == 0) {
                Log.v(LOG_TAG, "Error bulk insert");
            }
        }
    }

    private String normalizeValue(String value) {
        if (null != value) return value;
        return "";
    }

    private String normalizeValue(Float value) {
        if (null != value) return value.toString();
        return "";
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
