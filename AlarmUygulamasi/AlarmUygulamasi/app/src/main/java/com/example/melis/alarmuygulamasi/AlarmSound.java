package com.example.melis.alarmuygulamasi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Melis on 14.12.2017.
 */

public class AlarmSound extends AppCompatActivity {
    private MediaPlayer player;
    final Context context=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_layout);

        Button stop =(Button)findViewById(R.id.alarm);
        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                player.stop();


                Intent i = new Intent(context, Alarm.class);
                startActivity(i);
                return false;
            }
        });
        play(this, getAlarmSound());
    }



    private void play(Context context, Uri alert){
        //cihazın alarm zil sesini çekiyor, zil sesi tanımlanmamışsa random atıyor
        player = new MediaPlayer();
        try{
            player.setDataSource(context, alert);
            final AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if(audio.getStreamVolume(AudioManager.STREAM_ALARM) !=0){
                player.setAudioStreamType(AudioManager.STREAM_ALARM);
                player.prepare();
                player.start();

            }

        }catch (IOException e){
            Log.e("Error....","Check code..");
        }
    }

    private  Uri getAlarmSound(){ // alarm sesi icin

        Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alertSound == null){
            alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(alertSound == null){
                alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return  alertSound;
    }


}
