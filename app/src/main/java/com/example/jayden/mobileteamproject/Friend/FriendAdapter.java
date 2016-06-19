package com.example.jayden.mobileteamproject.Friend;

/**
 * Created by Jayden on 2016-06-12.
 */

import android.widget.BaseAdapter;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.support.v4.app.FragmentActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.example.jayden.mobileteamproject.R;

import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;

/**
 * Created by SAMSUNG on 2016-06-11.
 */
public class FriendAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity m_activity;
    private ArrayList<Friend> arr;
    private int friend;
    public FriendAdapter(Activity act, ArrayList<Friend> arr_list) {
        this.m_activity = act;
        arr = arr_list;
        mInflater = (LayoutInflater) m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FriendAdapter(FragmentActivity activity, int friend, ArrayList<Friend> arr_list) {
        this.m_activity = activity;
        friend= friend;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            int res = 0;
            res = R.layout.friend;
            convertView = mInflater.inflate(res, parent, false);
        }

        ImageView pro_img = (ImageView) convertView.findViewById(R.id.kt_user_profile);
        TextView pro_id = (TextView) convertView.findViewById(R.id.kt_user_id);

        ImageLoaderTask task = new ImageLoaderTask(pro_img,arr.get(position).prof_url);
        task.execute();
        pro_id.setText("" + arr.get(position).userId);

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
            this.profileView.setImageBitmap(bitmap);
        }

    }
}

