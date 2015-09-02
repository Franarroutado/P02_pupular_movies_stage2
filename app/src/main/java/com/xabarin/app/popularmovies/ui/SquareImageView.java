package com.xabarin.app.popularmovies.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by francisco on 30/08/15.
 * http://www.rogcg.com/blog/2013/11/01/gridview-with-auto-resized-images-on-android
 */
public class SquareImageView extends ImageView {

    // Ordering code based on github contributor https://github.com/sockeqwe
    // ===========================================================
    // Final Fields
    // ===========================================================

    private static final Double ASPECT_RATIO_2_40 = 2.40;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public SquareImageView(Context context)
    {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getHeightByRatio(getMeasuredWidth(), ASPECT_RATIO_2_40));
    }

    // ===========================================================
    // Methods
    // ===========================================================



    /**
     * Calc the height based on the ratio
     * http://stackoverflow.com/questions/3461926/calculate-height-for-image-video-based-on-aspect-ratio-and-width
     */
    private int getHeightByRatio(int width, double ratio) {
        Double dblResult = width * ratio;

        return  dblResult.intValue();
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
