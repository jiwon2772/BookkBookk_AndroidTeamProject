package com.example.jayden.mobileteamproject.BookShelf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.Posting.Post;
import com.example.jayden.mobileteamproject.R;

/**
 * Created by Administrator on 2016-05-13.
 * 서재에서 책 누르면 나오는 책 정보 액티비티
 */
public class ImageActivity extends Activity{

    AQuery aq;
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);
        aq = new AQuery(this);

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
