package com.example.trieveoncooper.ucbook.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.trieveoncooper.ucbook.Classes.LoginService;
import com.example.trieveoncooper.ucbook.Classes.User;
import com.example.trieveoncooper.ucbook.R;
import com.example.trieveoncooper.ucbook.live.BaseLiveService;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.trieveoncooper.ucbook.live.BaseLiveService.FIRE_BASE_REFERENCE;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.currentUser;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.userSet;

public class Login extends AppCompatActivity implements LoginService.Callbacks {
    private FirebaseAuth mAuth;
    TextView label;
    private static boolean checked = false;
    Fragment loginFragment;
    FirebaseDatabase database;
    Firebase reference;
    public boolean isTaken = false;
    CheckBox radioButton;
    private VideoView mVideoView;
    public int count = 0;
    int fadeCount = 0;
    Handler handler;
    TextView mSwitcher;
    Animation in;
    Animation out;
    Intent mServiceIntent;
     LoginService myService;
    Intent serviceIntent;
   public boolean servicebound = false;
    public ArrayList<String> labelmsgs = new ArrayList<>();
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            servicebound = true;
            Log.d("a","service connected");
            Toast.makeText(Login.this, "onServiceConnected called", Toast.LENGTH_SHORT).show();
            // We've binded to LocalService, cast the IBinder and get LocalService instance
            LoginService.LocalBinder binder = (LoginService.LocalBinder) service;
            myService = binder.getServiceInstance(); //Get instance of your service!
            myService.registerClient(Login.this); //Activity register in the service as client for callabcks!
            myService.registerLabel(label);
            myService.startAnimation();


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("a","service disconnected");
            servicebound = false;
            Toast.makeText(Login.this, "onServiceDisconnected called", Toast.LENGTH_SHORT).show();
            // tvServiceState.setText("Service disconnected");
        }
    };

    @Override
    public void updateClient(long millis) {
        if(count < 6) {
            label = findViewById(R.id.labelMsg);
            label.setText(labelmsgs.get(count));
            count++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        BaseLiveService b = new BaseLiveService();
        labelmsgs.add("Connect");
        labelmsgs.add("Buy");
        labelmsgs.add("Sell");
        labelmsgs.add("Match");
        labelmsgs.add("Network");
        labelmsgs.add("BookxChange");
        //startTimer();
        fadeCount = 0;
        label = findViewById(R.id.label);



        Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView emailField = (TextView) findViewById(R.id.emailBox);
        final TextView passwordField = (TextView) findViewById(R.id.passwordBox);
        Button googleButton = (Button) findViewById(R.id.googleButton);
        Button facebookButton = (Button) findViewById(R.id.fbLoginButton);
        Button fingerButton = (Button) findViewById(R.id.fingerButton);
        database = FirebaseDatabase.getInstance();
        reference = new Firebase(FIRE_BASE_REFERENCE);
        radioButton = findViewById(R.id.radioButton);
        mVideoView = (VideoView) findViewById(R.id.bgVideoView);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sfsunrise);
        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(false);
            }
        });
        radioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checked = !checked;
                radioButton.setChecked(checked);
            }
        });
        fingerButton.setVisibility(View.INVISIBLE);
        googleButton.setVisibility(View.INVISIBLE);
        googleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(Login.this, GoogleSignInActivity.class);
                Login.this.startActivity(activityChangeIntent);
            }

        });
        fingerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(Login.this, FingerAuthActivity.class);
                Login.this.startActivity(activityChangeIntent);
            }

        });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(Login.this, FaceBookSignInActivity.class);
                Login.this.startActivity(activityChangeIntent);
            }

        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (emailField.getText() != null && passwordField.getText() != null) {

                    String email = emailField.getText().toString();
                    String password = passwordField.getText().toString();
                    String revisedEmail = email.replaceAll("[.]", "DOT");
                    revisedEmail = revisedEmail.toString().trim().toUpperCase();

                    Log.d("a", "USERNAME" + revisedEmail);

                    doesNameExist(revisedEmail, email, password);


                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        serviceIntent = new Intent(Login.this, LoginService.class);
        startService(new Intent(this, LoginService.class));
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE); //Binding to the service!

    }
    @Override
    public void onResume() {
        super.onResume();
        // Starts the service, so that the service will only stop when explicitly stopped.
        //  Intent intent = new Intent(this, MyService.class);
        serviceIntent = new Intent(Login.this, LoginService.class);
        if(servicebound = false) {
            startService(new Intent(this, LoginService.class));
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE); //Binding to the service!
            // This registers mMessageReceiver to receive messages.
        }

    }

    public void startTimer(){
      Timer  t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                    if(count < 6) {
                        TextView msg = findViewById(R.id.labelMsg);
                        msg.setText(labelmsgs.get(count));
                        count++;
                    }
            }
        }, 5000, 8000);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    public boolean doesNameExist(final String sUsername,final String email, final String password)
    {
        Log.d("a","THE USER EXIST OR NAH "+sUsername);
        Firebase ref = reference.child("taken");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(sUsername))
                {
                    isTaken = true;
                   CheckBox radioButtn = findViewById(R.id.radioButton);

                    if(radioButtn.isChecked()) {
                        signIn(email,password);
                    }else{
                        Toast.makeText(Login.this, "This user exist try signing in", Toast.LENGTH_SHORT).show();

                    }

                }
                else if (!dataSnapshot.hasChild(sUsername))
                {
                    isTaken = false;
                    Log.d("a","THE USER EXIST NAH ");
                    CheckBox radioButtn = findViewById(R.id.radioButton);

                    if(!radioButtn.isChecked()) {
                        createUser(email,password);
                    }else{
                        Toast.makeText(Login.this, "Register by unchecking the radio button", Toast.LENGTH_SHORT).show();

                    }

                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(Login.this, "Connection Error. Please try again in some time.", Toast.LENGTH_SHORT).show();
            }

        });
        Log.d("a","VALUE"+isTaken);

        return isTaken;
    }
    public void createUser(final String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("a", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Firebase ref = reference.child("data").child("users").child(user.getUid());

                            User user1 = new User(user.getEmail());
                            user1.setName(user.getEmail());

                            ref.child("user").setValue(user1);
                            String revisedEmail = email.replaceAll("[.]","DOT");
                            revisedEmail = revisedEmail.toString().trim().toUpperCase();
                            reference.child("taken").child(revisedEmail).setValue(true);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("sa", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             Log.d("a", "signInWithEmail:success");
                             setCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("a", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void setCurrentUser() {
        Firebase df = new Firebase(FIRE_BASE_REFERENCE);
        final FirebaseUser user = mAuth.getCurrentUser();
        Log.d("a", "THE UID OF THE CURRENT USER" + user.getUid());

        df.child("data").child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapShot : dataSnapshot.getChildren()) {
                        User user = dataSnapShot.getValue(User.class);
                        currentUser = user;
                        userSet = true;
                        if(currentUser.isFirstLogin()){
                            Intent activityChangeIntent = new Intent(Login.this, FLoginActivity.class);
                            startActivity(activityChangeIntent);
                        }else {
                            Intent activityChangeIntent = new Intent(Login.this, Hub.class);
                            startActivity(activityChangeIntent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
