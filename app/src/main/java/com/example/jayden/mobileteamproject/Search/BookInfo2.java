package com.example.jayden.mobileteamproject.Search;

// Created by Jayden on 2016-06-12.

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.Friend.Friend;
import com.example.jayden.mobileteamproject.Friend.FriendAdapter;
import com.example.jayden.mobileteamproject.Main.USERINFO;
import com.example.jayden.mobileteamproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    int length;

    // 현재 눌러진 책의 대한 정보들을 가지고 있는 변수
    String title_;
    String author_;
    String description_;
    String publisher_;
    String img_;
    String ISBN_;
    // 현재 로그인 중인 사용자의 정보를 가지고 있습니다.
    long userId;
    String nick;
    String profile;

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


        receivedIntent = getIntent();
        myBundle = receivedIntent.getExtras();
        title_ = myBundle.getString("title");
        author_ = myBundle.getString("author");
        description_ = myBundle.getString("description");
        publisher_ = myBundle.getString("publisher");
        img_ = myBundle.getString("img");
        ISBN_ = myBundle.getString("ISBN");
        ISBN_ = ISBN_.substring(0, 10);

        //현재 접속중인 사용자의 정보를 저장
        userId = myBundle.getLong("id");
        nick = myBundle.getString("nick");
        profile = myBundle.getString("profile");

        myBundle.putString("select", "false"); // 선택버튼 눌렀는지 판단. false(안누름)으로 초기화

        title.setText(Html.fromHtml(title_));
        author.setText(Html.fromHtml(author_));
        description.setText(Html.fromHtml(description_));
        publisher.setText(Html.fromHtml(publisher_));
        aq.id(imageView).image(img_);

        phpDown task =  new phpDown();
        task.execute("http://jiwon2772.16mb.com/getUsersHaving.php?ISBN=" + ISBN_);


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
                    JSONObject jo = (JSONObject)ja.get(i);
                    userId = jo.getLong("id");
                    nickname = jo.getString("nick");
                    profile = jo.getString("profile");
                    date = jo.getString("date");
                    postId = jo.getString("postId");

                    lists.add(new Friend(userId, profile, nickname,date,postId));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new FriendAdapter(BookInfo2.this, lists);
            adapter.notifyDataSetInvalidated();
            fListView.setAdapter(adapter);
        }

    }
}
