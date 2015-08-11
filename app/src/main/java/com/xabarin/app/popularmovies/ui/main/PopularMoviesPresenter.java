package com.xabarin.app.popularmovies.ui.main;

import android.util.Log;

import com.xabarin.app.popularmovies.model.movies.MoviesCollection;
import com.xabarin.app.popularmovies.model.MoviesAPI;
import com.xabarin.app.popularmovies.preferences.PopularMoviesPreferences;

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

        String strMoviesdbAPI = getMoviesdbAPI();

        //getMoviesByHighestRated
        //myMoviesAPI.getResults(sortBy, strMoviesdbAPI, new Callback<MoviesCollection>() {
        myMoviesAPI.getMoviesByHighestRated(new Callback<MoviesCollection>() {
            @Override
            public void success(MoviesCollection moviesCollection, Response response) {
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
