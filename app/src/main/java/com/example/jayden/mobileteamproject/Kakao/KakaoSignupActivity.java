package com.example.jayden.mobileteamproject.Kakao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.example.jayden.mobileteamproject.Main.MainActivity;
import com.example.jayden.mobileteamproject.R;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import com.kakao.util.helper.log.Logger;

import com.example.jayden.mobileteamproject.Main.LoginActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class KakaoSignupActivity extends AppCompatActivity {
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    Intent passIntent;
    phpInsert task_insert;

    boolean isFirst  = true; //It Check this is first login.

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passIntent = new Intent(this, MainActivity.class);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                long id = userProfile.getId();
                String nickname = userProfile.getNickname();
                String profileUrl = userProfile.getProfileImagePath();
//                try {
//                    //nickname = URLEncoder.encode(nickname, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                //id가 어플 DB에 들어와있는지 확인하고 프로필과 닉네임 업데이트를 해줌
                task_insert = new phpInsert();
                task_insert.execute("http://jiwon2772.16mb.com/register.php?userId=" + id + "&nickname=" + nickname + "&profileURL=" + profileUrl);


                passIntent.putExtra("id",id);
                passIntent.putExtra("nick", nickname);
                passIntent.putExtra("profileImage", profileUrl);

                //get Kakao ID if this is first login with this user.
                if(true) {
                    LayoutInflater inflater = getLayoutInflater();
                    AlertDialog alertDialog = new AlertDialog.Builder(KakaoSignupActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Kakao ID")
                            .setView(inflater.inflate(R.layout.dialog_kakao, null))
                            .setPositiveButton("다음", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    redirectMainActivity(); // 로그인 성공시 MainActivity로
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                            .setCancelable(false)
                            .create();
                    alertDialog.show();
                }
                //redirectMainActivity(); // 로그인 성공시 MainActivity로
            }
        });
    }

    private void redirectMainActivity() {
        startActivity(passIntent);
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    private class phpInsert extends AsyncTask<String, Integer,String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder resultText = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if(line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            resultText.append(line);
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return resultText.toString();

        }

        protected void onPostExecute(String str){
            if(str.equals("1")){
                //Toast.makeText(getApplicationContext(),"DB Insert Complete.",Toast.LENGTH_LONG).show();
            }else{
                //Toast.makeText(getApplicationContext(),"DB Insert Failed.",Toast.LENGTH_LONG).show();
            }

        }

    }
}
