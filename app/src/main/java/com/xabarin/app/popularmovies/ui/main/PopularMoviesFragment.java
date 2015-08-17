package com.xabarin.app.popularmovies.ui.main;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.model.movies.Movie;
import com.xabarin.app.popularmovies.model.movies.MoviesCollection;
import com.xabarin.app.popularmovies.ui.BaseFragment;
import com.xabarin.app.popularmovies.ui.adapter.ImageAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends BaseFragment
        implements PopularMoviesView, LoaderManager.LoaderCallbacks<Cursor> {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private final static String LOG_TAG = PopularMoviesFragment.class.getSimpleName();

    private static final String SAVED_INSTANCE_KEY = "movies"; // To save the data when rotation the device

    private static final int POPULARMOVIES_LOADER = 0;


    // ===========================================================
    // Fields
    // ===========================================================

    private PopularMoviesPresenter mPresenter;

    private ImageAdapter mImageAdapter;
    /**
     * This value stores the current collection of movies from the savedInstanceState
     */
    private ArrayList<Movie> mCurrentMovies = new ArrayList<Movie>();

    @Bind(R.id.gridMovies)
    GridView mMoviesGrid;

    // ===========================================================
    // Constructors
    // ===========================================================

    public PopularMoviesFragment() {
        mPresenter = new PopularMoviesPresenter(this);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================


    @Override
    public void onStart() {
        super.onStart();
        mPresenter.requestMovies(getString(R.string.sortByPopularity_key));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateViewWithButterKnife(R.layout.fragment_popular_movies, inflater, container);

        ButterKnife.bind(this, view);

        mImageAdapter = new ImageAdapter(getActivity().getApplicationContext(), null, 0);

        mMoviesGrid.setAdapter(mImageAdapter);

//        if ( null != savedInstanceState && savedInstanceState.containsKey(SAVED_INSTANCE_KEY)) {
//            mCurrentMovies.clear();
//            mCurrentMovies = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_KEY);
//            buildView();
//        } else {
//            mPresenter.requestMovies(getString(R.string.sortByPopularity_key));
//        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(POPULARMOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_INSTANCE_KEY, mCurrentMovies);
        super.onSaveInstanceState(outState);
    }

    /** THE FOLLOWING ARE THE OVERRIDE METHODS FOR  PopularMoviesView Interface **/

    @Override
    public void onRequestMoviesSuccess(MoviesCollection moviesCollection) {
        mCurrentMovies.clear();
        mCurrentMovies.addAll(moviesCollection.getResults());
        //buildView();
    }

    @Override
    public Activity getViewActivity() {
        return this.getActivity();
    }

    /** THE FOLLOWING ARE THE OVERRIDE METHODS FOR  LoaderManager.LoaderCallbacks **/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri popularMovieUri =  MovieEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                popularMovieUri,
                MovieEntry.MOVIES_COLUMNS,
                null,null, MovieEntry.COLUMN_POPULARITY
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mImageAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mImageAdapter.swapCursor(null);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public static PopularMoviesFragment makePopularMoviesFragment() {
        return new PopularMoviesFragment();
    }

//    private void buildView() {
//        mImageAdapter.clear();
//        for (Movie movie : mCurrentMovies) {
//            Log.v(LOG_TAG, movie.getTitle());
//            mImageAdapter.add(movie);
//        }
//    }
//
//    @OnItemClick(R.id.gridMovies)
//    void onItemClick_event(int position) {
//        Movie movie = mImageAdapter.getItem(position);
//        Intent myIntent = new Intent(getActivity(),
//                PopularMoviesDetailActivity.class).putExtra(Constants.EXTRA_MOVIE_DETAIL_KEY, movie);
//        startActivity(myIntent);
//    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
