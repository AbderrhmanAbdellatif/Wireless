package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothDevice;
import java.util.Set;
public class Activity2 extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT=0 ,REQUEST_DISCOVER_BT=0;
    //private static final int REQUEST_DISABLE_BT=0;
    TextView mStatusBlueTv, mPairedTv;
    Button button;
    Button mPairedBtn,mDiscoverBtn;
    BluetoothAdapter bluetoothAdapter;

    TextView btstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        btstatus = findViewById(R.id.status);
        mPairedTv     = findViewById(R.id.pairedTv);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            btstatus.setText("cannot connect Bluetooth is not available");
        }else{
            btstatus.setText("Bluetooth is available");
        }
        mPairedBtn = (Button) findViewById(R.id.mPairedBtnid);
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
        mDiscoverBtn = findViewById(R.id.available);
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isDiscovering()){
                    showToast("Making Your Device Discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });
        //get paired devices btn click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isEnabled()){
                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices){
                        mPairedTv.append("\nDevice: " + device.getName()+ ", " + device);
                    }
                }
                else {
                    //bluetooth is off so can't get paired devices
                    showToast("Turn on bluetooth to get paired devices");
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
