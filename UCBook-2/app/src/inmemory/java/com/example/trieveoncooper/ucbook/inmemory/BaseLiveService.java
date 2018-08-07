package com.example.trieveoncooper.ucbook.inmemory;

import android.util.Log;

import com.example.trieveoncooper.ucbook.infrastructure.BookApplication;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Bus;

/**
 * Created by trieveoncooper on 11/13/17.
 */

public class BaseLiveService {
    protected Bus bus;
    protected BookApplication application;
    protected final DatabaseReference databaseReference;
    public static final String FIRE_BASE_REFERENCE = "https://ucbok-95490.firebaseio.com/";
    public static final String readReference = "https://ucbok-95490.firebaseio.com/users";

    public BaseLiveService(BookApplication application){
        Log.d("test","Tsdasdsadas"+FIRE_BASE_REFERENCE);

        this.application = application;
        // bus = application.getBus();
        //bus.register(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public BaseLiveService(){
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
}
