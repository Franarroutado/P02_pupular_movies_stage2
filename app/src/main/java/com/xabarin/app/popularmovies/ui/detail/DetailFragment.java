package com.xabarin.app.popularmovies.ui.detail;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.data.PopularMoviesContract;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int DETAIL_LOADER = 0;

    public static final String DETAIL_URI = "URI";

    // ===========================================================
    // Fields
    // ===========================================================
    private Uri mUri;

    @Bind(R.id.txtPlotSynopsis)     TextView mtxtViewPlotSynopsis;
    @Bind(R.id.txtReleaseDate)      TextView mtxtViewReleaseDate;
    @Bind(R.id.txtUserRating)       TextView mtxtViewUserRating;
    @Bind(R.id.appbar)              android.support.design.widget.AppBarLayout mappbar;
    @Bind(R.id.imgPoster)           com.xabarin.app.popularmovies.ui.SquareImageView mImgViewPoster;
    @Bind(R.id.collapsing_toolbar)  CollapsingToolbarLayout mcollapsingToolbar;

    @Bind(R.id.toolbar) Toolbar mToolBar;

    // ===========================================================
    // Constructors
    // ===========================================================

    public DetailFragment() {
        //setHasOptionsMenu(true);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (null != arguments) {
            mUri = arguments.getParcelable(DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_popular_movies_detail, container, false);
        ButterKnife.bind(this, rootView);

        FragmentSupportActionBar activity = (FragmentSupportActionBar)getActivity();
        activity.configureSupportActionBar(mToolBar);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     *  LoaderManager.LoaderCallbacks<Cursor>
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {

            return new CursorLoader(
                    getActivity(),
                    mUri,
                    PopularMoviesContract.MovieEntry.MOVIES_COLUMNS,
                    null, null, null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            mcollapsingToolbar.setTitle(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_TITLE));
            mtxtViewPlotSynopsis.setText(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_OVERVIEW));
            mtxtViewReleaseDate.setText(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_RELEASE_DATE));
            mtxtViewUserRating.setText(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_VOTE_AVERAGE));
            Picasso.with(getActivity().getApplicationContext())
                    .load(BASE_IMAGE_URL + data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_POSTER_URL))
                    .into(mImgViewPoster);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Nothing to implement
    }



    // ===========================================================
    // Methods
    // ===========================================================

    public static DetailFragment makePopularMoviesDetailFragment() {
        return new DetailFragment();
    }

    private int getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


    public interface FragmentSupportActionBar {

        void configureSupportActionBar(Toolbar toolbar);
    }
}
