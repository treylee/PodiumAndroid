package com.example.trieveoncooper.ucbook.activities;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.trieveoncooper.ucbook.R;
import com.example.trieveoncooper.ucbook.fragments.FirstLoginFragment;
import com.example.trieveoncooper.ucbook.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Hub extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Fragment firstLoginFragment;
    Fragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        Log.d("test", "CHECKING USER"+user.getEmail());

    }
}
