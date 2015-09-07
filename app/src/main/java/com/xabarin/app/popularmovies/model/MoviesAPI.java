package com.xabarin.app.popularmovies.model;

import com.xabarin.app.popularmovies.model.movies.MoviesCollection;
import com.xabarin.app.popularmovies.model.reviews.ReviewsCollection;
import com.xabarin.app.popularmovies.model.videos.VideosCollection;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
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

        @GET("/3/discover/movie")
        void getMovies(@Query("sort_by") String sortBy, @Query("api_key") String ApiKey, Callback<MoviesCollection> response);

        @GET("/3/movie/{id}/videos")
        void getVideos(@Path("id") String movieId, @Query("api_key") String ApiKey, Callback<VideosCollection> response);

        @GET("/3/movie/{id}/reviews")
        void getReviews(@Path("id") String movieId, @Query("api_key") String ApiKey, Callback<ReviewsCollection> response);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
