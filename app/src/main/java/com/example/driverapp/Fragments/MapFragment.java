package com.example.driverapp.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.driverapp.MainActivity;
import com.example.driverapp.Models.Car;
import com.example.driverapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    public MapFragment(){

    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //TODO: read the comments bellow
            /**
             * this map fragment is called in one of two manners
             * 1: if the user navigates to the map without selecting a car (directly from the btm menu)
             *here we figure out two cases:
             *1.a- if the current tracked car (static) in the main activity is null: we don't show(the marker and the car details)
             *1.b- else : we show the car details, and:
             *1.b.1- if the car coordinates != null: we show them with a (previous_location_marker)
             *1_b.2- else: we don't show the marker (but car details still shown)
             * 2: if the user selects a car to track(by clicking on "view on map button"(or its shortcut in collapsed version),
             or by swiping to right): this car becomes the current tracked car (static) in the main activity, and we repeat 1.b;
             * 3: now when the user is on the map, and the car details of the car is shown and he clicked on the CURSOR
             -> the message is sent to the car phone number, and the sms Listener waits for the location sent over sms
             -> when we get the coordinates, we pin a (current_location_marker)
             */

            if (MainActivity.currTrackedCar != null) {
                if (MainActivity.currTrackedCar.getLastLocationLat() != null && MainActivity.currTrackedCar.getLastLocationLng() != null) {
                    Car trackedCar = MainActivity.currTrackedCar;
                    LatLng latLng = new LatLng(trackedCar.getLastLocationLat(), trackedCar.getLastLocationLng()); //cord.lat et cord.lng contient les 2 coordonées
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(trackedCar.getMarque() + " " + trackedCar.getModele()).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16), 3000, null);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}