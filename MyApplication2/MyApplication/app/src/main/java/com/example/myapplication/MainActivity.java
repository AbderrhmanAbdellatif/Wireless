package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button blue;
    private Button wifi;

    BluetoothAdapter bluetoothAdapter;
    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        blue = findViewById(R.id.blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
                enableDisableBT();
            }
        });

        wifi = findViewById(R.id.wifi);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
                enableDisableWifi();
            }
        });
    }

    public void openActivity2(){
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }
    public void openActivity3(){
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void enableDisableBT(){
        if(bluetoothAdapter == null){
            showToast("Bluetooth is not available !" );
        }
        if(!bluetoothAdapter.isEnabled()){
            showToast("Turning on Bluetooth ..." );
            bluetoothAdapter.enable();
        }
        if(bluetoothAdapter.isEnabled()){
            showToast("Bluetooth is on..." );
        }
    }

    private void enableDisableWifi(){
        if(!wifiManager.isWifiEnabled()){
            showToast("Turning on wifi ..." );
            wifiManager.setWifiEnabled(true);
        }
        if(wifiManager.isWifiEnabled()){
            showToast("wifi is on ..." );
        }
    }
}
