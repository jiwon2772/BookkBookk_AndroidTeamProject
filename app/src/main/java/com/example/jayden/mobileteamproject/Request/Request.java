package com.example.jayden.mobileteamproject.Request;

import android.graphics.Bitmap;

public class Request {
    public String prof_url;
    public String pro_id;
    //public Drawable alarm;
    public String timer;
    public Bitmap bitmap;

    public Request(String p_img, String p_id, String tm)
    {
        prof_url = p_img;
        pro_id = p_id;
        timer= tm;
    }

}
