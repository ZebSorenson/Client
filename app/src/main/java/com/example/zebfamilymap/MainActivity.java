package com.example.zebfamilymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import Logic.DataCache;



public class MainActivity extends AppCompatActivity {

    DataCache myCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

       // Fragment fragment = fragmentManager.findFragmentById(R.id.activity)
        //  android:id="@/mainactivity"


    }


    //end of main activity
}