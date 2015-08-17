package com.xabarin.app.popularmovies.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.xabarin.app.popularmovies.R;

/**
 * Created by francisco on 8/08/15.
 */
public class PopularMoviesPreferences {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String PREFERENCES_FILES = "user_preferences";
    private final SharedPreferences mPreferences;
    private final Context mContext;

    // ===========================================================
    // Fields
    // ===========================================================


    // ===========================================================
    // Constructors
    // ===========================================================

    public PopularMoviesPreferences(Context myContext) {
        mContext = myContext;
        mPreferences = myContext.getSharedPreferences(PREFERENCES_FILES, Context.MODE_APPEND);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public String getMovieDBAPIKey() {
        return mPreferences.getString(
                mContext.getString(R.string.pref_apikey_key),
                mContext.getString(R.string.pref_apikey_default));
    }

    public String getSortByPopularity() {
        return mPreferences.getString(mContext.getString(R.string.sortByPopularity_key), null);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
