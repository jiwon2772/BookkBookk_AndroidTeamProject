package com.example.jayden.mobileteamproject.Request;

/**
 * Created by Jayden on 2016-06-22.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jayden.mobileteamproject.Friend.Friend;
import com.example.jayden.mobileteamproject.Main.USERINFO;
import com.example.jayden.mobileteamproject.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AcceptAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity m_activity;
    private ArrayList<Friend> arr;

    public AcceptAdapter(Activity act, ArrayList<Friend> arr_list) {
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

    public void updateProfile(ImageView img, Bitmap bitmap) {
        img.setImageBitmap(bitmap);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            int res = 0;
            res = R.layout.finish_list;
            convertView = mInflater.inflate(res, parent, false);
        }

        ImageView pro_img = (ImageView) convertView.findViewById(R.id.ivImg);
        TextView pro_nick = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView date = (TextView)convertView.findViewById(R.id.tvDescription);

        ImageLoaderTask task = new ImageLoaderTask(pro_img,arr.get(position).prof_url);
        task.execute();

        pro_nick.setText("" + arr.get(position).nick);
        date.setText("" + arr.get(position).date);

        Button btnRequest = (Button) convertView.findViewById(R.id.checkKAKAO);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //상대 카카오톡 아이디 확인
            }
        });

        return convertView;
    }

    public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView profileView;
        private String profileAddress;

        public ImageLoaderTask(ImageView imgView, String imgAddress) {
            this.profileView = imgView;
            this.profileAddress = imgAddress;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                InputStream is = new java.net.URL(this.profileAddress).openStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("ImageLoaderTask", "Cannot load image from " + this.profileAddress);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            updateProfile(profileView, bitmap);
        }
    }

}
