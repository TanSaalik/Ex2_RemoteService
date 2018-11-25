package com.example.tan.ex2_remoteservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button btnChangeColor, btnSendMessage;
    Messenger mService = null;
    boolean isBound;
    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            .format(Calendar.getInstance().getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), RemoteService.class);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection =
            new ServiceConnection() {
                public void onServiceConnected(ComponentName className, IBinder service) {
                    mService = new Messenger(service);
                    isBound = true;
                }

                public void onServiceDisconnected(ComponentName className) {
                    mService = null;
                    isBound = false;
                }
            };

    public void sendMessage()
    {
        if (!isBound){
            return;
        }
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("KeyString", date);
        msg.setData(bundle);

        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getRandomColor(){
        Random random = new Random();
        return Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));
    }

    public void setBGColor() {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getRandomColor());
    }

    public void onChangeColor(View view) {
        setBGColor();
    }

    public void onSendMessage(View view) {
        new Handler().postDelayed(new Runnable() {
            public void run(){
                sendMessage();
            }
        }, 5000);
    }
}
