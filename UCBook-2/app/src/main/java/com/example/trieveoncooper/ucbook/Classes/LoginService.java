package com.example.trieveoncooper.ucbook.Classes;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by trieveoncooper on 12/5/17.
 */

public class LoginService extends Service  {
    private Timer t;
    private PowerManager.WakeLock wakeLock;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float startX = 0;
    private float startY = 0;
    private float startZ = 0;
    private float x;
    private float y;
    private float z;
    boolean moved = false;
    boolean startValuesSet = false;
    private long first_time_accel;
    boolean moveValueSet = false;
    boolean start = false;
    private final IBinder mBinder = new LocalBinder();
    Handler handler = new Handler();
    TextView label;
    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    Callbacks activity;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public void registerClient(Activity activity){
        this.activity = (Callbacks) activity;
    }
    public class LocalBinder extends Binder {
        public LoginService getServiceInstance(){
            return LoginService.this;
        }
    }

    public interface Callbacks{

        public void updateClient(long data);
    }
    public void registerLabel(TextView t){
        label = t;
    }
    public void startAnimation(){
        label.setText("testing");
    }



}