package com.example.jayden.mobileteamproject.Friend;

/**
 * Created by Jayden on 2016-06-12.
 */

import android.widget.BaseAdapter;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.support.v4.app.FragmentActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
        import android.widget.TextView;
import android.widget.Toast;

import com.example.jayden.mobileteamproject.Main.USERINFO;
import com.example.jayden.mobileteamproject.R;
import com.example.jayden.mobileteamproject.Search.BookInfo2;

import java.io.BufferedReader;
import java.io.IOException;
        import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by SAMSUNG on 2016-06-11.
 */
public class FriendAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity m_activity;
    private ArrayList<Friend> arr;
    private int friend;
    public FriendAdapter(Activity act, ArrayList<Friend> arr_list) {
        this.m_activity = act;
        arr = arr_list;
        mInflater = (LayoutInflater) m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FriendAdapter(FragmentActivity activity, int friend, ArrayList<Friend> arr_list) {
        this.m_activity = activity;
        friend= friend;
        arr = arr_list;
        mInflater = (LayoutInflater) m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            int res = 0;
            res = R.layout.user_having_book;
            convertView = mInflater.inflate(res, parent, false);
        }

        ImageView pro_img = (ImageView) convertView.findViewById(R.id.ivImg);
        TextView pro_nick = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView date = (TextView)convertView.findViewById(R.id.tvDescription);

        ImageLoaderTask task = new ImageLoaderTask(pro_img,arr.get(position).prof_url);
        task.execute();
        pro_nick.setText("" + arr.get(position).nick);
        date.setText("" + arr.get(position).date);


        Button btnRequest = (Button)convertView.findViewById(R.id.request);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phpInsert task = new phpInsert();
                task.execute("http://jiwon2772.16mb.com/addRequest.php?userId=" + USERINFO.id + "&destId=" + arr.get(position).userId + "&postId=" +
                        arr.get(position).post);
                Toast.makeText(parent.getContext(),"요청이 전송되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }
    public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView profileView;
        private String profileAddress;

        public ImageLoaderTask(ImageView imgView, String imgAddress) {
            this.profileView = imgView;
            this.profileAddress = imgAddress;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                InputStream is = new java.net.URL(this.profileAddress).openStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("ImageLoaderTask", "Cannot load image from " + this.profileAddress);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            this.profileView.setImageBitmap(bitmap);
        }

    }
    private class phpInsert extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultText = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            resultText.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultText.toString();


        }

        protected void onPostExecute(String str) {
            if (str.equals("1")) {
                //Toast.makeText(getApplicationContext(),"DB Insert Complete.",Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(),"DB Insert Failed.",Toast.LENGTH_LONG).show();
            }


        }
    }
}

