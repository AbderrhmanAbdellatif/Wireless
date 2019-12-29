package com.example.wifi_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button wifi , blue;

        wifi = findViewById(R.id.wifi);
        blue = findViewById(R.id.bluetooth);

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void openActivity2(){
        Intent intent = new Intent(this, mainBluetoothActiviy.class);
        startActivity(intent);
    }
}
