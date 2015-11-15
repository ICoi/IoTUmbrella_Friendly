package com.example.ico.iotumbrella_friendly;

import android.app.Activity;
import android.os.Bundle;
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

public class FavoriteActivity extends Activity {
    static final LatLng SEOUL = new LatLng(37.56, 126.97);
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
                // 추가하기 버튼 클릭시 즐겨찾기로 등록
                //https://maps.googleapis.com/maps/api/geocode/json?latlng=36.4534862,127.8819707&sensor=true&language=ko&key=AIzaSyBwVWvFpbuR-1cPmRJCfkFDjdCtWNWLwhY&location_type=ROOFTOP
                // 위와 같은 방식으로 요청보내면 됨!

            }
        });
        //map.animateCamera(CameraUpdateFactory.zoomTo(10),2000,null);
    }

}
