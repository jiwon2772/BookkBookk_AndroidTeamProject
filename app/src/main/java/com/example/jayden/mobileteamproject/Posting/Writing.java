package com.example.jayden.mobileteamproject.Posting;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.jayden.mobileteamproject.R;
import com.example.jayden.mobileteamproject.Search.BookSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016-05-20.
 */
public class Writing extends AppCompatActivity implements View.OnClickListener {

    LinearLayout selected_book;
    LinearLayout SearchBook;
    ImageView ivImg;
    TextView tvTitle;
    TextView tvAuthor;
    TextView tvDescription;
    Button delete;
    Button addBook;
    EditText text;
    CheckBox isHave;
    String img; // 등록한 책의 Url
    String ISBN; //책의 고유번호

    Intent intent;
    phpInsert task_insert;

    // 책을 등록했는지 확인해준다.
    boolean isAdded = false;


    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing);

        aq = new AQuery(this);
        selected_book = (LinearLayout) findViewById(R.id.select_book);
        ivImg = (ImageView) findViewById(R.id.ivImg);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        addBook = (Button) findViewById(R.id.addBook);
        isHave = (CheckBox)findViewById(R.id.chk);
        SearchBook = (LinearLayout)findViewById(R.id.SearchBook);
        text = (EditText)findViewById(R.id.postText);
        img = null; //초기화

        SearchBook.setOnClickListener(this);
        addBook.setOnClickListener(this);

        // id값을 넘겨받기 위해 intent를 불러온다.
        intent = getIntent();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == SearchBook.getId()) {
            Intent i = new Intent(Writing.this, BookSearch.class);
            Bundle myData = new Bundle();
            i.putExtras(myData);
            startActivityForResult(i, 2222);
        }
        else if (view.getId() == addBook.getId()) {
            if(img == null) {
                Toast.makeText(Writing.this,"책을 선택해 주셔야 합니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            String txt = text.getText().toString();

            int state = 0;
            if(isHave.isChecked() == true)
                state = 1;
            task_insert = new phpInsert();
            task_insert.execute("http://jiwon2772.16mb.com/addPost.php?userId=" + intent.getLongExtra("id", 0) + "&ISBN=" + ISBN + "&bookURL=" +
                    img + "&text=" + txt + "&isHave=" + state);
            Writing.this.finish();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ((requestCode == 2222) && (resultCode == Activity.RESULT_OK)) {
                Bundle myResults = data.getExtras();
                String author = myResults.getString("author");
                String description = myResults.getString("description");
                img = myResults.getString("img");
                String title = myResults.getString("title");
                ISBN = myResults.getString("ISBN");
                ISBN = ISBN.substring(0,10);


                // 책 등록 상태를 표시
                isAdded = true;

                tvAuthor.setText(Html.fromHtml(author));
                tvTitle.setText(Html.fromHtml(title));
                tvDescription.setText(Html.fromHtml(description));
                aq.id(ivImg).image(img);
                selected_book.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//onActivityResult

    private class phpInsert extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultText = new StringBuilder();
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
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            resultText.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultText.toString();


        }

        protected void onPostExecute(String str) {
            if (str.equals("1")) {
                //Toast.makeText(getApplicationContext(),"DB Insert Complete.",Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(),"DB Insert Failed.",Toast.LENGTH_LONG).show();
            }


        }
    }
}
