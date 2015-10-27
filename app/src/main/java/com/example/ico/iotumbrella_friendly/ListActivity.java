package com.example.ico.iotumbrella_friendly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setOnClickListener();
    }



    // Page Move Button Click
    public void setOnClickListener(){
        ImageButton bluetoothBtn = (ImageButton)findViewById(R.id.imgbtn_main_bluetooth);
        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mapBtn = (ImageButton)findViewById(R.id.imgbtn_main_map);
        mapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ListActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });
    }

}
