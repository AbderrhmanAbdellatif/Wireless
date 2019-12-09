package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Activity3 extends AppCompatActivity {

    Button but1, but2, showDevices;
    private WifiManager wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        but1 = findViewById(R.id.on);
        but2 = findViewById(R.id.off);
        showDevices = findViewById(R.id.showdevices);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifi.isWifiEnabled() == true){
                    showToast("wifi already on..." );
                }else{
                    showToast("Turning on wifi ..." );
                    wifi.setWifiEnabled(true);
                }
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifi.isWifiEnabled() == false){
                    showToast("wifi already off ..." );
                }else{
                    showToast("Turning off wifi ..." );
                    wifi.setWifiEnabled(false);
                }            }
        });
    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
