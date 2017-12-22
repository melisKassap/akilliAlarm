package com.example.melis.alarmuygulamasi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Melis on 16.12.2017.
 */

public class BaglantiListesiGecis extends AppCompatActivity {
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    Button btn;
    private StringBuilder recDataString = new StringBuilder();
    Handler h;
    final int handlerState = 0;
    private ConnectedThread mConnectedThread;



    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  //android cihazlara icin 128 bit uzunlugundaki kimlik
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baglanti_gecis);

        Intent newint= getIntent();
        address = newint.getStringExtra(BaglantiListesi.EXTRA_ADDRESS);

        new BTbaglan().execute();




        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);


                    if (recDataString.charAt(0) == '3')
                    //arduinodan gelen değer 3 ise larm çaldırma ekranını çağırıyoruz
                    {
                        Intent in = new Intent(BaglantiListesiGecis.this, AlarmSound.class);

                        startActivity(in);
                    }
                    recDataString.delete(0, recDataString.length());
                }
            }};





        findViewById(R.id.bitir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //alarm sonlardır butonuna tıkladığımıza arduino ya 0 değeri gönderiyor
                    btSocket.getOutputStream().write("0".toString().getBytes());
                    Intent i = new Intent(BaglantiListesiGecis.this, AdminPaneli.class);
                    startActivity(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });




    }




    private class BTbaglan extends AsyncTask<Void,Void,Void> {
        private boolean ConnectSuccess = true;
        @Override
        protected void onPreExecute(){
            //bluetooth bağlantısı
            progress = ProgressDialog.show(BaglantiListesiGecis.this,"Baglanıyor...","Lütfen Bekleyin");
        }

        @Override
        protected Void doInBackground(Void...devices){
            try {
                //bluetooth bağlantısı yapılırken sorgular arka planda dönüyor
                if(btSocket == null || !isBtConnected){
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice cihaz = myBluetooth.getRemoteDevice(address);
                    btSocket = cihaz.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e){
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(!ConnectSuccess){
                msg("Baglantı Hatası, Lütfen Tekrar Deneyin");
                finish();
            } else {
                msg("Baglantı Basarılı");
                isBtConnected = true;
                if(btSocket != null){
                    try{
                        btSocket.getOutputStream().write("1".toString().getBytes());
                        mConnectedThread = new ConnectedThread(btSocket);
                        mConnectedThread.start();
                    }catch (IOException e){
                        msg("Error");
                    }
                }
            }
            progress.dismiss();
        }
    }
    //Hata mesajı
    private void msg(String s){
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;

        //thread kısmı oluşturuluyor
        //arduinodan veri alınıyor

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;


            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);

                    h.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

    }

}


