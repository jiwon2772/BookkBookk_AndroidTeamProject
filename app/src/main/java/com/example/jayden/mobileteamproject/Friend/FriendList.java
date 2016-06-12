package com.example.jayden.mobileteamproject.Friend;

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

import com.example.jayden.mobileteamproject.R;
import com.kakao.usermgmt.UserManagement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FriendList extends AppCompatActivity {
    protected ArrayList<Friend> lists;
    FriendAdapter adapter;
    ListView listView;
    Intent passIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        listView = (ListView) findViewById(R.id.fListView);
        lists = new ArrayList<Friend>();
        adapter = new FriendAdapter(this, lists);
        //adapter.notifyDataSetInvalidated();
        listView.setAdapter(adapter);


        Intent a = getIntent();
        String profile = a.getStringExtra("profileImage");
        String nick = a.getStringExtra("nick");

        passIntent.putExtra("nick", nick);
        passIntent.putExtra("profileImage", profile);

        lists.add(new Friend(profile, nick));
    }

}
