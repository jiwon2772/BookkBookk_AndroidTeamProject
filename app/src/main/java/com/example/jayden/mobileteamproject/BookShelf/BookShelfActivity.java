package com.example.jayden.mobileteamproject.BookShelf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.jayden.mobileteamproject.Main.MainActivity;
import com.example.jayden.mobileteamproject.Posting.Post;
import com.example.jayden.mobileteamproject.Posting.PostAdapter;
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

/**
 * Created by Administrator on 2016-06-08.
 */
public class BookShelfActivity extends Activity {

    protected ArrayList<Post> shelfLists;
    Bitmap[] imageIDs;
    int length;
    int current = 0;
    phpDown task;
    BookShelfView gridViewImages;
    ImageGridAdapter imageGridAdapter;

    android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg){
            current = current + 1;
            if(length == current) {
                imageGridAdapter = new ImageGridAdapter(BookShelfActivity.this, shelfLists);
                imageGridAdapter.notifyDataSetInvalidated();
                gridViewImages.setAdapter(imageGridAdapter);
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookshelf);

        Intent a = getIntent();
        long id = a.getLongExtra("id", 0);

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

                imageIDs = new Bitmap[ja.length()];
                length = ja.length();

                for (int i = 0; i < ja.length(); i++) {
                    // web에서 가져온 정보를 안드로이드 객체에 넣어준다.
                    JSONObject jo = (JSONObject)ja.get(i);
                    userId = jo.getLong("id");
                    bookUrl = jo.getString("book");
                    text = jo.getString("text");
                    date = jo.getString("date");

                    shelfLists.add(new Post(userId, bookUrl, date, text));

                    //가져온 url을 통해 이미지를 모두 저장
                    class ImageDown extends Thread {
                        int count;
                        String profile;
                        String bookUrl;

                        ImageDown(int i, String book) {
                            bookUrl = book;
                            count = i;
                        }
                        @Override
                        public void run() {
                            try {
                                InputStream is = new java.net.URL(bookUrl).openStream();
                                imageIDs[count] = BitmapFactory.decodeStream(is);
                                Message msg = mHandler.obtainMessage(count);
                                mHandler.sendMessage(msg);
                            } catch (IOException e) {
                                Log.e("ImageLoaderTask", "Cannot load image from " + profile);
                            }
                        }
                    }
                    ImageDown task_image = new ImageDown(i,bookUrl);
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

