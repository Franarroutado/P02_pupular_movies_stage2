package com.xabarin.app.popularmovies.ui.main;

import android.content.ContentValues;
import android.util.Log;

import com.xabarin.app.popularmovies.Constants;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesDB;
import com.xabarin.app.popularmovies.model.MoviesAPI;
import com.xabarin.app.popularmovies.model.SortBy;
import com.xabarin.app.popularmovies.model.movies.Movie;
import com.xabarin.app.popularmovies.model.movies.MoviesCollection;
import com.xabarin.app.popularmovies.preferences.GeneralPreferences;

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

    private static final String LOG_TAG = PopularMoviesPresenter.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private PopularMoviesView mView;

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


    public void requestMovies(SortBy sortBy, String codeApi) {

        RestAdapter myRestAdapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
        MoviesAPI myMoviesAPI = myRestAdapter.create(MoviesAPI.class);
        String sortByOption  = GeneralPreferences.mapSortByToString(sortBy);

        myMoviesAPI.getMovies(sortByOption, codeApi, new Callback<MoviesCollection>() {
            @Override
            public void success(MoviesCollection moviesCollection, Response response) {
                Log.v(LOG_TAG, "Successfully fetching movies from the Internet. ");
                insertPopularMovies(moviesCollection);
                mView.onRequestMoviesSuccess(moviesCollection);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Error fetching movies from the Internet. " + error.toString());
            }
        });
    }

    private void insertPopularMovies(MoviesCollection moviesCollection) {

        // Anytime a collection of movies are populated into the database it is neccesary to clear DB
        PopularMoviesDB.deleteAllMovies(mView.getViewActivity());

        // Insert the new movies data into the database
        Vector<ContentValues> cVector = new Vector<ContentValues>(moviesCollection.getResults().size());

        int numInsertedRows = 0;

        for (Movie movie : moviesCollection.getResults())
        {
            ContentValues popularMoviesValues = new ContentValues();
            popularMoviesValues.put(MovieEntry.COLUMN_ID, movie.getId());
            popularMoviesValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, normalizeValue(movie.getVote_average()));
            popularMoviesValues.put(MovieEntry.COLUMN_TITLE, normalizeValue(movie.getTitle()));
            popularMoviesValues.put(MovieEntry.COLUMN_POSTER_URL, normalizeValue(movie.getPoster_path()));
            popularMoviesValues.put(MovieEntry.COLUMN_OVERVIEW, normalizeValue(movie.getOverview()));
            popularMoviesValues.put(MovieEntry.COLUMN_RELEASE_DATE, normalizeValue(movie.getRelease_date()));

            cVector.add(popularMoviesValues);
        }

        // add to database
        if ( cVector.size() > 0) {
            ContentValues [] cvArray = new ContentValues[cVector.size()];
            cVector.toArray(cvArray);

            PopularMoviesDB.addMovieCollection(mView.getViewActivity(), cvArray);

            numInsertedRows = cvArray.length;

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
