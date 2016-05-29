package com.example.jayden.mobileteamproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WriteActivity extends AppCompatActivity {

    phpInsert task_insert;
    Button add;
    EditText text;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing);

        add = (Button)findViewById(R.id.addBook);
        text = (EditText)findViewById(R.id.text);

        // id값을 넘겨받기 위해 intent를 불러온다.
        intent = getIntent();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = text.getText().toString();
                try {
                    txt = URLEncoder.encode(new String(txt.getBytes("UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                task_insert = new phpInsert();
                task_insert.execute("http://jiwon2772.16mb.com/addPost.php?userId=" + intent.getLongExtra("id", 0) + "&bookURL="  +
                        "https://s-media-cache-ak0.pinimg.com/736x/d8/4a/9a/d84a9a9b018345c7048e76c10dc4dc84.jpg" + "&text=" + txt);
                WriteActivity.this.finish();
            }
        });
    }

    private class phpInsert extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultText = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if(line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            resultText.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return resultText.toString();

        }

        protected void onPostExecute(String str){
            if(str.equals("1")){
                //Toast.makeText(getApplicationContext(),"DB Insert Complete.",Toast.LENGTH_LONG).show();
            }else{
                //Toast.makeText(getApplicationContext(),"DB Insert Failed.",Toast.LENGTH_LONG).show();
            }

        }

    }
}
