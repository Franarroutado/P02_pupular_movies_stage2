package com.xabarin.app.popularmovies.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.xabarin.app.popularmovies.R;

/**
 * Created by francisco on 8/08/15.
 */
public class BaseActivity extends Activity {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

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
        setContentView(R.layout.activity_fragment_container);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * @param f              - fragment
     * @param tag            - tag to find fragment by
     * @param addToBackStack - true if adding fragment should be in backstack, false if not
     */
    public void attachFragment(Fragment f, String tag, boolean addToBackStack) {
        FragmentManager myFragmentManager = getFragmentManager();
        FragmentTransaction myFragmentTransaction = myFragmentManager.beginTransaction();
        myFragmentTransaction.replace(R.id.activity_fragment_container, f, tag);
        if (addToBackStack) {
            myFragmentTransaction.addToBackStack(null);
        }
        myFragmentTransaction.commit();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
