package com.example.showbluthooth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button listen,send,listdevices;
    ListView listView;
    TextView msg_box,status;
    EditText writeMsg;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice [] devices;
    SendReceive sendReceive1;
    static final int STATE_LISTENING=1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;
    int REQUEST_ENABLE_BLUTOOTH=1;
    private static final String APP_NAME="Bluetooth";
    private static final UUID MY_UUID=UUID.fromString("6f0a1e08-e914-4dbc-a411-5eeba477ebd0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listen =(Button)findViewById(R.id.bt_listen);
        send=(Button)findViewById(R.id.sendButton);
        listView=(ListView)findViewById(R.id.peerListView);
        msg_box=(TextView)findViewById(R.id.readMsg);
        status=(TextView)findViewById(R.id.Status);
        writeMsg=(EditText)findViewById(R.id.writeMsg);
        listdevices=(Button)findViewById(R.id.list_dev);
       // if(!bluetoothAdapter.isEnabled()){
            Intent enbleinstant=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enbleinstant,REQUEST_ENABLE_BLUTOOTH);
        //}
        implementListeners();
    }
    private class Serverclass extends  Thread{
        private BluetoothServerSocket serverSocket;
        public Serverclass(){
           try {
               serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord( APP_NAME, MY_UUID );
           }catch (IOException e){
               e.printStackTrace();
            }
        }
        public void run(){
            BluetoothSocket bluetoothSocket =null;
            while (bluetoothSocket==null){
                try{
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);
                    bluetoothSocket=serverSocket.accept();
                }catch (IOException e){
                    e.printStackTrace();
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }
                if(bluetoothSocket!=null){
                    Message message=Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendReceive1=new SendReceive(bluetoothSocket);
                    sendReceive1.start();
                    break;
                }
            }

        }

    }
    private void implementListeners() {
        listdevices.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                String [] strings=new String[bt.size()];
                devices=new BluetoothDevice[bt.size()];
                int index=0;
                if(bt.size() > 0 ){
                    for (BluetoothDevice device:bt){
                        devices[index]=device;
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        } );

        listen.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Serverclass serverclass=new Serverclass();
                serverclass.start();
            }
        } );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClientClass clientClass=new ClientClass(devices[position]);
                clientClass.start();
                 status.setText("   Connecting  ");

            }
        } );
        send.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
               String s=String.valueOf(writeMsg.getText());
               sendReceive1.write(s.getBytes());
            }
        } );
    }
    private class ClientClass extends Thread {
        private BluetoothDevice bluetoothDevice;
        private BluetoothSocket bluetoothSocket;

        public ClientClass(BluetoothDevice bluetoothDevice1) {
            this.bluetoothDevice = bluetoothDevice1;
            try {
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord( MY_UUID );
            } catch (IOException e) {
                e.printStackTrace( );

            }
        }
        public  void run(){
            try {
                bluetoothSocket.connect();
                Message message=Message.obtain();
                message.what=STATE_CONNECTING;
                handler.sendMessage(message);
                sendReceive1=new SendReceive(bluetoothSocket);
                sendReceive1.start();

            }catch (IOException e){
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

            }

        }
    }
        private  class SendReceive extends Thread{
            private  final BluetoothSocket bluetoothSocket;
            private  InputStream inputStream;
            private  OutputStream outputStream;
            public SendReceive(BluetoothSocket bluetoothSocket1){
                this.bluetoothSocket=bluetoothSocket1;
                InputStream inputStream1=null;
                OutputStream outputStream1=null;
                try{
                    inputStream=bluetoothSocket.getInputStream();
                    outputStream=bluetoothSocket.getOutputStream();
                }catch (IOException e){
                   e.printStackTrace();
                }
                inputStream=inputStream1;
                outputStream=outputStream1;

            }
             public void run(){
                byte [] buffer=new byte[1024];
                int bytes;
                while (true){
                    try {
                        bytes= inputStream.read( buffer );
                        handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();

                    }catch (IOException e){
                        e.printStackTrace();

                    }
                }
             }
             public void write (byte [] bytes){
                try{
                    outputStream.write(bytes);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

             }

        }



    Handler handler=new Handler( new Handler.Callback( ) {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case STATE_LISTENING:
                    status.setText("Listening");
                break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("CONNECTION FAILED");
                    break;
                case STATE_MESSAGE_RECEIVED:
                     byte [] reBytes =(byte [] )msg.obj;
                     String tempmag=new String(reBytes,0,msg.arg1);
                     msg_box.setText(tempmag);
                    break;
            }
            return true;
        }
    } );

}
