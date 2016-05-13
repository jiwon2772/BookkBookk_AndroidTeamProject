package com.example.jayden.mobileteamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

// from 2016/05/06 by Jiwon
public class MainActivity extends AppCompatActivity {

    private ArrayList<Post> lists;
    PostAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.postlist);
        lists = new ArrayList<Post>();
        String address = "http://thumbnail.egloos.net/600x0/http://pds21.egloos.com/pds/201306/12/82/e0180182_51b79da89ad55.jpg";
        //String profile = "http://www.odilederousiers.fr/charles/dl/profile.png";
        //lists.add(new Post(address,profile,"Jayden Jung","9분전","이 책은 정말 정말 재미있는 책입니다 \n 궁금하시죠? \n ㅋㅋㅋㅋㅋㅋㅋ안알려줌"));
        //lists.add(new Post("http://seoultowallstreet.com/wp-content/uploads/2012/09/Book-Cover-4.jpg",profile,"Jacob Martin","2시간전","이 책은 음 뭐랄까 한쿡 사람들이 좋아할것 같아요\n kkkkkkkkkkkkkkkkkkkkk 조크입니다"));
        //lists.add(new Post("http://cfile23.uf.tistory.com/image/187DC64350C6E9CE0622EE",profile,"홍기욱","4시간전","자기전에 읽었는데 재밌더라구요 엥?"));
        adapter = new PostAdapter(MainActivity.this, lists);

        listView.setAdapter(adapter);

        Intent a = getIntent();
        String nick = a.getStringExtra("nick");
        String profile = a.getStringExtra("profileImage");

        lists.add(new Post(address,profile,nick,"9분전","이 책은 정말 정말 재미있는 책입니다 \n 궁금하시죠? \n ㅋㅋㅋㅋㅋㅋㅋ안알려줌"));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClickLogout();
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
}
