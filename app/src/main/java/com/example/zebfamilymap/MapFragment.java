package com.example.zebfamilymap;

import  androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
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
import java.util.Objects;
import java.util.Random;

import Activities.PersonActivity;
import BackendLogic.DataCache;
import model.Event;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
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

        assert mapFragment != null;

        mapFragment.getMapAsync(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        map.setOnMapLoadedCallback(this);

        ArrayList<Event> userEvents = dataCache.getEventArrayList(); //All of the user's events

        Random random = new Random();

        HashMap<String, Float> eventTypeColorMap = new HashMap<String, Float>(); //a map for us to connect the events to colors

        for (Event event : userEvents) { // we're going to add the markers and set their colors based on their type

            LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());

            String eventType = event.getEventType().toLowerCase();

            float color;

            if (eventTypeColorMap.containsKey(eventType)) {

                color = eventTypeColorMap.get(eventType);
            } else {

                float[] colorCode = new float[3];

                colorCode[0] = random.nextFloat() * 360;

                colorCode[1] = random.nextFloat();

                colorCode[2] = 1.0f;
                //manually set the color based on events we KNOW we're going to have

                if (eventType.equalsIgnoreCase("birth")) {

                    color = BitmapDescriptorFactory.HUE_GREEN;
                } else if (eventType.equalsIgnoreCase("marriage")) {

                    color = BitmapDescriptorFactory.HUE_RED;
                } else if (eventType.equalsIgnoreCase("death")) {

                    color = BitmapDescriptorFactory.HUE_ORANGE;
                } else {

                    color = colorCode[0];

                    eventTypeColorMap.put(eventType, color);
                }
            }

            Objects.requireNonNull(map.addMarker(new MarkerOptions() //set the info about our marker
                            .position(eventLocation)
                            .title(eventType + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")")
                            .icon(BitmapDescriptorFactory.defaultMarker(color))))
                    .setTag(event);
            map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
        }

        mapEventText.setOnClickListener(new View.OnClickListener() { // if the user clicks on the text or icon we'll be taken to the person activity
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


                Person person_from_event = dataCache.getPersonByPersonID(mapEvent.getPersonID());

                if (person_from_event != null) { //creating a string for the person the event is connected to

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
                }
                return true;
            }
        });

    }


    @Override
    public void onMapLoaded() {
   //not needed
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) { // get the menu to display

        super.onCreateOptionsMenu(menu, menuInflater);

        if (getActivity() != null && getActivity().getClass() == MainActivity.class) {
            menuInflater.inflate(R.menu.menu, menu);
        }

    }


    //end of class
}