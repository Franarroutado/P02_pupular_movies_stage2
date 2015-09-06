package com.xabarin.app.popularmovies.ui.detail;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.xabarin.app.popularmovies.data.PopularMoviesContract;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.FavMovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesDB;

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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
