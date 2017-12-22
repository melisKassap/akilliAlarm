package com.example.melis.alarmuygulamasi;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Melis on 14.12.2017.
 */

public class Alarm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmlayout2);



    findViewById(R.id.cikis).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ActivityCompat.finishAffinity(Alarm.this);
        }
    });


}
}
