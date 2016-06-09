package com.example.jayden.mobileteamproject.BookShelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
    Bitmap bmp;

    public ImageGridAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.arr = list;
        //aq = new AQuery(context);
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
            // GridView 뷰를 구성할 ImageView 뷰의 비트맵을 정의합니다.
            // 그리고 그것의 크기를 402*503으로 맞춤
            // 크기를 줄이는 이유는 메모리 부족 문제를 막을 수 있기 때문입니다.

            //aq.id(imageView).image(arr.get(position).bookUrl);
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bitmap new_bmp = Bitmap.createScaledBitmap(bmp, 150, 200, true);

            //---------------------------------------------------------------
            // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
            // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.
            imageView = new ImageView(context);
            imageView.setPadding(0, 65, 0, 0);
            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(new_bmp);
            //---------------------------------------------------------------
            // 지금은 사용하지 않는 코드입니다.
            //imageView.setMaxWidth(320);
            //imageView.setMaxHeight(240);
            //imageView.setImageResource(imageIDs[position]);

            //---------------------------------------------------------------
            // 사진 항목들의 클릭을 처리하는 ImageClickListener 객체를 정의합니다.
            // 그리고 그것을 ImageView의 클릭 리스너로 설정합니다.

            ImageClickListener imageViewClickListener
                    = new ImageClickListener(context, arr.get(position));
            imageView.setOnClickListener(imageViewClickListener);
        }

        return imageView;
    }
}
