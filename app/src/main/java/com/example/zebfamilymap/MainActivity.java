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

        Fragment fragment = fragmentManager.findFragmentById(R.id.mainActivity);

        //put this into an if to check to see if we need to go into the map fragment.
        fragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .add(R.id.mainActivity, fragment)
                .commit();

    }


    //end of main activity
}