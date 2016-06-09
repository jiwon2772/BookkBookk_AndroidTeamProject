package com.example.jayden.mobileteamproject.Request;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jayden.mobileteamproject.Friend.FriendList;
import com.example.jayden.mobileteamproject.Main.LoginActivity;
import com.example.jayden.mobileteamproject.R;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RequestList extends AppCompatActivity {
    private android.widget.ListView ListView = null;
    private ListViewAdapter ListAdapter = null;
    Intent passIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        ListView = (ListView) findViewById(R.id.ListView);
        ListAdapter = new ListViewAdapter(this);
        ListView.setAdapter(ListAdapter);
        passIntent = new Intent(this, FriendList.class);
        Intent a = getIntent();
        String profile = a.getStringExtra("profileImage");
        String nick = a.getStringExtra("nick");
        passIntent.putExtra("nick", nick);
        passIntent.putExtra("profileImage",profile);
        ListAdapter.addItem(profile, nick, getResources().getDrawable(R.drawable.alarm), "몇 분 전");
        ListAdapter.addItem(profile, nick, getResources().getDrawable(R.drawable.alarm), "몇 분 전");
        ListAdapter.addItem(profile, nick, getResources().getDrawable(R.drawable.alarm), "몇 분 전");
        ListAdapter.addItem(profile, nick, getResources().getDrawable(R.drawable.alarm), "몇 분 전");
    }

    private class ViewHolder {//to manage the view (image and text view) like putting in the holder

        public ImageView pro_img;
        public TextView pro_id;
        public ImageView alarm;
        public TextView timer;

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

        public void addItem(String p_img, String p_id, Drawable a_icon, String tm) { // to add the data icon and data name(title)

            ListData addInfo = new ListData();
            addInfo.prof_url = p_img;
            addInfo.pro_id = p_id;
            addInfo.alarm = a_icon;
            addInfo.timer = tm;

            mListData.add(addInfo);// to add the data information in list.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { // to implement the view object putting in actual list view.
            ViewHolder holder;
            if (convertView == null) { // convertview that is delivererd the each item of list view by android.
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  // read the layout information to make layout object.
                convertView = inflater.inflate(R.layout.item, null);

                holder.pro_img = (ImageView) convertView.findViewById(R.id.kt_user_profile);//to put icon in the holder.
                holder.pro_id = (TextView) convertView.findViewById(R.id.kt_user_id);
                holder.alarm = (ImageView) convertView.findViewById(R.id.timer_view);//to put icon in the holder.
                holder.timer = (TextView) convertView.findViewById(R.id.timer);
                Button re_btn = (Button) convertView.findViewById(R.id.request);

                re_btn.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "요청 수락", Toast.LENGTH_SHORT).show();
                        startActivity(passIntent);
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);
            ImageLoaderTask task = new ImageLoaderTask(holder.pro_img, mData.prof_url);
            task.execute();
            holder.pro_id.setText(mData.pro_id);
            holder.alarm.setImageDrawable(mData.alarm);//to set image in icon
            holder.timer.setText(mData.timer);

            return convertView;
        }
    }

    public static class ListData { //To create the object to have a list information

        public String prof_url;
        public String pro_id;
        public Drawable alarm;
        public String timer;

    }

    public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView imageView;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClickLogout();
    }

    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectLoginActivity();
            }
        });
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
