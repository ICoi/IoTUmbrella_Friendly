package com.example.ico.iotumbrella_friendly;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class FavoriteActivity extends Activity {
    static final LatLng INHAUNIV = new LatLng(37.4509881, 126.6544217);
    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

    private AsyncHttpClient mHttpClient;
    private RequestParams requestParams;

    private static final String URLFEONA = "http://www.feona.kr:9000/location";
    private GoogleMap map;
    private MarkerOptions markerOptions;
    private Marker seoul;

    private String nowLat;
    private String nowLon;
    private TextView placeTV;

    private String add_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //setting.getString("lostPlace", "nothing")
        SharedPreferences setting = getSharedPreferences("setting",0);
        placeTV = (TextView)findViewById(R.id.tv_favor_place);
        placeTV.setText(setting.getString("favoRegionAddress","저장된 위치가 없습니다."));
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.googlemap)).getMap();

       // 여기에 favo 처음 위치 설정으로 바꾸기!!
        nowLat = Double.toString(INHAUNIV.latitude);
        nowLon = Double.toString(INHAUNIV.longitude);
        if(map != null){
            markerOptions = new MarkerOptions().position(INHAUNIV)
                    .title("Favorite Place")
                    .snippet("이곳의 날씨정보를 받아봅니다.")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.a_btn_favorite))
                    .draggable(true);
            seoul = map.addMarker(markerOptions);

        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(INHAUNIV, 15));
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
                nowLat = Double.toString(pos.latitude);
                nowLon = Double.toString(pos.longitude);
          //      seoul.setSnippet(pos.latitude + "," + pos.longitude);
    //            Toast.makeText(FavoriteActivity.this, "Pos : " + pos.latitude + " // " + pos.longitude, Toast.LENGTH_SHORT).show();
                marker.showInfoWindow();
            }
        });

        Button btn = (Button)findViewById(R.id.buttonmap);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHttpClient = new AsyncHttpClient();
                mHttpClient.addHeader("Accept-Encoding","UTF-8");
                RequestParams params = new RequestParams();
                params.put("latlng",nowLat + ","+nowLon);
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

                            add_address =results0.getString("formatted_address");
                            JSONObject now;
                   //         for(int j = 0 ; j < address_com.length(); j++){
                    //            now = address_com.getJSONObject(j);
                                Log.i("ADDRESS", add_address);

                                AsyncHttpClient newHttpClient = new AsyncHttpClient();
                                RequestParams newRequestParams = new RequestParams();
                                JSONObject postObject = new JSONObject();
                                postObject.put("region", add_address);

                           // postObject.put("Content-Type", "text/json");
                            newHttpClient.addHeader("Content-Type","text/json");
                            newHttpClient.addHeader("Accept-Encoding","UTF-8");
                            try {
                                StringEntity entity = new StringEntity(postObject.toString());
                                // newHttpClient.get(URLFEONA, ) asdfads 여기서 전송 보내야되!! 우리 서버로!! 그거 코드 받아오는거 구현해야됨 ㅎㅎ
                                newHttpClient.post(getApplicationContext(), URLFEONA, entity, "text/json", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        Log.i("RequestFeona","SUCCESS");
                                        String newResStr = new String(bytes);
                                        try {
                                            JSONObject job = new JSONObject(newResStr);
                                            JSONObject msgOB = job.getJSONObject("msg");
                                            // 마지막으로 신호 끊긴 위치! 저장
                                            SharedPreferences setting = getSharedPreferences("setting",0);
                                            SharedPreferences.Editor editor = setting.edit();
                                            editor.putString("favoRegionCode",msgOB.getString("code"));
                                            editor.putString("favoRegionAddress",add_address);
                                            editor.commit();
                                            placeTV.setText(add_address);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                             Log.i("RequestFeona","FAIL");

                                    }
                                });
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
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
