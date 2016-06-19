package com.example.jayden.mobileteamproject.BookShelf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.Posting.Post;
import com.example.jayden.mobileteamproject.R;

/**
 * Created by Administrator on 2016-05-13.
 * 서재에서 책 누르면 나오는 책 정보 액티비티
 */
public class ImageActivity extends ActionBarActivity {

    AQuery aq;
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mybook);
        aq = new AQuery(this);

        // 액션바 셋팅
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.book_actionbar, null);
        getSupportActionBar().setCustomView(mCustomView);
        // 액션바 셋팅 끝

        Intent receivedIntent = getIntent();
        post = (Post)receivedIntent.getSerializableExtra("post");

        //----------------------------------------------------------------
        // 확대되는 이미지를 보여주기 위해 ImageView 뷰를 설정합니다.
        ImageView book = (ImageView)findViewById(R.id.bookimage);
        ImageView profile = (ImageView) findViewById(R.id.profile);
        TextView user = (TextView) findViewById(R.id.user);
        TextView time = (TextView) findViewById(R.id.time);
        TextView text = (TextView) findViewById(R.id.text);
        user.setText(post.nickname);         // 유저아이디를 각 리스트뷰에 연결해줌
        time.setText(post.time);
        text.setText(post.text);
        setImage(book,post.bookUrl);
        setImage(profile,post.profileUrl);
    }

    private void setImage(ImageView imageView, String url) {
        aq.id(imageView).image(url);
    }
}
