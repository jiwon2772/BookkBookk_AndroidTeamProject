package com.example.jayden.mobileteamproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jayden on 2016-05-12.
 */
public class BookImage extends ImageView {

    private Bitmap itsBitmap = null;
    private int itsWidth;
    private int itsHeight;

    public BookImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BookImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookImage(Context context) {
        super(context);
    }

    public void setImageAddress(String add) {
        new ImageLoaderTask(this, add).execute();
    }

    public void setItsBitmap(Bitmap bitmap) {
        itsBitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (itsBitmap == null) {
        } else {
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(itsBitmap,new Rect(0,0,itsBitmap.getWidth(),itsBitmap.getHeight()), new Rect(20,20,itsWidth-20,itsHeight-20), null);
            itsBitmap = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = (int) getDip(10);
        int height = (int) getDip(10);

        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED: // unspecified
                width = widthMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:  // wrap_content
                break;
            case MeasureSpec.EXACTLY:  // match_parent
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED: // unspecified
                height = heightMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:  // wrap_content
                break;
            case MeasureSpec.EXACTLY:  // match_parent
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }

        setMeasuredDimension(width, height);
        itsWidth = width;
        itsHeight = height;

    }

    public float getDip(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {

        /**
         * The target image view to load an image
         */
        private ImageView imageView;
        /**
         * The address where an image is stored.
         */
        private String imageAddress;

        public ImageLoaderTask(ImageView imageView, String imageAddress) {
            this.imageView = imageView;
            this.imageAddress = imageAddress;
        }


        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                InputStream is = new java.net.URL(this.imageAddress).openStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("ImageLoaderTask", "Cannot load image from " + this.imageAddress);
            }
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            itsBitmap = bitmap;

        }

    }
}
