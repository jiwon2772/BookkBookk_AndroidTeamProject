package com.example.jayden.mobileteamproject.BookShelf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.Friend.Friend;
import com.example.jayden.mobileteamproject.Friend.FriendAdapter;
import com.example.jayden.mobileteamproject.Main.USERINFO;
import com.example.jayden.mobileteamproject.Posting.Post;
import com.example.jayden.mobileteamproject.R;
import com.example.jayden.mobileteamproject.Request.RequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-05-13.
 * 서재에서 책 누르면 나오는 책 정보 액티비티
 */
public class ImageActivity extends ActionBarActivity {

    AQuery aq;
    Post post;
    int length;
    RequestAdapter adapter;
    ListView fListView;
    ArrayList<Friend> lists;

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
        post = (Post) receivedIntent.getSerializableExtra("post");

        //----------------------------------------------------------------
        // 확대되는 이미지를 보여주기 위해 ImageView 뷰를 설정합니다.
        ImageView book = (ImageView) findViewById(R.id.bookimage);
        ImageView profile = (ImageView) findViewById(R.id.profile);
        TextView user = (TextView) findViewById(R.id.user);
        TextView time = (TextView) findViewById(R.id.time);
        TextView text = (TextView) findViewById(R.id.text);
        user.setText(post.nickname);         // 유저아이디를 각 리스트뷰에 연결해줌
        time.setText(post.time);
        text.setText(post.text);
        setImage(book, post.bookUrl);
        setImage(profile, post.profileUrl);
        fListView = (ListView)findViewById(R.id.requestList);
        aq = new AQuery( this );
        lists = new ArrayList<Friend>();


        phpDown task =  new phpDown();
        task.execute("http://jiwon2772.16mb.com/getRequest.php?userId=" + USERINFO.id + "&postId=" + post.postId);
    }

    private void setImage(ImageView imageView, String url) {
        aq.id(imageView).image(url);
    }
    private class phpDown extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "EUC-KR"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();

        }

        protected void onPostExecute(String str) {
            long userId;
            String nickname;
            String profile;
            String date;
            String postId;

            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results"); //get the JSONArray which I made in the php file. the name of JSONArray is "results"

                length = ja.length();

                for (int i = 0; i < ja.length(); i++) {
                    // web에서 가져온 정보를 안드로이드 객체에 넣어준다.
                    JSONObject jo = (JSONObject) ja.get(i);
                    userId = jo.getLong("sourceId");
                    nickname = jo.getString("nickname");
                    profile = jo.getString("profileURL");
                    date = jo.getString("date");
                    postId = jo.getString("postId");

                    lists.add(new Friend(userId, profile, nickname, date, postId));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new RequestAdapter(ImageActivity.this, lists);
            adapter.notifyDataSetInvalidated();
            fListView.setAdapter(adapter);
        }

    }
}

