package com.example.jayden.mobileteamproject.Posting;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Jayden on 2016-05-12.
 */
public class Post implements Serializable {
    public String postId;
    public long userId;
    public String bookUrl;
    public String profileUrl;
    public String nickname;
    public String time;
    public String text;
    public Bitmap[] bitmap;

    public Post(String p_id, long i, String b, String p, String n, String ti, String te) {
        postId = p_id;
        userId = i;
        bookUrl = b;
        profileUrl = p;
        nickname = n;
        time = ti;
        text = te;
    }
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
    public Post(long i, String b, String date, String txt) {
        userId = i;
        bookUrl = b;
        time = date;
        text = txt;
    }
}