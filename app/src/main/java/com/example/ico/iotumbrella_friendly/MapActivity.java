package com.example.ico.iotumbrella_friendly;

//http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
//http://webnautes.tistory.com/825
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MapActivity extends Activity {
    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";
    static final LatLng INHAUNIV = new LatLng(37.4509881, 126.6544217);

    private AsyncHttpClient mHttpClient;
    private RequestParams requestParams;

    private static final String URLFEONA = "http://www.feona.kr:9000/location";
    private GoogleMap map;
    private MarkerOptions markerOptions;
    private Marker seoul;

    private String nowLat;
    private String nowLon;
    private TextView placeTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        //setting.getString("lostPlace", "nothing")
        SharedPreferences setting = getSharedPreferences("setting",0);
        nowLat = setting.getString("lostLat", "0");
        nowLon = setting.getString("lostLon", "0");
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng nowLL;
        if(nowLat.equals("0")){
            // 저장된거가 없는 경우
            nowLL = INHAUNIV;
            Toast.makeText(getApplicationContext(),"최근 접속 기록이 없습니다.",Toast.LENGTH_SHORT).show();
        }else {
          nowLL  =new LatLng(Double.parseDouble(nowLat), Double.parseDouble(nowLon));
            if(map != null){
                markerOptions = new MarkerOptions().position(nowLL)
                        .title("SEOUL")
                        .snippet("Seoul is love")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.a_btn_favorite))
                        .draggable(false);
                seoul = map.addMarker(markerOptions);

            }

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLL, 15));
        }


    }
}
