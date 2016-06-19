package com.example.jayden.mobileteamproject.BookShelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.example.jayden.mobileteamproject.R;

/**
 * Created by Administrator on 2016-05-23.
 */
public class BookShelfView extends GridView {

    private Bitmap background;

    public BookShelfView(Context context) {
        super(context);
        init();
    }

    public BookShelfView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookShelfView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x, y, width, height;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bookk_bookcase);
        //이미지 자르기
        width = getBackgroundWidth();
        height = getBackgroundHeight();
        background = Bitmap.createScaledBitmap(background,width,height,true);
    }

    private int getBackgroundHeight() {
        return getHeight() / 4;
    }

    private int getBackgroundWidth() {
        return getHeight();
    }

    //Set the background image of the ShelfView panel.
    protected void init() {
    }

    //Draw a background in the screen and create multiple panels using height & width.
    @Override
    protected void dispatchDraw(Canvas canvas) {

        int top = getChildCount() > 0 ? getChildAt(0).getTop() : 0;
        for (int y = top; y < getHeight(); y += getBackgroundHeight()) {
                canvas.drawBitmap(background, 0, y, null);
        }
        super.dispatchDraw(canvas);
    }
}
