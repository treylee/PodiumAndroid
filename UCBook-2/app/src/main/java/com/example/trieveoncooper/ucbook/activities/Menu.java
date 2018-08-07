package com.example.trieveoncooper.ucbook.activities;

import android.support.v7.app.AppCompatActivity;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.trieveoncooper.ucbook.Classes.ImageChanger;
import com.example.trieveoncooper.ucbook.R;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ImageChanger adapter = new ImageChanger(this);
        viewPager.setAdapter(adapter);
    }
}
