package com.example.driverapp.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driverapp.Models.Car;
import com.example.driverapp.Models.CarViewModel;
import com.example.driverapp.Models.RecyclerViewAdapter;
import com.example.driverapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Observer;
import java.util.function.ToDoubleBiFunction;

public class ListFragment extends Fragment {

    CarViewModel carViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private  RecyclerView.LayoutManager layoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        // Add the following lines to create RecyclerView
        recyclerView = view.findViewById(R.id.recycle_view_container);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        carViewModel = new ViewModelProvider(getActivity(), ViewModelProvider
                .AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(CarViewModel.class);
        carViewModel.getAllCars().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                adapter.setCars(cars);
            }
        });
    }


}