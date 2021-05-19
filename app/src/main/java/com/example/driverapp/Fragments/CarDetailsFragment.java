package com.example.driverapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.driverapp.R;

public class CarDetailsFragment extends Fragment {

    TextView carName,carPhone,date,time;
    View mfragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mfragment =inflater.inflate(R.layout.fragment_car_details, container, false);

        return mfragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carName = mfragment.findViewById(R.id.infoName);
        carPhone = mfragment.findViewById(R.id.infoPhone);
        date = mfragment.findViewById(R.id.infoDate);
        time = mfragment.findViewById(R.id.infoTimeHour);
    }


}