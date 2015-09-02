package com.xabarin.app.popularmovies.ui.detail;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.data.PopularMoviesContract;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.model.movies.Movie;

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

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int DETAIL_LOADER = 0;

    public static final String DETAIL_URI = "URI";

    // ===========================================================
    // Fields
    // ===========================================================

    private Movie mMovie;
    private Uri mUri;

    @Bind(R.id.txtMovieTitle)   TextView mtxtViewTitle;
    @Bind(R.id.txtPlotSynopsis) TextView mtxtViewPlotSynopsis;
    @Bind(R.id.txtReleaseDate)  TextView mtxtViewReleaseDate;
    @Bind(R.id.txtUserRating)   TextView mtxtViewUserRating;


    // ===========================================================
    // Constructors
    // ===========================================================

    public DetailFragment() {
        setHasOptionsMenu(true);
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
//        Intent intent = getActivity().getIntent();
//        if (intent != null && intent.hasExtra(Constants.EXTRA_MOVIE_DETAIL_KEY)) {
//            mMovie = intent.getParcelableExtra(Constants.EXTRA_MOVIE_DETAIL_KEY);
//            ((TextView) rootView.findViewById(R.id.txtMovieTitle)).setText(mMovie.getTitle());
//            ((TextView) rootView.findViewById(R.id.txtPlotSynopsis)).setText(mMovie.getOverview());
//            ((TextView) rootView.findViewById(R.id.txtReleaseDate)).setText(mMovie.getRelease_date());
//            ((TextView) rootView.findViewById(R.id.txtUserRating)).setText(mMovie.getVote_average().toString());
//            Picasso.with(getActivity())
//                    .load(getString(R.string.moviedb_image_url) + mMovie.getPoster_path())
//                    .into((ImageView) rootView.findViewById(R.id.imgPoster));
//        }
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
            mtxtViewTitle.setText(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_TITLE));
            mtxtViewPlotSynopsis.setText(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_OVERVIEW));
            mtxtViewReleaseDate.setText(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_RELEASE_DATE));
            mtxtViewUserRating.setText(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_VOTE_AVERAGE));
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
