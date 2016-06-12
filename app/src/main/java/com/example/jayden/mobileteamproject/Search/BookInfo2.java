package com.example.jayden.mobileteamproject.Search;

// Created by Jayden on 2016-06-12.

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.Friend.Friend;
import com.example.jayden.mobileteamproject.Friend.FriendAdapter;
import com.example.jayden.mobileteamproject.R;

import java.util.ArrayList;

public class BookInfo2 extends Activity {

    TextView title;
    TextView author;
    TextView description;
    TextView publisher;
    ImageView imageView;
    Bundle myBundle;
    Intent receivedIntent;
    private AQuery aq;
    ListView fListView;
    protected ArrayList<Friend> lists;
    FriendAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info2);

        aq = new AQuery( this );
        title = (TextView)findViewById(R.id.infoTitle);
        author = (TextView)findViewById(R.id.infoAuthor);
        description = (TextView)findViewById(R.id.infoDescription);
        publisher = (TextView)findViewById(R.id.infoPublisher);
        imageView = (ImageView)findViewById(R.id.imageView);
        fListView = (ListView)findViewById(R.id.fListView);
        lists = new ArrayList<Friend>();
        adapter = new FriendAdapter(this, lists);
        fListView.setAdapter(adapter);


        receivedIntent = getIntent();
        myBundle = receivedIntent.getExtras();
        String title_ = myBundle.getString("title");
        String author_ = myBundle.getString("author");
        String description_ = myBundle.getString("description");
        String publisher_ = myBundle.getString("publisher");
        String img_ = myBundle.getString("img");
        myBundle.putString("select", "false"); // 선택버튼 눌렀는지 판단. false(안누름)으로 초기화

        title.setText(Html.fromHtml(title_));
        author.setText(Html.fromHtml(author_));
        description.setText(Html.fromHtml(description_));
        publisher.setText(Html.fromHtml(publisher_));
        aq.id(imageView).image(img_);


    }
}
