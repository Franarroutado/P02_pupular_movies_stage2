package com.xabarin.app.popularmovies.ui.main;

import android.app.Activity;
import android.app.Fragment;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.model.SortBy;
import com.xabarin.app.popularmovies.model.movies.Movie;
import com.xabarin.app.popularmovies.model.movies.MoviesCollection;
import com.xabarin.app.popularmovies.preferences.GeneralPreferences;
import com.xabarin.app.popularmovies.ui.adapter.ImageAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment
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

    @Bind(R.id.gridMovies) GridView mMoviesGrid;
    @Bind(R.id.fabMenu) com.github.clans.fab.FloatingActionMenu mFabMenu;
    @Bind(R.id.fabFavorite) com.github.clans.fab.FloatingActionButton mFabFavorite;
    @Bind(R.id.fabMostPopular) com.github.clans.fab.FloatingActionButton mFabMostPopular;
    @Bind(R.id.fabByRank) com.github.clans.fab.FloatingActionButton mFabHighestRated;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPreferences = new GeneralPreferences(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, rootView);

        mImageAdapter = new ImageAdapter(getActivity().getApplicationContext(), null, 0);
        mMoviesGrid.setAdapter(mImageAdapter);

        mMoviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Long lngIdMovie = cursor.getLong(MovieEntry.CURSOR_COLUMN_INDEX_FOR_ID);
                    ((Callback) getActivity()).onItemSelected(MovieEntry.buildMoviesUri(lngIdMovie));
                }
            }
        });

        mFabFavorite.setOnClickListener(fabClickListener);
        mFabHighestRated.setOnClickListener(fabClickListener);
        mFabMostPopular.setOnClickListener(fabClickListener);

        mFabFavorite.setOnClickListener(fabClickListener);

        return rootView;
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

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";

            SortBy sortByDefault = mPreferences.getSortByEnumPreference();
            SortBy sortBySelected = null;

            switch (v.getId()) {
                case R.id.fabByRank :
                    text = ((FloatingActionButton) v).getLabelText();
                    sortBySelected = SortBy.HIGHEST_RATED;
                    Log.v(LOG_TAG, "OrderBy HighestRanked");
                    break;
                case R.id.fabMostPopular :
                    text = ((FloatingActionButton) v).getLabelText();
                    sortBySelected = SortBy.MOST_POPULAR;
                    Log.v(LOG_TAG, "OrderBy MostPopular");
                    break;
                case R.id.fabFavorite :
                    text = ((FloatingActionButton) v).getLabelText();
                    sortBySelected = SortBy.FAVORITES;
                    Log.v(LOG_TAG, "OrderBy Favorite");
                    break;
            }
            mFabMenu.toggle(true);
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

            // Only reload movies if the option selected is different of the current.
            if (null != sortBySelected && !sortBySelected.equals(sortByDefault)) {
                mPreferences.setSortByPreference(sortBySelected);
                onSortByChanged();
            }
        }
    };

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        void onItemSelected(Uri uri);
    }

}