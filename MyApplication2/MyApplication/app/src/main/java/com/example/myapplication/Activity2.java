package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT=0;
    //private static final int REQUEST_DISABLE_BT=0;

    Button button;
    BluetoothAdapter bluetoothAdapter;

    TextView btstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        btstatus = findViewById(R.id.status);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            btstatus.setText("cannot connect Bluetooth is not available");
        }else{
            btstatus.setText("Bluetooth is available");
        }

        button = (Button) findViewById(R.id.onoff);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bluetoothAdapter.isEnabled()){
                    showToast("Turning on Bluetooth ..." );
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                if(bluetoothAdapter.isEnabled()){
                    showToast("Turning off Bluetooth ..." );
                    bluetoothAdapter.disable();
                }
            }
        });
    }

    /*public void enableDisableBT(){
        if(!bluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        }
        if(bluetoothAdapter.isEnabled()){}
    }*/
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
