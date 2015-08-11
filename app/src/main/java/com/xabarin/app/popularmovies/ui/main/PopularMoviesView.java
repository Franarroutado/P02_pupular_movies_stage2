package com.xabarin.app.popularmovies.ui.main;

import android.app.Activity;

import com.xabarin.app.popularmovies.model.movies.MoviesCollection;

/**
 * Created by francisco on 8/08/15.
 */
public interface PopularMoviesView {

    void onRequestMoviesSuccess(MoviesCollection moviesCollection);

    Activity getViewActivity();

}
