package com.leadway.remoteportalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slash);
        TimerTask task = new TimerTask() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this,InitialLoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        timer.schedule(task, delay);

    }
}
