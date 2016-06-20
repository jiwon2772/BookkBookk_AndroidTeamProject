package com.example.jayden.mobileteamproject.Friend;

/**
 * Created by Jayden on 2016-06-12.
 */
public class Friend {
    public long userId;
    public String prof_url;
    public String nick;
    public String date;

    public Friend(String p_img, String p_nick)
    {
        prof_url = p_img;
        nick = p_nick;
    }
    public Friend(long p_id,String p_img,String p_nick )
    {
        userId = p_id;
        prof_url = p_img;
        nick = p_nick;
    }
    public Friend(long p_id,String p_img,String p_nick,String dat)
    {
        userId = p_id;
        prof_url = p_img;
        nick = p_nick;
        date = dat;
    }
}

