package com.example.driverapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driverapp.Models.Car;
import com.example.driverapp.Models.RecyclerViewAdapter;
import com.example.driverapp.R;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

public class ListFragment extends Fragment {


    public ListFragment(List<Car> carList){
        //required empty constructor
    }
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
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
    /*TODO
          Update RecyclerView
                */
        //recyclerView.setAdapter(new RecyclerViewAdapter(carList,view.getContext()));
        return view;
    }

}