package com.xabarin.app.popularmovies.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.model.SortBy;
import com.xabarin.app.popularmovies.preferences.GeneralPreferences;
import com.xabarin.app.popularmovies.preferences.GeneralPreferencesActivity;
import com.xabarin.app.popularmovies.ui.FragmentSupportActionBar;
import com.xabarin.app.popularmovies.ui.detail.DetailActivity;
import com.xabarin.app.popularmovies.ui.detail.DetailFragment;


public class PopularMoviesActivity extends AppCompatActivity
        implements FragmentSupportActionBar, PopularMoviesFragment.Callback {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String LOG_TAG = PopularMoviesActivity.class.getSimpleName();

    private static final String DETAILFRAGMENT_TAG = "DFTAG";


    // ===========================================================
    // Fields
    // ===========================================================

    private boolean mTwoPane;
    /**
     * Set the 3 sort options: by most popular, by highest-rated or favourite
     */
    private SortBy mSortOption;

    private GeneralPreferences mGeneralPreferences;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        mGeneralPreferences = new GeneralPreferences(getApplicationContext());
        mSortOption = mGeneralPreferences.getSortByEnumPreference();

        if (findViewById(R.id.fragment_movies_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_movies_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // GeneralPreferences preferences = new GeneralPreferences(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Call intent explicity
            startActivity(new Intent(this, GeneralPreferencesActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  PopularMoviesFragment.Callback implementation
    */

    @Override
    public void onItemSelected(Uri uri) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, uri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movies_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class).setData(uri);
            startActivity(intent);
        }

    }

    @Override
    public void configureSupportActionBar(Toolbar toolbar) {

        if (!mTwoPane) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
