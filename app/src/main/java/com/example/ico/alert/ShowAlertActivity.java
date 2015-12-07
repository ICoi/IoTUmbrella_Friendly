package com.example.ico.alert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ico.iotumbrella_friendly.R;

public class ShowAlertActivity extends Activity {
/*
    private TextView latituteField;
    private TextView longitudeField;
    private TextView choose;
    private CheckBox choice;
    private Button button;

    */
    private LocationManager locationManager;
    private String provider;
    Criteria criteria;
    private MyLocationListener mylistener;
    @SuppressLint("InlinedApi")
    @SuppressWarnings("deprecation")
    public static int getLocationMode(Context context) {
        int locationMode = Settings.Secure.LOCATION_MODE_OFF;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = Settings.Secure.LOCATION_MODE_OFF;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER)
                    && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
            } else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
            }
        }

        return locationMode;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alert);
/*
        latituteField = (TextView) findViewById(R.id.Latitude);
        longitudeField = (TextView) findViewById(R.id.Longitute);
        choose = (TextView) findViewById(R.id.choice);
        choice = (CheckBox) findViewById(R.id.checkBox);
        button = (Button) findViewById(R.id.button);
*/
/*
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub

                if (choice.isChecked()) {

                    criteria.setAccuracy(Criteria.ACCURACY_FINE);

                    choose.setText("fine accuracy selected. " +
                            "WIFIì •ë³´ í™œìš©í•˜ì—¬ ìœ„ì¹˜ ì°¾ê¸°.ë” ì •í™•í•¨");

                } else {

                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);

                    choose.setText("coarse accuracy selected.ê¸°ì§€êµ­ì •ë³´ í™œìš©í•˜ì—¬ ìœ„ì¹˜ ì°¾ê¸°");

                }

            }

        });

*/

//        if (choice.isChecked()) {

 //           criteria.setAccuracy(Criteria.ACCURACY_FINE);
/*
            choose.setText("fine accuracy selected. " +
                    "WIFIì •ë³´ í™œìš©í•˜ì—¬ ìœ„ì¹˜ ì°¾ê¸°.ë” ì •í™•í•¨");

        } else {
*/
 //           criteria.setAccuracy(Criteria.ACCURACY_COARSE);
/*
            choose.setText("coarse accuracy selected.ê¸°ì§€êµ­ì •ë³´ í™œìš©í•˜ì—¬ ìœ„ì¹˜ ì°¾ê¸°");

        }
        */
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        // location provider ê²°ì •ë°©ë²•ì„ ì •í•œë‹¤.
        criteria = new Criteria();

        //ìœ„ì¹˜ ì •í™•ë„ ì„¤ì •
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        //ë°©ìœ„ê° ì •ë³´ ì„¤ì •
        criteria.setBearingRequired(true);

        //ì†Œëª¨ ì „ì› ì„¤ì •
        criteria.setPowerRequirement(Criteria.POWER_LOW);


        criteria.setCostAllowed(true);

        //ìµœì„ ì˜ providerí•˜ë‚˜ë§Œ ë¦¬í„´ë°›ëŠ”ë‹¤...
        provider = locationManager.getBestProvider(criteria, true);


        int mode = getLocationMode(this);

        switch (mode) {
            case android.provider.Settings.Secure.LOCATION_MODE_OFF:
      //          Toast.makeText(ShowAlertActivity.this, "LOCATION_MODE_OFF",
       //                 Toast.LENGTH_SHORT).show();
                break;
            case android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY:
      //          Toast.makeText(ShowAlertActivity.this,
      //                  "LOCATION_MODE_SENSORS_ONLY = GPS_PROVIDER",
       //                 Toast.LENGTH_SHORT).show();
                break;
            case android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING:
      //          Toast.makeText(ShowAlertActivity.this,
      //                  "LOCATION_MODE_BATTERY_SAVING = NETWORK_PROVIDER",
        //                Toast.LENGTH_SHORT).show();
                break;
            case android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY:
        //        Toast.makeText(ShowAlertActivity.this,
       //                 "LOCATION_MODE_HIGH_ACCURACY = GPS_PROVIDER+NETWORK_PROVIDER",
        //                Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }


  //      Toast.makeText(ShowAlertActivity.this, provider + "providerê°€ ì„ íƒë˜ì—ˆìŒ.",
    //            Toast.LENGTH_SHORT).show();


        //providerì˜ last known location
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }


        mylistener = new MyLocationListener();

        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            // last known locationì´ ì—†ëŠ” ê²½ìš° ìœ„ì¹˜ ì„¤ì •ì„ í•˜ë„ë¡í•œë‹¤.
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("연결이 끊김!! 우산 두고 어디가는중임?")
                .setTitle("블루투스 연결 끊김!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();




    }




    //ì•¡í‹°ë¹„í‹°ê°€ ì¼ì‹œì •ì§€(Paused) ìƒíƒœì—ì„œ ë³µê·€ë  ë•Œ í˜¸ì¶œëœë‹¤.
    //locationlistenerì—…ë°ì´íŠ¸ë¥¼ ì¶”ê°€í•œë‹¤.
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(ShowAlertActivity.this, "onResume",
                Toast.LENGTH_SHORT).show();

        //ìµœì„ ì˜ providerí•˜ë‚˜ë§Œ ë¦¬í„´ë°›ëŠ”ë‹¤...
        provider = locationManager.getBestProvider(criteria, true);
        Toast.makeText(ShowAlertActivity.this, provider + "providerê°€ ì„ íƒë˜ì—ˆìŒ.",
                Toast.LENGTH_SHORT).show();


        //  ìµœì†Œ 1ë¯¸í„° ì´ë™í•˜ê³  200ë°€ë¦¬ì„¸ì»¨ë“œ ì§€ë‚˜ë©´ ìœ„ì¹˜ ì—…ë°ì´íŠ¸..
        try {
            locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }


    }

    //ì•¡í‹°ë¹„í‹°ê°€ ì¼ì‹œì •ì§€ ìƒíƒœê°€ ë ë•Œ locationlistenerì—…ë°ì´íŠ¸ë¥¼ ì œê±°í•œë‹¤.
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(ShowAlertActivity.this, "onPause",
                Toast.LENGTH_SHORT).show();

        try {
            locationManager.removeUpdates(mylistener);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }

    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {


         //   latituteField.setText("Latitude: "
         //           + String.valueOf(location.getLatitude()));
         //   longitudeField.setText("Longitude: "
         //           + String.valueOf(location.getLongitude()));

            // 마지막으로 신호 끊긴 위치! 저장
            SharedPreferences setting = getSharedPreferences("setting",0);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("lostLat",Double.toString(location.getLatitude()));
            editor.putString("lostLon",Double.toString(location.getLongitude()));
            editor.commit();
         //   Toast.makeText(getApplicationContext(), setting.getString("lostPlace", "nothing"), Toast.LENGTH_SHORT).show();;//setting.getString("test");
            ShowAlertActivity.this.finish();

            Toast.makeText(ShowAlertActivity.this, "Lat : " + location.getLatitude() +" Lon : " + location.getLongitude() ,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Toast.makeText(ShowAlertActivity.this, provider + " state visible",
                            Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Toast.makeText(ShowAlertActivity.this, provider + " out of service",
                            Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(ShowAlertActivity.this, provider + " service stop",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(ShowAlertActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(ShowAlertActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
}
