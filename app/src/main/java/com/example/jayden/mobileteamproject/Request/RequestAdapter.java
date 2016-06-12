package com.example.jayden.mobileteamproject.Request;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.example.jayden.mobileteamproject.R;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RequestAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity m_activity;
    private ArrayList<Request> arr;

    public RequestAdapter(Activity act, ArrayList<Request> arr_list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int res = 0;
            res = R.layout.item;
            convertView = mInflater.inflate(res, parent, false);
        }

       ImageView pro_img = (ImageView) convertView.findViewById(R.id.kt_user_profile);
       TextView pro_id = (TextView) convertView.findViewById(R.id.kt_user_id);
        TextView timer = (TextView) convertView.findViewById(R.id.timer);
        Button re_btn = (Button) convertView.findViewById(R.id.request);

        pro_id.setText(arr.get(position).pro_id);
        pro_img.setImageBitmap(arr.get(position).bitmap);
        timer.setText(arr.get(position).timer);

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
            updateProfile(profileView,bitmap);
        }

    }
}
