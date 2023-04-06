package com.example.zebfamilymap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import BackendLogic.DataCache;
import model.Event;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;

    public static DataCache dataCache = DataCache.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_maps, container, false);

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

      //create a LatLng object for each event in userEvents and then add a marker to the map for each event
        for (Event event : userEvents) {
            LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
            String eventType = event.getEventType().toLowerCase(); // convert to lowercase
            float color = BitmapDescriptorFactory.HUE_BLUE; // default color
            switch (eventType) {
                case "birth":
                    color = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case "marriage":
                    color = BitmapDescriptorFactory.HUE_RED;
                    break;
                case "death":
                    color = BitmapDescriptorFactory.HUE_ORANGE;
                    break;
            }
            map.addMarker(new MarkerOptions()
                    .position(eventLocation)
                    .title(eventType + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")")
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));
            map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
        }







        //Similar to what you have above
        //grab from the datacache all of your events
        //use the add markers to put all of the events into here.
        //Colors for the events,
        //in the datacache, write a function that goes through the events
        //and color coordinates them, array called event colors.
        //add them to
        //updateCache method
        //right when you log in, everything gets color coordinated.
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
    public void onCreateOptionsMenu( Menu menu, MenuInflater menuInflater){

        super.onCreateOptionsMenu(menu, menuInflater);

        if(getActivity()!=null && getActivity().getClass() == MainActivity.class){
            menuInflater.inflate(R.menu.menu, menu);
        }

    }
}