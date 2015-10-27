package com.example.ico.iotumbrella_friendly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // SharedPreferences에 저장 할 때 key 값
    public static final String PROPERTY_REG_ID = "registration_id";

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "ICELANCER";

        private static final String URL = "http://203.128.183.153:9000/gcm/register";

    // My Sender Id - project number
    String SENDER_ID = "147568965374";
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;

    String regid;
    private EditText mDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // TODO - 다음 페이지로 넘어간느 버튼!! 추후에는 자동으로 다음 페이지로 넘어가도록 수정해야함
        Button btn = (Button)findViewById(R.id.btn_startbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });




    }

    public void makePushAlertSetting(){

        mHttpClient = new SyncHttpClient();
        context = getApplicationContext();
        mDisplay = (EditText)findViewById(R.id.editText);
        if(checkPlayServices()){
            // 구글 플레이 서비스 확인 로직
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if(regid.isEmpty()){
                registerInBackground();
            }

            EditText et = (EditText)findViewById(R.id.register_id_text);
            et.setText("Regi id : " + regid);
            Log.i("REG_ID", regid);
        }else{
            Log.i(TAG, "No valid Google Play Services APK found");
        }
    }


    @Override
    protected void onResume(){
        super.onResume();;
        checkPlayServices();
    }

    // 구글 플레이 서비스 확인
    private boolean checkPlayServices(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Log.i("ICELANCER", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    /// sharedPreference에 등록 아이디가 저장되어 있는지 확인하고 없으면 빈 문자열을 있으면 기존에등록된 등록 아이디를 반환
    // 또한 앱 버전이 등록 아이디를 발급받은 시점과 달라도 빈 문자열을 반환
    private String getRegistrationId(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");




        if(registrationId.isEmpty()){
            Log.i(TAG, "Registration not found");
            return "";
        }

        // 앱이 업데이트 되었는지 확인하고, 업데이트 되었다면 기존 등록 아이디를 제거한다.
        // 새로운 버전에서도 기존 등록 아이디가 정상적으로 동작하는지를 보장할 수 없기 때문이다
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion){
            Log.i(TAG, "App version changed");
            return "";
        }
        return registrationId;
    }
    private SharedPreferences getGCMPreferences(Context context){
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context){
        try{
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        }catch(PackageManager.NameNotFoundException e){
            // should never happen
            throw new RuntimeException("Could not get package name: "+ e);
        }
    }

    // 아이디가 없거나 앱 버전이 변경되면 registerInBackground메서드 호출
    private void registerInBackground(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params){
                String msg = "";
                try{
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    //등록 완료
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("REG_ID",regid);

                    // 서버에 발급받은 등록 아이디를 전송한다.
                    // 등록 아이디는 서버에서 앱에 푸쉬 메시지를 전송할 때 사용된다.
                    sendRegistrationIdToBackend();

                    // 등록 아이드를 저장해 등록 아이들ㄹ 매번 받지 않도록 한다.
                    storeRegistrationId(context, regid);
                }catch(IOException ex){
                    msg = "Error : " + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg){
                mDisplay.append(msg + "\n");
            }
        }.execute(null,null,null);
    }
    // SharedPreferences에 발급받은 등록 아이디를 저장해 등록 아읻를 여러 번 받지 ㅇ낳게 한다
    private void storeRegistrationId(Context context, String regid){
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regid on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    private AsyncHttpClient mHttpClient;
    private RequestParams requestParams;
    // 등록 아이디를 서드 파티 서버에 전달
    private void sendRegistrationIdToBackend() throws JSONException, UnsupportedEncodingException {
        requestParams = new RequestParams();
        JSONObject postBodyMsg = new JSONObject();
        postBodyMsg.put("name","Daun1233");
        postBodyMsg.put("regId",regid.toString());

        StringEntity entity = new StringEntity(postBodyMsg.toString());
        requestParams.put("Content-Type","text/json");
        requestParams.put("regId", regid.toString());
        Log.i("DAUN", postBodyMsg.toString());

        mHttpClient.post(getApplicationContext(), URL, entity, "text/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("DAUN","success");

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i("DAUN","failure");

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
