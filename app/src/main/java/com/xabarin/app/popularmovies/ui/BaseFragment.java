package com.xabarin.app.popularmovies.ui;

import android.app.Fragment;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by francisco on 8/08/15.
 */
public class BaseFragment extends Fragment {

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

    // ===========================================================
    // Methods
    // ===========================================================

    protected View onCreateViewWithButterKnife(
            @LayoutRes int layout,
            LayoutInflater inflater,
            ViewGroup container) {
        View myView = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, myView);

        return myView;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
