package com.example.jayden.mobileteamproject.BookShelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.Posting.Post;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-05-13.
 */
public class ImageGridAdapter extends BaseAdapter {
    Context context = null;
    //-----------------------------------------------------------
    // imageIDs는 이미지 파일들의 리소스 ID들을 담는 배열입니다.
    // 이 배열의 원소들은 자식 뷰들인 ImageView 뷰들이 무엇을 보여주는지를
    // 결정하는데 활용될 것입니다.

    private ArrayList<Post> arr;
    private AQuery aq;
    Bitmap bmp = null;
    private int itsHeight;
    private int itsWidth;

    public ImageGridAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.arr = list;
    }
    public ImageGridAdapter(Context context, ArrayList<Post> list, int height, int width) {
        this.context = context;
        this.arr = list;
        itsHeight = height;
        itsWidth = width;
    }

    public int getCount() {
        return (null != arr) ? arr.size() : 0;
    }

    public Object getItem(int position) {
        return (null != arr) ? arr.get(position) : 0;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (null != convertView)
            imageView = (ImageView) convertView;
        else {
            //---------------------------------------------------------------
            //가져온 url을 통해 이미지를 모두 저장
            class ImageDown extends Thread {
                String bookUrl;

                ImageDown(String book) {
                    bookUrl = book;
                }

                @Override
                public void run() {
                    try {
                        InputStream is = new java.net.URL(bookUrl).openStream();
                        bmp = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        Log.e("ImageLoaderTask", "Cannot load image from ");
                    }
                }
            }
            ImageDown task_image = new ImageDown(arr.get(position).bookUrl);
            task_image.start();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(bmp == null) {
                Toast.makeText(context,"네트워크 연결이 약합니다. 다소 시간이 걸릴 수 있습니다.",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(bmp == null) {
                Toast.makeText(context, "네트워크 상황이 매우 안좋습니다. 나중에 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                return imageView;
            }
            //인터넷에서 bitmap으로 이미지를 가져온 후의 작업입니다.
            Log.d("size check","height : " + itsHeight + "width : " + itsWidth);
            int bookHeight = (int)((itsHeight / 4) * 0.75);
            int bookWidth = (int)(bookHeight * 0.75);
            Bitmap new_bmp = Bitmap.createScaledBitmap(bmp, bookWidth, bookHeight, true);


            //---------------------------------------------------------------
            // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
            // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.
            imageView = new ImageView(context);
            int topPadding = (int)(itsHeight / 4 * 0.15);
            imageView.setPadding(0, topPadding, 0, 0);
            imageView.setImageBitmap(new_bmp);
            imageView.setPadding(0, topPadding, 0, 0);
            //-------------------------------------------------------------

            ImageClickListener imageViewClickListener
                    = new ImageClickListener(context, arr.get(position));
            imageView.setOnClickListener(imageViewClickListener);
        }

        bmp = null;
        return imageView;
    }
}
