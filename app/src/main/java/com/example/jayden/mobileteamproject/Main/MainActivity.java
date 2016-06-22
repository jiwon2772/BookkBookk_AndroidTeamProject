package com.example.jayden.mobileteamproject.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
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
import android.util.Log;
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

    static final int PROGRESS_DIALOG = 0;
    ProgressThread progressThread;
    ProgressDialog progressDialog;

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

        //프로그래스바 초기 설정
        showDialog(PROGRESS_DIALOG);

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
        onClickLogout();
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
    //프로그래스바 만들기
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case PROGRESS_DIALOG:
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
                return progressDialog;
            default:
                return null;
        }
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                int total = msg.arg1;
                progressDialog.setProgress(total);
                if (total >= 40) {
                    dismissDialog(PROGRESS_DIALOG);
                    progressThread.setState(ProgressThread.STATE_DONE);
                }
            }
        };
        switch (id) {
            case PROGRESS_DIALOG:
                progressDialog.setProgress(0);
                progressThread = new ProgressThread(handler);
                progressThread.start();
        }
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
    /** Nested class that performs progress calculations (counting) */
    private class ProgressThread extends Thread {
        Handler mHandler;
        final static int STATE_DONE = 0;
        final static int STATE_RUNNING = 1;
        int mState;
        int total;

        ProgressThread(Handler h) {
            mHandler = h;
        }

        public void run() {
            mState = STATE_RUNNING;
            total = 0;
            while (mState == STATE_RUNNING) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("ERROR", "Thread Interrupted");
                }
                Message msg = mHandler.obtainMessage();
                msg.arg1 = total;
                mHandler.sendMessage(msg);
                total++;
            }
        }
        /* sets the current state for the thread,
            * used to stop the thread */
        public void setState(int state) {
            mState = state;
        }
    }
}


