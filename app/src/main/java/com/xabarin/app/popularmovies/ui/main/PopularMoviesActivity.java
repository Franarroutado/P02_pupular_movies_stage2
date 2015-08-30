package com.xabarin.app.popularmovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.preferences.GeneralPreferences;
import com.xabarin.app.popularmovies.preferences.GeneralPreferencesActivity;
import com.xabarin.app.popularmovies.ui.BaseActivity;


public class PopularMoviesActivity extends BaseActivity {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String LOG_TAG = PopularMoviesActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    /**
     * Set the 3 sort options: by most popular, by highest-rated or favourite
     */
    private String mSortOption;

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
//        if (null == savedInstanceState) {
//            attachPopularMoviesFragment();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GeneralPreferences preferences = new GeneralPreferences(getApplicationContext());
        //String sortOption = GeneralPreferences.mapSortByToString(preferences.getSortByEnumPreference());

        // update the location in our second pane using the fragment manager
//        if (sortOption != null && !sortOption.equals(mSortOption)) {
//            PopularMoviesFragment pmf = (PopularMoviesFragment) getFragmentManager().findFragmentById(R.id.activity_fragment_container);
//            if (null != pmf) {
//                pmf.onSortByChanged();
//            }
//        }

        mSortOption = GeneralPreferences.mapSortByToString(preferences.getSortByEnumPreference());
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

    // ===========================================================
    // Methods
    // ===========================================================

    private void attachPopularMoviesFragment() {
        super.attachFragment(PopularMoviesFragment.makePopularMoviesFragment(), LOG_TAG, false);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
