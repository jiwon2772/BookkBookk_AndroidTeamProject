package com.example.jayden.mobileteamproject.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Contacts;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jayden.mobileteamproject.BookShelf.BookShelfActivity;
import com.example.jayden.mobileteamproject.Friend.PeopleActivity;
import com.example.jayden.mobileteamproject.Posting.Writing;
import com.example.jayden.mobileteamproject.R;
import com.example.jayden.mobileteamproject.Request.RequestList;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;


public class MainActivity extends ActionBarActivity {

    private String[] navItems = {"BOOK", "PEOPLE", "DEVELOPER","LOGOUT"};
    private ListView lvNavList;
    private FrameLayout flContainer;

    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    Bundle MyInfo;

    int MAX_PAGE = 2;
    long userId; //User ID
    String nick;
    String profile;

    Intent userInfo;
    Fragment cur_fragment = new Fragment();
    Fragment page1, page2, page3;

    ActionBar thisActionBar;
    FragmentPagerAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        thisActionBar = getSupportActionBar();

        setCustomActionbar();
        lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);
        lvNavList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.drawable.menu1, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        dlDrawer.setDrawerListener(dtToggle);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        pageAdapter = new adapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(1);

        userInfo = getIntent();
        userId = userInfo.getLongExtra("id", 0);
        nick = userInfo.getStringExtra("nick");
        profile = userInfo.getStringExtra("profileImage");
        MyInfo = new Bundle();
        MyInfo.putLong("id", userId);
        MyInfo.putString("nick", nick);
        MyInfo.putString("profile",profile);

        //fragment 초기화
        page1 = new Page_1();
        page2 = new Page_2();
        page3 = new Page_3();
        page1.setArguments(MyInfo);
    }

    private class adapter extends FragmentPagerAdapter {
        public adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 0 || MAX_PAGE <= position)
                return null;
            switch (position) {
                case 0:
                    cur_fragment = page1;
                    break;
                case 1:
                    cur_fragment = page2;
                    break;
//                case 2:
//                    cur_fragment = page3;
//                    break;
            }
            return cur_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //lists.clear();
        //task = new phpDown();
        //task.execute("http://jiwon2772.16mb.com/mainActivity.php");//도메인을 실행
    }

    @Override
    public void onBackPressed() {
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("부끄부끄")
                .setView(inflater.inflate(R.layout.dialog_exit, null))
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.this.finish(); // 종료
                    }
                })
                .setNegativeButton("뒤로 돌아가기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
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
                //redirectLoginActivity();
            }
        });
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void setCustomActionbar() {
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        getSupportActionBar().setCustomView(mCustomView);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(255, 255, 255, 255)));

        ImageButton slideBtn = (ImageButton) findViewById(R.id.action_slide);
        ImageButton writeBtn = (ImageButton) findViewById(R.id.action_write);
        ImageButton peopleBtn = (ImageButton) findViewById(R.id.action_people);
        ImageButton bookBtn = (ImageButton) findViewById(R.id.action_book);
        //메뉴창을 띄웁니다.
        slideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlDrawer.openDrawer(lvNavList);
                dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
            }
        });
        //People정보 창을 띄웁니다.
        peopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PeopleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(userInfo);
                startActivity(intent);
            }
        });
        // 글을 작성합니다
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Writing.class);
                intent.putExtras(userInfo);
                startActivity(intent);
            }
        });
        // book을 누를경우 책장을 엽니다.
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookShelfActivity.class);
                intent.putExtras(userInfo);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedTab", getSupportActionBar().getSelectedNavigationIndex());
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(MainActivity.this, BookShelfActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Intent intent2 = new Intent(MainActivity.this, BookShelfActivity.class);
                    startActivity(intent2);
                    break;
                case 2:
                    Intent intent3 = new Intent(MainActivity.this, BookShelfActivity.class);
                    startActivity(intent3);
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            dlDrawer.closeDrawer(lvNavList);
        }
    }

}


