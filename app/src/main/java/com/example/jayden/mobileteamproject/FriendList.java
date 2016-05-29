package com.example.jayden.mobileteamproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FriendList extends AppCompatActivity {
    private ListView F_ListView = null;
    private ListViewAdapter F_ListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        F_ListView = (ListView) findViewById(R.id.F_ListView);
        F_ListAdapter = new ListViewAdapter(this);
        F_ListView.setAdapter(F_ListAdapter);

        Intent a = getIntent();
        String profile = a.getStringExtra("profileImage");
        String nick = a.getStringExtra("nick");
        F_ListAdapter.addItem(profile, nick);
        F_ListAdapter.addItem(profile, nick);
        F_ListAdapter.addItem(profile, nick);
        F_ListAdapter.addItem(profile, nick);
        F_ListAdapter.addItem(profile, nick);

    }

    private class ViewHolder {//to manage the view (image and text view) like putting in the holder
        public ImageView pro_img;
        public TextView pro_id;

    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();//to arraylist for list data

        public ListViewAdapter(Context mContext) { //listview adapter
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() { //to count the list data
            return mListData.size();
        }

        @Override
        public Object getItem(int position) { // return to get each of the list item
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) { //return to get each of the list item position
            return position;
        }

        public void addItem(String p_img, String p_id) { // to add the data icon and data name(title)

            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.prof_url = p_img;
            addInfo.pro_id = p_id;
            mListData.add(addInfo);// to add the data information in list.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { // to implement the view object putting in actual list view.
            ViewHolder holder;
            if (convertView == null) { // convertview that is delivererd the each item of list view by android.
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  // read the layout information to make layout object.
                convertView = inflater.inflate(R.layout.friend, null);

                holder.pro_img = (ImageView) convertView.findViewById(R.id.kt_user_profile);//to put icon in the holder.
                holder.pro_id = (TextView) convertView.findViewById(R.id.kt_user_id);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            if (mData.prof_url != null) {//if icon don't have null value
                ImageLoaderTask task = new ImageLoaderTask(holder.pro_img, mData.prof_url);
                task.execute();
                holder.pro_id.setText(mData.pro_id);
            } else {
                holder.pro_img.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    public static class ListData { //To create the object to have a list information

        public String prof_url;
        public String pro_id;

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
            this.imageView.setImageBitmap(bitmap);
        }

    }
}
