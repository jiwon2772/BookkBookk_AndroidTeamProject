package com.example.jayden.mobileteamproject.Main;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.jayden.mobileteamproject.Friend.Friend;
import com.example.jayden.mobileteamproject.R;
import com.example.jayden.mobileteamproject.Request.AcceptAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jayden on 2016-06-11.
 */
public class Page_3 extends android.support.v4.app.Fragment {

    phpDown task;
    int length;
    AcceptAdapter adapter;
    ListView fListView;
    ArrayList<Friend> lists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_finish, container, false);
        fListView = (ListView)getActivity().findViewById(R.id.finishList);
        lists = new ArrayList<Friend>();

        task =  new phpDown();
        task.execute("http://jiwon2772.16mb.com/getAccept.php?userId=" + USERINFO.id);

        return linearLayout;
    }
    private class phpDown extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                    userId = jo.getLong("destId");
                    nickname = jo.getString("nickname");
                    profile = jo.getString("profileURL");
                    date = jo.getString("date");
                    postId = jo.getString("postId");

                    lists.add(new Friend(userId, profile, nickname, date, postId));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new AcceptAdapter(getActivity(), lists);
            fListView.setAdapter(adapter);
            adapter.notifyDataSetInvalidated();
        }

    }
}
