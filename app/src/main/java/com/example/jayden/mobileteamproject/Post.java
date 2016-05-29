package com.example.jayden.mobileteamproject;

import android.graphics.Bitmap;

/**
 * Created by Jayden on 2016-05-12.
 */
public class Post {
    public Long userId;
    public String bookUrl;
    public String profileUrl;
    public String nickname;
    public String time;
    public String text;
    public Bitmap[] bitmap;


    public Post(long i, String b, String p, String n, String ti, String te,Bitmap[] bit) {
        userId = i;
        bookUrl = b;
        profileUrl = p;
        nickname = n;
        time = ti;
        text = te;
        bitmap = bit;
    }
    public Post(long i, String b, String p, String n, String ti, String te) {
        userId = i;
        bookUrl = b;
        profileUrl = p;
        nickname = n;
        time = ti;
        text = te;
        bitmap = null;
    }
}