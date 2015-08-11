package com.xabarin.app.popularmovies.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xabarin.app.popularmovies.Constants;
import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.model.movies.Movie;
import com.xabarin.app.popularmovies.ui.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesDetailFragment extends BaseFragment {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String LOG_TAG = PopularMoviesDetailFragment.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Movie mMovie;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PopularMoviesDetailFragment() {
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
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_popular_movies_detail, container, false);
        if (intent != null && intent.hasExtra(Constants.EXTRA_MOVIE_DETAIL_KEY)) {
            mMovie = intent.getParcelableExtra(Constants.EXTRA_MOVIE_DETAIL_KEY);
            ((TextView) rootView.findViewById(R.id.txtMovieTitle)).setText(mMovie.getTitle());
            ((TextView) rootView.findViewById(R.id.txtPlotSynopsis)).setText(mMovie.getOverview());
            ((TextView) rootView.findViewById(R.id.txtReleaseDate)).setText(mMovie.getRelease_date());
            ((TextView) rootView.findViewById(R.id.txtUserRating)).setText(mMovie.getVote_average().toString());
            Picasso.with(getActivity())
                    .load(getString(R.string.moviedb_image_url) + mMovie.getPoster_path())
                    .into((ImageView) rootView.findViewById(R.id.imgPoster));
        }
        return rootView;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public static PopularMoviesDetailFragment makePopularMoviesDetailFragment() {
        return new PopularMoviesDetailFragment();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
