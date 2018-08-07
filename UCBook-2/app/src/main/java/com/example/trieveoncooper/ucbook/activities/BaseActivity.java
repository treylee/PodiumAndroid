package com.example.trieveoncooper.ucbook.activities;

/**
 * Created by trieveoncooper on 11/13/17.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.trieveoncooper.ucbook.infrastructure.BookApplication;
import com.squareup.otto.Bus;

public class BaseActivity extends AppCompatActivity {
    protected BookApplication application;
    protected Bus bus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        application = (BookApplication) getApplication();
        bus = application.getBus();
        bus.register(this);
    }

  @Override
    protected void onDestroy(){
      super.onDestroy();
      bus.unregister(this);
  }
}
