package com.example.administrator.face.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.face.R;


public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        Thread time=new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    Intent i=new Intent();
                    i.setClass(ThreadActivity.this,StartActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        time.start();
    }
}
