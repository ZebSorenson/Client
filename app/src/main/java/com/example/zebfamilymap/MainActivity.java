package com.example.zebfamilymap;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import BackendLogic.DataCache;
import RequestResult.PersonIDResult;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;





public class MainActivity extends AppCompatActivity {

    DataCache myCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //handler is used to know when to switch

        Iconify.with(new FontAwesomeModule());




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