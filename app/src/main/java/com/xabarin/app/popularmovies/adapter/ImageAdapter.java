package com.xabarin.app.popularmovies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.model.movies.Movie;

import java.util.ArrayList;

/**
 * Created by francisco on 9/08/15.
 */
public class ImageAdapter extends BaseAdapter {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private final static String LOG_TAG = ImageAdapter.class.getSimpleName();

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    // ===========================================================
    // Fields
    // ===========================================================

    private ArrayList<Movie> mArrLstMovies = new ArrayList<Movie>();
    private Context mContext;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ImageAdapter(Context context, ArrayList<Movie> myMovies) {
        mContext = context;
        mArrLstMovies = myMovies;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public int getCount() {
        return mArrLstMovies.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public Movie getItem(int position) {
        return mArrLstMovies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if ( null == convertView ) {
            Log.v(LOG_TAG, "-------- Create a new View --------");
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_image_item_movies, parent, false);
        }

        Picasso.with(mContext)
                .load(BASE_IMAGE_URL + getItem(position).getPoster_path())
                .into((ImageView) convertView);

        return convertView;
    }

    public void clear() {
        if (mArrLstMovies != null) {
            mArrLstMovies.clear();
            notifyDataSetChanged();
        }
    }

    public void add(Movie movie) {
        if (mArrLstMovies != null) {
            mArrLstMovies.add(movie);
            notifyDataSetChanged();
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
