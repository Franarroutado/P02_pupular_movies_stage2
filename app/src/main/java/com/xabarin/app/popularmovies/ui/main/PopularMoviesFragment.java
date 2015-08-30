package com.xabarin.app.popularmovies.ui.main;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.model.SortBy;
import com.xabarin.app.popularmovies.model.movies.Movie;
import com.xabarin.app.popularmovies.model.movies.MoviesCollection;
import com.xabarin.app.popularmovies.preferences.GeneralPreferences;
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

    private GeneralPreferences mPreferences;

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
        setHasOptionsMenu(true);
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
        //mPresenter.requestMovies(mPreferences.getSortByEnumPreference(), getPreferenceAPIKey());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.popular_movies_fragment_menu, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        SortBy sortByDefault = mPreferences.getSortByEnumPreference();
        SortBy sortBySelected = null;
        switch (item.getItemId()) {
            case R.id.action_menuSortHighestRanked :
                sortBySelected = SortBy.HIGHEST_RATED;
                Log.v(LOG_TAG, "OrderBy HighestRanked");
                break;
            case R.id.action_menuSortMostPopular :
                sortBySelected = SortBy.MOST_POPULAR;
                Log.v(LOG_TAG, "OrderBy MostPopular");
                break;
            case R.id.action_menuFavorite :
                sortBySelected = SortBy.FAVORITES;
                Log.v(LOG_TAG, "OrderBy Favorite");
                break;
        }

        // Only reload movies if the option selected is different of the current.
        if (null != sortBySelected && !sortBySelected.equals(sortByDefault)) {
            mPreferences.setSortByPreference(sortBySelected);
            onSortByChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPreferences = new GeneralPreferences(getActivity().getApplicationContext());

        View view = super.onCreateViewWithButterKnife(R.layout.fragment_popular_movies, inflater, container);
        ButterKnife.bind(this, view);

        mImageAdapter = new ImageAdapter(getActivity().getApplicationContext(), null, 0);
        mMoviesGrid.setAdapter(mImageAdapter);

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
                null, null, null);
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

    public void onSortByChanged() {

        String apiKey = getPreferenceAPIKey();
        SortBy sortBy = mPreferences.getSortByEnumPreference();

        mPresenter.requestMovies(sortBy, apiKey);
        getLoaderManager().restartLoader(POPULARMOVIES_LOADER, null, this);
    }

    public static PopularMoviesFragment makePopularMoviesFragment() {
        return new PopularMoviesFragment();
    }

    private String getPreferenceAPIKey() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getString(getString(R.string.pref_apikey_key),
                getString(R.string.pref_apikey_default));

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}