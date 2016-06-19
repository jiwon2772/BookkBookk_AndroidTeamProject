package com.example.jayden.mobileteamproject.Friend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.jayden.mobileteamproject.Posting.Post;
import com.example.jayden.mobileteamproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddFriend extends ActionBarActivity {

    protected ArrayList<Friend> lists;
    FriendAdapter adapter;
    ListView listView;
    int length; // the number of lists

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // primary sections of the activity.
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.people_actionbar, null);
        getSupportActionBar().setCustomView(mCustomView);

        //기본 변수 초기화
        listView = (ListView) findViewById(R.id.fListView);
        lists = new ArrayList<Friend>();
        adapter = new FriendAdapter(this, lists);
        //adapter.notifyDataSetInvalidated();
        listView.setAdapter(adapter);
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

        /*
        protected void onPostExecute(String str){
            txtView.setText(str);
        }
        */

        protected void onPostExecute(String str) {
            long userId;
            String nickname;
            String profile;

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

                    lists.add(new Friend(userId, profile, nickname));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
