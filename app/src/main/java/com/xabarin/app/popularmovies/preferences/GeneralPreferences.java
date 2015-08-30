package com.xabarin.app.popularmovies.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.xabarin.app.popularmovies.Constants;
import com.xabarin.app.popularmovies.model.SortBy;

/**
 * Created by francisco on 26/08/15.
 */
public class GeneralPreferences {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final String PREFERENCES_FILE = "user_preferences";

    private static final String SORT_BY_KEY = Constants.BUNDLE_KEY_ROOT + "sortBy";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_RANK = "vote_average.desc";
    private static final String SORT_BY_FAVORITE = "fav";

    private final SharedPreferences mPreferences;
    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public GeneralPreferences(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_APPEND);
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

    private String getSortByPreference() {
        return mPreferences.getString(SORT_BY_KEY, SORT_BY_POPULARITY);
    }

    public SortBy getSortByEnumPreference() {
        return mapStringToSortBy(getSortByPreference());
    }


    public void setSortByPreference(SortBy sortBy) {
        mPreferences.edit().putString(SORT_BY_KEY, mapSortByToString(sortBy)).apply();
    }

    public void clearUserPreferences() {
        mPreferences.edit().clear().apply();
    }

    public static String mapSortByToString(SortBy sortBy) {

        String sortText = "";

        switch (sortBy) {
            case MOST_POPULAR:
                sortText = SORT_BY_POPULARITY;
                break;
            case HIGHEST_RATED:
                sortText = SORT_BY_RANK;
                break;
            case FAVORITES:
                sortText = SORT_BY_FAVORITE;
                break;
        }
        return sortText;
    }

    public static SortBy mapStringToSortBy(String option) {

        switch (option) {
            case SORT_BY_POPULARITY :
                return SortBy.MOST_POPULAR;
            case SORT_BY_RANK :
                return SortBy.HIGHEST_RATED;
            case SORT_BY_FAVORITE :
                return SortBy.FAVORITES;
            default :
                return null;
        }

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
