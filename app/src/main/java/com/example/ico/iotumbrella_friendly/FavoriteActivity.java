package com.example.ico.iotumbrella_friendly;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavoriteActivity extends Activity {
    static final LatLng SEOUL = new LatLng(37.56, 126.97);
    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

    private AsyncHttpClient mHttpClient;
    private RequestParams requestParams;


    private GoogleMap map;
    private MarkerOptions markerOptions;
    private Marker seoul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.googlemap)).getMap();

        if(map != null){
            markerOptions = new MarkerOptions().position(SEOUL)
                    .title("SEOUL")
                    .snippet("Seoul is love")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.a_btn_favorite))
                    .draggable(true);
            seoul = map.addMarker(markerOptions);

        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        /*
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                drawMarker(point);
            }
        });
        */
        map.setOnMarkerDragListener(new OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng pos = marker.getPosition();
                seoul.setSnippet(pos.latitude + "," + pos.longitude);
                Toast.makeText(FavoriteActivity.this, "Pos : " + pos.latitude + " // " + pos.longitude, Toast.LENGTH_SHORT).show();
                marker.showInfoWindow();
            }
        });

        Button btn = (Button)findViewById(R.id.buttonmap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get address
                // ???????? ???? ??릭?? ??겨찾기?? ????
                //c
                // ???? ???? 방??????? ????보???면 ??!

                mHttpClient = new AsyncHttpClient();
                mHttpClient.addHeader("Accept-Encoding","UTF-8");
                RequestParams params = new RequestParams();
                params.put("latlng","36.4534862,127.8819707");
                params.put("sensor","true");
                params.put("language","ko");
                params.put("key","AIzaSyBwVWvFpbuR-1cPmRJCfkFDjdCtWNWLwhY");
                params.put("location_type","ROOFTOP");
                mHttpClient.get(URL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        String resStr = new String(response);
                        try{
                            JSONObject object = new JSONObject(resStr);
                            JSONArray results = new JSONArray(object.getString("results"));
                            JSONObject results0 = results.getJSONObject(0);

                            JSONArray address_com = new JSONArray(results0.getString("address_components"));
                            JSONObject now;
                            for(int j = 0 ; j < address_com.length(); j++){
                                now = address_com.getJSONObject(j);
                                Log.i("ADDRESS",now.getString("long_name"));

                                // TODO android preference로 저장하기1!! 귀차느니.. ㅋㅋㅋ
                                // setting.xml 이라는 파일이 생성됨 - > 여기에 저장되는거임
                                SharedPreferences setting = getSharedPreferences("setting",0);
                                SharedPreferences.Editor editor = setting.edit();
                                editor.putString("test","hahahoootest");
                                editor.commit();
                                Toast.makeText(getApplicationContext(),setting.getString("test","nothing"),Toast.LENGTH_SHORT).show();;//setting.getString("test");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Toast.makeText(getApplicationContext(),"??겨찾?? ???? ?????",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        //map.animateCamera(CameraUpdateFactory.zoomTo(10),2000,null);
    }

}
