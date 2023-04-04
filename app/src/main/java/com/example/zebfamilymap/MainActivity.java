package com.example.zebfamilymap;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import Logic.DataCache;
import RequestResult.PersonIDResult;


public class MainActivity extends AppCompatActivity {

    DataCache myCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //handler is used to know when to switch


        PersonIDResult child = myCache.getFirstChildPerson();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment_Design = fragmentManager.findFragmentById(R.id.mainActivity);

        if(child == null){
            fragment_Design = new LoginFragment();

        }
        else{
            fragment_Design = new MapsFragment();
        }
        fragmentManager.beginTransaction().add(R.id.mainActivity, fragment_Design).commit();

    }


    //end of main activity
}