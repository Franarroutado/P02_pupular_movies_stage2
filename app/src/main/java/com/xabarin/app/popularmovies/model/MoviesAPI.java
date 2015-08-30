package com.xabarin.app.popularmovies.model;

import com.xabarin.app.popularmovies.model.movies.MoviesCollection;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by francisco on 8/08/15.
 */
public interface MoviesAPI {

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

//        @GET("/3/discover/movie?sort_by=popularity.desc&api_key=fb26558156aac88aaa33978aab72662f")
//        public void getMoviesByPopularity(Callback<MoviesCollection> response);
//
//        @GET("/3/discover/movie?sort_by=vote_average.desc&api_key=fb26558156aac88aaa33978aab72662f")
//        public void getMoviesByHighestRated(Callback<MoviesCollection> response);

        @GET("/3/discover/movie")
        public void getMovies(@Query("sort_by") String sortBy, @Query("api_key") String ApiKey, Callback<MoviesCollection> response);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
