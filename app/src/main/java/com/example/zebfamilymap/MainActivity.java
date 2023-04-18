package com.example.zebfamilymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import BackendLogic.DataCache;
import RequestResult.PersonIDResult;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    DataCache myCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Iconify.with(new FontAwesomeModule());

        PersonIDResult child = myCache.getFirstChildPerson(); //this will give us the logged in person's details

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();

        manager.findFragmentById(R.id.mainActivity);

        Fragment fragment_Design;

        if(child == null){
            fragment_Design = new LoginFragment();

        }
        else{
            fragment_Design = new GoogleMapFragment();
        }
        manager.beginTransaction().add(R.id.mainActivity, fragment_Design).commit();

    }


    //end of main activity
}