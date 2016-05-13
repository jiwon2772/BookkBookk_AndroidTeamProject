package com.example.jayden.mobileteamproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jayden on 2016-05-12.
 */
public class PostAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Activity m_activity;
    private ArrayList<Post> arr;

    public PostAdapter(Activity act, ArrayList<Post> arr_list) {
        this.m_activity = act;
        arr = arr_list;
        mInflater = (LayoutInflater) m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            int res = 0;
            res = R.layout.post_layout;
            convertView = mInflater.inflate(res, parent, false);
        }
        BookImage book = (BookImage)convertView.findViewById(R.id.bookimage);
        ImageView profile  = (ImageView)convertView.findViewById(R.id.profile);
        TextView user = (TextView)convertView.findViewById(R.id.user);
        TextView time = (TextView)convertView.findViewById(R.id.time);
        TextView text = (TextView)convertView.findViewById(R.id.text);
        //LinearLayout layout_view =  (LinearLayout)convertView.findViewById(R.id.view);

        book.setImageAddress(arr.get(position).bookUrl);// 책 이미지를 각 리스트뷰에 연결해줌
       // BookImage.ImageLoaderTask bookTesk = new BookImage.ImageLoaderTask(book,)
        //int resId=  m_activity.getResources().getIdentifier(arr.get(position).profileUrl, "drawable", m_activity.getPackageName());
        // setting Item adapter

        ImageLoaderTask task = new ImageLoaderTask(profile,arr.get(position).profileUrl);
        task.execute();
        user.setText(arr.get(position).userID);         // 유저아이디를 각 리스트뷰에 연결해줌
        time.setText(arr.get(position).time);
        text.setText(arr.get(position).text);
//        layout_view.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });
        return convertView;
    }
    public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {

        /** The target image view to load an image */
        private ImageView imageView;

        /** The address where an image is stored. */
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
            this.imageView.setImageBitmap(bitmap);
        }
    }
}
