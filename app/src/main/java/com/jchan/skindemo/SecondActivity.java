package com.jchan.skindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("skin","onCreate "+this.getClass().getSimpleName());
        setContentView(R.layout.activity_second);

    }
}