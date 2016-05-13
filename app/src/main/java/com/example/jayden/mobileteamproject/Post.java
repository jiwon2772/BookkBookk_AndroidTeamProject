package com.example.jayden.mobileteamproject;

/**
 * Created by Jayden on 2016-05-12.
 */
public class Post {
    public String bookUrl;
    public String profileUrl;
    public String userID;
    public String time;
    public String text;


    public Post(String b, String p, String u, String ti, String te) {
        bookUrl = b;
        profileUrl = p;
        userID = u;
        time = ti;
        text = te;
    }
}
