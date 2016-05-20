package com.example.jayden.mobileteamproject;

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


    public Post(long i, String b, String p, String n, String ti, String te) {
        userId = i;
        bookUrl = b;
        profileUrl = p;
        nickname = n;
        time = ti;
        text = te;
    }
}