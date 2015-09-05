package com.xabarin.app.popularmovies.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xabarin.app.popularmovies.R;
import com.xabarin.app.popularmovies.model.movies.Movie;

import java.util.ArrayList;

/**
 * Created by francisco on 9/08/15.
 */
public class ImageAdapter extends CursorAdapter {

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

    public ImageAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mContext = context;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_image_item_movies, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Log.v(LOG_TAG, BASE_IMAGE_URL + cursor.getString(2));
        Picasso.with(mContext)
                .load(BASE_IMAGE_URL + cursor.getString(2))
                .into((ImageView) view.findViewById(R.id.picture));
        ((TextView)view.findViewById(R.id.text)).setText(cursor.getString(1));
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
