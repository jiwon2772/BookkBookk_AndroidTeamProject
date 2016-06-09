package com.example.jayden.mobileteamproject.Search;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.example.jayden.mobileteamproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookSearch extends Activity implements View.OnClickListener{

    private  final String BOOK_URL = "https://openapi.naver.com/v1/search/book.xml?query=%s&display=20";

    private AQuery mAq;
    private ArrayList<BookVO> mBookList;
    BookAdaptor mAdaptor;
    EditText etBook;
    ListView listBook;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);

        etBook = (EditText)findViewById(R.id.etBook);
        listBook = (ListView)findViewById(R.id.listBook);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        mBookList = new ArrayList<BookVO>();
        mAq = new AQuery(this);

        //3.3 리스트뷰와 어댑터 연결
        mAdaptor = new BookAdaptor(this, mBookList);
        listBook.setAdapter(mAdaptor);



        // 리스트 클릭 이벤트
        listBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String author = mBookList.get(position).getAuthor();
                String title = mBookList.get(position).getTitle();
                String description = mBookList.get(position).getDescription();
                String img = mBookList.get(position).getImgUrl();
                String isbn =  mBookList.get(position).getISBN();

                // 번들 통해서 writing.xml에 선택한 책 정보를 넘긴다.
                Intent myLocalIntent = getIntent();
                Bundle myBundle = myLocalIntent.getExtras();

                myBundle.putString("author",author);
                myBundle.putString("title",title);
                myBundle.putString("description",description);
                myBundle.putString("img",img);
                myBundle.putString("ISBN",isbn);

                myLocalIntent.putExtras(myBundle);
                setResult(Activity.RESULT_OK, myLocalIntent);



                finish();

            }

        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnSubmit.getId()){
            String query = etBook.getText().toString();
            mBookList.clear();

            mAq.ajax(String.format(BOOK_URL, query), XmlDom.class, new AjaxCallback<XmlDom>() {
                @Override
                public void callback(String url, XmlDom object, AjaxStatus status) {
                    Log.d("LDK", "url: " + url);
                    Log.d("LDK", "status code: " + status.getCode());

                    // getElementsByTagName(tag) 와 동일, 노드리스트를 리턴받는다.
                    List<XmlDom> itemList =object.tags("item");
                    for(XmlDom item : itemList) {
                        //title 노드를 리턴
                        XmlDom titleNode = item.tag("title");
                        String strTitle = titleNode.text(); //텍스트노드의 텍스트를 가져옴.

                        String strAuthor = item.tag("author").text();
                        String strImg = item.tag("image").text();

                        BookVO book = new BookVO();
                        book.setTitle(strTitle);
                        book.setAuthor(strAuthor);
                        book.setImgUrl(strImg);
                        book.setDescription(item.tag("description").text());
                        mBookList.add(book);
                    }

                    //변경된 모델 데이터를 리스트 뷰에게 알려줘서 뷰를 갱신
                    mAdaptor.notifyDataSetChanged();
                }
            }.header("X-Naver-Client-Id", "7aWy98Ywds8IV1NEXUAL")
                    .header("X-Naver-Client-Secret", "fa1vHElxst"));
        }
    }
}

