package com.xabarin.app.popularmovies.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.ui.BaseActivity;
import com.xabarin.app.popularmovies.ui.SettingsActivity;
import com.xabarin.app.popularmovies.ui.main.PopularMoviesActivity;

public class PopularMoviesDetailActivity extends BaseActivity {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String LOG_TAG = PopularMoviesActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

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
        if (null == savedInstanceState) {
            attachPopularMoviesFragment();
        }
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void attachPopularMoviesFragment() {
        super.attachFragment(PopularMoviesDetailFragment.makePopularMoviesDetailFragment(), LOG_TAG, false);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
