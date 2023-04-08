package com.example.zebfamilymap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Activities.PersonActivity;
import BackendLogic.DataCache;
import model.Event;
import model.Person;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;

    public static DataCache dataCache = DataCache.getInstance();

    public Event mapEvent;

    public TextView mapEventText;

    public ImageView mapImageView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_maps, container, false);

        mapEventText = view.findViewById(R.id.mapEventInformation);

        mapImageView = view.findViewById(R.id.mapGenderImage); //maybe come back here. We did some kind of supression

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //

        map = googleMap;
        map.setOnMapLoadedCallback(this);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));

//        LatLng sydney = new LatLng(-34, 151);
//        BitmapDescriptor markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(markerIcon));
//        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));

        ArrayList<Event> userEvents = dataCache.getEventArrayList();
        Random random = new Random();
        HashMap<String, Float> eventTypeColorMap = new HashMap<String, Float>();

        for (Event event : userEvents) {
            LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
            String eventType = event.getEventType().toLowerCase();

            float color;
            if (eventTypeColorMap.containsKey(eventType)) {
                color = eventTypeColorMap.get(eventType);
            } else {
                float[] hsv = new float[3];
                hsv[0] = random.nextFloat() * 360;
                hsv[1] = random.nextFloat();
                hsv[2] = 1.0f;
                if (eventType.equalsIgnoreCase("birth")) {
                    color = BitmapDescriptorFactory.HUE_GREEN;
                } else if (eventType.equalsIgnoreCase("marriage")) {
                    color = BitmapDescriptorFactory.HUE_RED;
                } else if (eventType.equalsIgnoreCase("death")) {
                    color = BitmapDescriptorFactory.HUE_ORANGE;
                } else {
                    color = hsv[0];
                    eventTypeColorMap.put(eventType, color);
                }
            }

            map.addMarker(new MarkerOptions()
                            .position(eventLocation)
                            .title(eventType + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")")
                            .icon(BitmapDescriptorFactory.defaultMarker(color)))
                    .setTag(event);
            map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
        }


        //the below code is new

        mapEventText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonActivity.start(getActivity(), mapEvent);
            }
        });

        mapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonActivity.start(getActivity(), mapEvent);
            }
        });






        //the above code is new

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                mapEvent = (Event) marker.getTag();

                assert mapEvent != null;

                map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition())); //center the map to the clicked event


                //get all of the information of the person
                //make a string from the information
                //set the text of the mapEventText.set the text to be the String we've created.
                //check if it is a male, display the male icon, if it is a female, display the female icon.
                //use the font awesome resource

                //Write a function in your DataCache that can take in a personID and return the person object that it's given.

                //create a string and add the name to it (we will add more about the event)

                //All the needed info about the event is already in the event object.

                //set the textview to be equal to that string.

                //based off the person object's gender, we'll set the ImageView = to either  boy or girl icon.

                //return true not false....

                Person person_from_event = dataCache.getPersonByPersonID(mapEvent.getPersonID());

                if (person_from_event != null) {
                    String textString = person_from_event.getFirstName() + " "
                            + person_from_event.getLastName() + ": "
                            + mapEvent.getEventType() + ": " +
                            mapEvent.getCity() + ", " +
                            mapEvent.getCountry() + "( " + mapEvent.getYear() + " )";

                    mapEventText.setText(textString);

                    if (person_from_event.getGender().equalsIgnoreCase("m")) {

                        Drawable maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.byu_blue).sizeDp(35);

                        mapImageView.setImageDrawable(maleIcon);
                    } else if (person_from_event.getGender().equalsIgnoreCase("f")) {

                        Drawable femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.pretty_pink).sizeDp(35);

                        mapImageView.setImageDrawable(femaleIcon);
                    }
                    //possibly put another else case in here for an incorrect gender input?
                }


                return true;
            }
        });

//        public Event mapEvent;
//
//        public TextView mapEventText;
//
//        public ImageView mapImageView;


//end of onMapReady
    }


    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        super.onCreateOptionsMenu(menu, menuInflater);

        if (getActivity() != null && getActivity().getClass() == MainActivity.class) {
            menuInflater.inflate(R.menu.menu, menu);
        }

    }











    //end of class
}