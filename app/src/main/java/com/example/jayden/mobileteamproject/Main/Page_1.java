package com.example.jayden.mobileteamproject.Main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.example.jayden.mobileteamproject.R;
import com.example.jayden.mobileteamproject.Search.BookAdaptor;
import com.example.jayden.mobileteamproject.Search.BookVO;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jayden on 2016-06-11.
 */
public class Page_1 extends android.support.v4.app.Fragment implements View.OnClickListener {

    private  final String BOOK_URL = "https://openapi.naver.com/v1/search/book.xml?query=%s&display=20";

    private AQuery mAq;
    private ArrayList<BookVO> mBookList;
    BookAdaptor mAdaptor;
    EditText etBook;
    ListView listBook;
    Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.book_search, container, false);

        etBook = (EditText)linearLayout.findViewById(R.id.etBook);
        listBook = (ListView)linearLayout.findViewById(R.id.listBook);
        btnSubmit = (Button)linearLayout.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        mBookList = new ArrayList<BookVO>();
        mAq = new AQuery(getActivity());

        //3.3 리스트뷰와 어댑터 연결
        mAdaptor = new BookAdaptor(getActivity(), mBookList);
        listBook.setAdapter(mAdaptor);



        // 리스트 클릭 이벤트
        listBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String author = mBookList.get(position).getAuthor();
                String title = mBookList.get(position).getTitle();
                String description = mBookList.get(position).getDescription();
                String img = mBookList.get(position).getImgUrl();
                String isbn = mBookList.get(position).getISBN();

            }

        });


        return linearLayout;
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
