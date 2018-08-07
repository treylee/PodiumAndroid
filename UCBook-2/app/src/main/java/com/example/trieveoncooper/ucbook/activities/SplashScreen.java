package com.example.trieveoncooper.ucbook.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.trieveoncooper.ucbook.R;

public class SplashScreen extends AppCompatActivity {

    boolean back = false;
    private Runnable task = new Runnable() {
        public void run() {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(task, 5000);
    }
    @Override
    public void onResume(){
        super.onResume();
        if(back) {
            SplashScreen.this.finish();
            System.exit(0);
        }
        back = true;

    }
}