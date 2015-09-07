package com.xabarin.app.popularmovies.ui.detail;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.xabarin.app.popularmovies.Constants;
import com.xabarin.app.popularmovies.data.PopularMoviesContract;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.FavMovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesDB;
import com.xabarin.app.popularmovies.model.MoviesAPI;
import com.xabarin.app.popularmovies.model.reviews.ReviewsCollection;
import com.xabarin.app.popularmovies.model.videos.VideosCollection;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by francisco on 5/09/15.
 */
public class DetailPresenter {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private final static String LOG_TAG = DetailPresenter.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private DetailView mView;

    // ===========================================================
    // Constructors
    // ===========================================================

    public DetailPresenter(DetailView view) {
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

    private void saveFavourite(ContentValues contentValues) {
        String idMovie = (String)contentValues.get(FavMovieEntry.COLUMN_ID);
        Uri uri = FavMovieEntry.buildMoviesUri(Long.parseLong(idMovie) );
        Log.v(LOG_TAG, "URI used to save fav movie " + uri.toString());
        mView.getViewActivity().getContentResolver().insert(FavMovieEntry.CONTENT_URI, contentValues);
    }

    /**
     * IF the favourite exists delete it, if it does not exist, store it in the database
     * @param contentValues
     * @return true if is saved, true if it is deleted.
     */
    public boolean toggleFavourite(ContentValues contentValues) {

        String idMovie = (String)contentValues.get(FavMovieEntry.COLUMN_ID);
        Boolean blnStatus = PopularMoviesDB.existFabMovie(mView.getViewActivity(), idMovie);

        if (blnStatus) { // if movie exists: delete it, return false;
            Log.v(LOG_TAG, "Delete FavMovie " + idMovie);
            deleteFavourite(contentValues);
            return false;

        } else { // if movie does not exists create it, return true;
            Log.v(LOG_TAG, "Fav movie add to the database.");
            saveFavourite(contentValues);
            return true;
        }
    }

    private void deleteFavourite(ContentValues contentValues) {
        String idMovie = (String)contentValues.get(PopularMoviesContract.FavMovieEntry.COLUMN_ID);
        Uri uri = FavMovieEntry.buildMoviesUri(Long.parseLong(idMovie));
        Log.v(LOG_TAG, "URI used to delete fav movie " + uri.toString());
        mView.getViewActivity().getContentResolver().delete(
                uri,
                PopularMoviesContract.FavMovieEntry.COLUMN_ID + "=?",
                new String[]{idMovie});
    }
    public Boolean isFavourite(String idMovie) {
        return PopularMoviesDB.existFabMovie(mView.getViewActivity(), idMovie);
    }

    public void requestVideos(String idMovie, String codeApi) {
        RestAdapter myRestAdapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
        MoviesAPI myMoviesAPI = myRestAdapter.create(MoviesAPI.class);
        myMoviesAPI.getVideos(idMovie, codeApi, new Callback<VideosCollection>() {
            @Override
            public void success(VideosCollection videosCollection, Response response) {
                Log.v(LOG_TAG, "Successfully fetching videos from the Internet. ");
                mView.onRequestVideosSuccess(videosCollection);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Error fetching videos from the Internet. " + error.toString());
            }
        });
    }

    public void requestReviews(String idMovie, String codeApi) {
        RestAdapter myRestAdapter = new RestAdapter.Builder().setEndpoint(Constants.BASE_URL).build();
        MoviesAPI myMoviesAPI = myRestAdapter.create(MoviesAPI.class);
        myMoviesAPI.getReviews(idMovie, codeApi, new Callback<ReviewsCollection>() {
            @Override
            public void success(ReviewsCollection reviewsCollection, Response response) {
                Log.v(LOG_TAG, "Successfully fetching reviews from the Internet. ");
                mView.onRequestReviewsSuccess(reviewsCollection);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Error fetching reviews from the Internet. " + error.toString());
            }
        });
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
