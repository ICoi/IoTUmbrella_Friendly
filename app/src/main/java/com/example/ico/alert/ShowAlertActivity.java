package com.example.ico.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ico.iotumbrella_friendly.R;

public class ShowAlertActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alert);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("연결이 끊김!! 우산 두고 어디가는중임?")
                .setTitle("블루투스 연결 끊김!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ShowAlertActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}