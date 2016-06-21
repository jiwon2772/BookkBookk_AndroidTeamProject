package com.example.jayden.mobileteamproject.BookShelf;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.Posting.Post;
import com.example.jayden.mobileteamproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-06-08.
 */
public class BookShelfActivity extends ActionBarActivity {

    protected ArrayList<Post> shelfLists;
    int length;
    phpDown task;
    BookShelfView gridViewImages;
    ImageGridAdapter imageGridAdapter;
    ImageView profileView;
    TextView nickView;
    private AQuery aq;

    long id; //접속한 사용자의 아이디
    String profile;
    String nickname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf);

        // 액션바 셋팅
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.book_actionbar, null);
        getSupportActionBar().setCustomView(mCustomView);
        // 액션바 셋팅 끝

        aq = new AQuery( this );
        profileView = (ImageView)findViewById(R.id.user_profile);
        nickView = (TextView)findViewById(R.id.user_nick);

        Intent a = getIntent();
        id = a.getLongExtra("id", 0);
        profile = a.getStringExtra("profileImage");
        nickname = a.getStringExtra("nick");

        //상단 서재 주인 정보 세팅
        nickView.setText(nickname);
        aq.id(profileView).image(profile);

        shelfLists = new ArrayList<Post>();

        gridViewImages = (BookShelfView)findViewById(R.id.gridViewImages);
        task = new phpDown();
        task.execute("http://jiwon2772.16mb.com/loadShelf.php?userId=" + id);//도메인을 실행

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
            String postId;
            long userId;
            String nickname;
            String profile;
            String bookUrl;
            String text;
            String date;

            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results"); //get the JSONArray which I made in the php file. the name of JSONArray is "results"

                length = ja.length();

                for (int i = 0; i < ja.length(); i++) {
                    // web에서 가져온 정보를 안드로이드 객체에 넣어준다.
                    JSONObject jo = (JSONObject)ja.get(i);
                    postId = jo.getString("postId");
                    userId = jo.getLong("id");
                    nickname = jo.getString("nick");
                    profile = jo.getString("profile");
                    bookUrl = jo.getString("book");
                    text = jo.getString("text");
                    date = jo.getString("date");

                    shelfLists.add(new Post(postId, userId, bookUrl, profile, nickname, date, text));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            imageGridAdapter = new ImageGridAdapter(BookShelfActivity.this, shelfLists, gridViewImages.getHeight(), gridViewImages.getWidth());
            imageGridAdapter.notifyDataSetInvalidated();
            gridViewImages.setAdapter(imageGridAdapter);
        }

    }
}

