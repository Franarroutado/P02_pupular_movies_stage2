package com.xabarin.app.popularmovies.ui.detail;

import android.app.Activity;

import com.xabarin.app.popularmovies.model.reviews.ReviewsCollection;
import com.xabarin.app.popularmovies.model.videos.VideosCollection;

/**
 * Created by francisco on 5/09/15.
 */
public interface DetailView {

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

    Activity getViewActivity();

    void onRequestVideosSuccess(VideosCollection videosCollection);

    void onRequestReviewsSuccess(ReviewsCollection reviewsCollection);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
