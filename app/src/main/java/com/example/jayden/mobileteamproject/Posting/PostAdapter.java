package com.example.jayden.mobileteamproject.Posting;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jayden.mobileteamproject.Posting.Post;
import com.example.jayden.mobileteamproject.R;

import java.io.IOException;
import java.io.InputStream;
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

    public void updateProfile(ImageView first, ImageView second, Bitmap[] bitmap) {
        first.setImageBitmap(bitmap[0]);
        second.setImageBitmap(bitmap[1]);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int res = 0;
            res = R.layout.post_layout2;
            convertView = mInflater.inflate(res, parent, false);
        }

        ImageView book = (ImageView) convertView.findViewById(R.id.bookimage);
        ImageView profile = (ImageView) convertView.findViewById(R.id.profile);
        TextView user = (TextView) convertView.findViewById(R.id.user);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView text = (TextView) convertView.findViewById(R.id.text);
        //LinearLayout layout_view =  (LinearLayout)convertView.findViewById(R.id.view);

        user.setText(arr.get(position).nickname);         // 유저아이디를 각 리스트뷰에 연결해줌
        time.setText(arr.get(position).time);
        text.setText(arr.get(position).text);
        profile.setImageBitmap(arr.get(position).bitmap[0]);
        book.setImageBitmap(arr.get(position).bitmap[1]);

//        layout_view.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });
        // notifyDataSetChanged();
        return convertView;
    }
}
