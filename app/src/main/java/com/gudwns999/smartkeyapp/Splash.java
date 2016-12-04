package com.gudwns999.smartkeyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Splash extends Activity {
    private URL Url;
    private String strUrl,strCookie,result;
    private String code_name;

    // SharedPreferences에 저장할 때 key 값으로 사용됨
    public static final String PROPERTY_REG_ID = "registration_id";
    // SharedPreferences에 저장할 때 key 값으로 사용됨
    public static final String PROPERTY_APP_VERSION = "appVersion";

    static String SENDER_ID = "11111111111"; // 프로젝트 아이디
    static String SERVER_URL = "http://111.111.111.111:8080/gcm/sendGCMReg.do"; // 서버 주소
    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Toast.makeText(Splash.this, "사용자 정보를 받아 오고 있습니다", Toast.LENGTH_SHORT).show();
        //GCM
        context = this;

        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpGCM hu = new HttpGCM(context);
                String[] params = {SERVER_URL, "KEY:1234", "REG:" + regid};
                hu.execute(params);
            }
        });

        // gcm 등록
        gcm = GoogleCloudMessaging.getInstance(this); // GoogleCloudMessaging 클래스의 인스턴스를 생성한다
        regid = getRegistrationId(context); // 기존에 발급받은 등록 아이디를 가져온다
        if (regid.isEmpty()) { // 기존에 발급된 등록 아이디가 없으면 registerInBackground 메서드를 호출해 GCM 서버에 발급을 요청한다.
            System.out.println("************************************************* gcm 발급");
            registerInBackground();
        }
        System.out.println("************************************************* gcm regid : " + regid);
/*
        try{
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    strUrl = "http://gudwns999.com/carInfo.php"; //탐색하고 싶은 URL이다.
                }
                @Override
                protected Void doInBackground(Void... voids) {
                    try{
                        Url = new URL(strUrl);  // URL화 한다.
                        HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
                        conn.setRequestMethod("GET"); // get방식 통신
                        conn.setDoOutput(true);       // 쓰기모드 지정
                        conn.setDoInput(true);        // 읽기모드 지정
                        conn.setUseCaches(false);     // 캐싱데이터를 받을지 안받을지
                        conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                        strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

                        InputStream is = conn.getInputStream();        //input스트림 개방

                        StringBuilder builder = new StringBuilder();   //문자열을 담기 위한 객체
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));  //문자열 셋 세팅
                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line+ "\n");
                        }

                        result = builder.toString();
                    }catch(MalformedURLException | ProtocolException exception) {
                        exception.printStackTrace();
                    }catch(IOException io){
                        io.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    System.out.println(result);
                    //정보를 받고 1초 뒤 다음 화면으로 넘어간다.
                    Toast.makeText(Splash.this, "확인 완료", Toast.LENGTH_SHORT).show();
                    android.os.Handler handler = new android.os.Handler() {
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            Intent intent = new Intent(Splash.this, Main.class);
                            intent.putExtra("CODE_NAME",code_name);
                            startActivity(intent);
                            finish();
                        }
                    };
                    handler.sendEmptyMessageDelayed(0, 1000);
                    ///////////////////////////////////////////////
                }
            }.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
*/
    }
    // 저장된 reg id 조회
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context); // 이전에 저장해둔 등록 아이디를 SharedPreferences에서 가져온다.
        String registrationId = prefs.getString(PROPERTY_REG_ID, ""); // 저장해둔 등록 아이디가 없으면 빈 문자열을 반환한다.
        if (registrationId.isEmpty()) {
            System.out.println("************************************************* Registration not found.");
            return "";
        }

        // 앱이 업데이트 되었는지 확인하고, 업데이트 되었다면 기존 등록 아이디를 제거한다.
        // 새로운 버전에서도 기존 등록 아이디가 정상적으로 동작하는지를 보장할 수 없기 때문이다.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) { // 이전에 등록 아이디를 저장한 앱의 버전과 현재 버전을 비교해 버전이 변경되었으면 빈 문자열을 반환한다.
            System.out.println("************************************************* App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(Splash.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // reg id 발급
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // 서버에 발급받은 등록 아이디를 전송한다.
                    // 등록 아이디는 서버에서 앱에 푸쉬 메시지를 전송할 때 사용된다.
                    sendRegistrationIdToBackend();

                    // 등록 아이디를 저장해 등록 아이디를 매번 받지 않도록 한다.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                System.out.println("****************************************************************************** msg : " + msg);
            }

        }.execute(null, null, null);
    }

    // SharedPreferences에 발급받은 등록 아이디를 저장해 등록 아이디를 여러 번 받지 않도록 하는 데 사용
    private void storeRegistrationId(Context context, String regid) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        System.out.println("************************************************* Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    // 등록 아이디를 서버(앱이랑 통신하는 서버)에 전달
    // 서버는 이 등록 아이디를 사용자마다 따로 저장해두었다가 특정 사용자에게 푸쉬 메시지를 전송할 때 사용할 수 도 있음
    private void sendRegistrationIdToBackend() {
        System.out.println("************************************************* 서버에 regid 전달 : " + regid);

        HttpGCM hu = new HttpGCM(context);
        String[] params = {SERVER_URL, "KEY:1234", "REG:" + regid};
        hu.execute(params);
    }

    // 토스트 생성 함수
    public void printToast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }
}

