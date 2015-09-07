package com.xabarin.app.popularmovies.ui.detail;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.FavMovieEntry;
import com.xabarin.app.popularmovies.data.PopularMoviesContract.MovieEntry;
import com.xabarin.app.popularmovies.model.reviews.Review;
import com.xabarin.app.popularmovies.model.reviews.ReviewsCollection;
import com.xabarin.app.popularmovies.model.videos.Video;
import com.xabarin.app.popularmovies.model.videos.VideosCollection;
import com.xabarin.app.popularmovies.ui.FragmentSupportActionBar;
import com.xabarin.app.popularmovies.ui.Toaster;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment
        implements DetailView, LoaderManager.LoaderCallbacks<Cursor> {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================
    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private final static String BASE_YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private final static String INTENT_YOUTUBE = "vnd.youtube:";

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final int DETAIL_LOADER = 0;

    public static final String DETAIL_URI = "URI";

    // ===========================================================
    // Fields
    // ===========================================================
    private Uri mUri;

    private ContentValues mContentValues;

    private DetailPresenter mPresenter;

    @Bind(R.id.txtPlotSynopsis)     TextView mtxtViewPlotSynopsis;
    @Bind(R.id.txtReleaseDate)      TextView mtxtViewReleaseDate;
    @Bind(R.id.txtUserRating)       TextView mtxtViewUserRating;

    @Bind(R.id.imgPoster)           com.xabarin.app.popularmovies.ui.SquareImageView mImgViewPoster;
    @Bind(R.id.collapsing_toolbar)  CollapsingToolbarLayout mcollapsingToolbar;
    @Bind(R.id.toolbar)             Toolbar mToolBar;
    @Bind(R.id.fabFavorite)         android.support.design.widget.FloatingActionButton mFabFavorite;

    @Bind(R.id.container_videos)    LinearLayout mContainerVideos;
    @Bind(R.id.container_reviews)    LinearLayout mContainerReviews;

    // ===========================================================
    // Constructors
    // ===========================================================

    public DetailFragment() {
        mPresenter =  new DetailPresenter(this);
        //setHasOptionsMenu(true);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (null != arguments) {
            mUri = arguments.getParcelable(DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_popular_movies_detail, container, false);
        ButterKnife.bind(this, rootView);

        mFabFavorite.setOnClickListener(fabClickListener);

        FragmentSupportActionBar activity = (FragmentSupportActionBar)getActivity();
        activity.configureSupportActionBar(mToolBar);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        if (mUri != null) { // this controls when the detail is FIRST TIME created using two pane configuration.
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);

            String idMovie = mUri.getLastPathSegment();
            String apiKey = getPreferenceAPIKey();
            mPresenter.requestReviews(idMovie, apiKey);
            mPresenter.requestVideos(idMovie, apiKey);
        }

        super.onActivityCreated(savedInstanceState);
    }

    /**
     *  LoaderManager.LoaderCallbacks<Cursor>
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MovieEntry.MOVIES_COLUMNS,
                    null, null, null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            mContentValues = new ContentValues();
            mContentValues.put(FavMovieEntry.COLUMN_ID,             data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_ID));
            mContentValues.put(FavMovieEntry.COLUMN_TITLE,          data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_TITLE));
            mContentValues.put(FavMovieEntry.COLUMN_OVERVIEW,       data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_OVERVIEW));
            mContentValues.put(FavMovieEntry.COLUMN_RELEASE_DATE,   data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_RELEASE_DATE));
            mContentValues.put(FavMovieEntry.COLUMN_VOTE_AVERAGE,   data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_VOTE_AVERAGE));
            mContentValues.put(FavMovieEntry.COLUMN_POSTER_URL,     data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_POSTER_URL));

            mcollapsingToolbar.setTitle(    data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_TITLE));
            mtxtViewPlotSynopsis.setText(   data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_OVERVIEW));
            mtxtViewReleaseDate.setText(    data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_RELEASE_DATE));
            mtxtViewUserRating.setText(     data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_VOTE_AVERAGE));
            Picasso.with(getActivity().getApplicationContext())
                    .load(BASE_IMAGE_URL + data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_POSTER_URL))
                    .into(mImgViewPoster);

            setFabImage(mPresenter.isFavourite(data.getString(MovieEntry.CURSOR_COLUMN_INDEX_FOR_ID)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Nothing to implement
    }

    /** THE FOLLOWING ARE THE OVERRIDE METHODS FOR  DetailView Interface **/

    @Override
    public Activity getViewActivity() {
        return this.getActivity();
    }

    @Override
    public void onRequestVideosSuccess(VideosCollection videosCollection) {

        if(videosCollection != null && videosCollection.getResults().size() > 0) {
            ImageButton imgButton;
            for (Video video : videosCollection.getResults()) {
                imgButton = new ImageButton(getActivity());
                imgButton.setTag(video);
                imgButton.setImageResource(R.drawable.trailer);
                imgButton.setOnClickListener(videoButtonClickListener);
                mContainerVideos.addView(imgButton);
            }
        } else {
            TextView txtView = new TextView(getActivity());
            txtView.setText("No movie trailers available, try later!");
            mContainerVideos.addView(txtView);
        }

    }

    @Override
    public void onRequestReviewsSuccess(ReviewsCollection reviewsCollection) {

        if(reviewsCollection != null && reviewsCollection.getResults().size() > 0) {

            TextView txtReview;
            for (Review review : reviewsCollection.getResults()) {
                txtReview = new TextView(getActivity());
                txtReview.setText(review.getAuthor() + ": " + review.getContent());
                mContainerReviews.addView(txtReview);
            }
        } else {
            TextView txtView = new TextView(getActivity());
            txtView.setText("No movie trailers available, try later!");
            mContainerReviews.addView(txtView);
        }
    }


    // ===========================================================
    // Methods
    // ===========================================================

    private ImageButton.OnClickListener videoButtonClickListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Video video = (Video)v.getTag();
            watchYoutubeVideo(video.getKey());
        }
    };

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Boolean blnResult = false;
            if (null != mContentValues) {
                blnResult = mPresenter.toggleFavourite(mContentValues);
            }

            setFabImage(blnResult);
            if (blnResult) {
                Toaster.makeAtoast(getActivity().getApplicationContext(), "Favourite added to your list!").show();
            } else {
                Toaster.makeAtoast(getActivity().getApplicationContext(), "Favourite deleted from your list!").show();
            }
        }
    };


    private void setFabImage(Boolean status) {
        if (status) {
            mFabFavorite.setImageResource(R.drawable.ic_star_black);
        } else {
            mFabFavorite.setImageResource(R.drawable.ic_star_border_white);
        }
    }

    private String getPreferenceAPIKey() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getString(getString(R.string.pref_apikey_key),
                getString(R.string.pref_apikey_default));

    }

    /**
     * http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
     * @param id
     */
    private void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(INTENT_YOUTUBE + id));
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse(BASE_YOUTUBE_URL+id));
            startActivity(intent);
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
