package com.example.jayden.mobileteamproject;

import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

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
import java.util.logging.Handler;
import java.util.logging.LogRecord;

// from 2016/05/06 by Jiwon
public class MainActivity extends AppCompatActivity {

    protected ArrayList<Post> lists;
    PostAdapter adapter;
    Button menu;
    Button search;
    Button write;
    ListView listView;
    phpDown task;
    Bitmap[][] bitmapList;
    int length;
    int current= 0;


    long id;
    android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg){
            lists.get(msg.what).bitmap = bitmapList[msg.what];
            current = current + 1;
            if(length == current) {
                adapter = new PostAdapter(MainActivity.this, lists);
                adapter.notifyDataSetInvalidated();
                listView.setAdapter(adapter);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = (Button)findViewById(R.id.menu);
        search = (Button)findViewById(R.id.searchButton);
        write = (Button)findViewById(R.id.write);

        //각 버튼의 이벤트핸들러 구현
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.postlist);
        lists = new ArrayList<Post>();
        task = new phpDown();
        task.execute("http://jiwon2772.16mb.com/mainActivity.php");//도메인을 실행

        Intent a = getIntent();
        id = a.getLongExtra("id", 0);
        String nick = a.getStringExtra("nick");
        String profile = a.getStringExtra("profileImage");

//        adapter = new PostAdapter(MainActivity.this, lists);
//        adapter.notifyDataSetInvalidated();
//        listView.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //lists.clear();
        //task = new phpDown();
        //task.execute("http://jiwon2772.16mb.com/mainActivity.php");//도메인을 실행
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //onClickLogout();
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
            String bookUrl;
            String text;
            String date;

            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results"); //get the JSONArray which I made in the php file. the name of JSONArray is "results"

                bitmapList = new Bitmap[ja.length()][2];
                length = ja.length();

                for (int i = 0; i < ja.length(); i++) {
                    // web에서 가져온 정보를 안드로이드 객체에 넣어준다.
                    JSONObject jo = (JSONObject)ja.get(i);
                    userId = jo.getLong("id");
                    nickname = jo.getString("nick");
                    profile = jo.getString("profile");
                    bookUrl = jo.getString("book");
                    text = jo.getString("text");
                    date = jo.getString("date");

                    lists.add(new Post(userId, bookUrl, profile, nickname, date, text));

                    //가져온 url을 통해 이미지를 모두 저장
                    class ImageDown extends Thread {
                        int count;
                        String profile;
                        String bookUrl;

                        ImageDown(int i, String pro, String book) {
                            profile = pro;
                            bookUrl = book;
                            count = i;
                        }
                        @Override
                        public void run() {
                            try {
                                InputStream is = new java.net.URL(profile).openStream();
                                bitmapList[count][0] = BitmapFactory.decodeStream(is);
                                is = new java.net.URL(bookUrl).openStream();
                                bitmapList[count][1] = BitmapFactory.decodeStream(is);
                                Message msg = mHandler.obtainMessage(count);
                                mHandler.sendMessage(msg);
                            } catch (IOException e) {
                                Log.e("ImageLoaderTask", "Cannot load image from " + profile);
                            }
                        }
                    }
                    ImageDown task_image = new ImageDown(i,profile,bookUrl);
                    task_image.start();

                    //lists.add(new Post(userId, bookUrl, profile, nickname, date, text));
                    //adapter.notifyDataSetInvalidated();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            adapter = new PostAdapter(MainActivity.this, lists);
//            adapter.notifyDataSetInvalidated();
//            listView.setAdapter(adapter);
        }

    }
}
