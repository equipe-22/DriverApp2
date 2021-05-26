package com.example.driverapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driverapp.MainActivity;
import com.example.driverapp.Models.Car;
import com.example.driverapp.Models.CarViewModel;
import com.example.driverapp.Models.RecyclerViewAdapter;
import com.example.driverapp.R;
import com.example.driverapp.Utils.RecyclerViewSwipeDecorator;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class ListFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    MainActivity mainActivity;
    View thisFragment;

    public ListFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisFragment = inflater.inflate(R.layout.list_fragment, container, false);
        // Add the following lines to create RecyclerView
        recyclerView = thisFragment.findViewById(R.id.recycle_view_container);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisFragment.getContext()));
        MainActivity.myCars.getAllCars().observe(getViewLifecycleOwner(), new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                MainActivity.myCarsList = cars;
            }
        });
        adapter = new RecyclerViewAdapter(mainActivity);
        recyclerView.setAdapter(adapter);
        return thisFragment;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
    //here swiping gestures are handled
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Car selectedCar = MainActivity.myCarsList.get(position);
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    //supprimer la voiture selectionnee
                    AlertDialog rmDialog = askToRemove(position, selectedCar);
                    rmDialog.show();

                break;

                case ItemTouchHelper.RIGHT:
                    /*
                    si la voiture selectionee est jamais trackee avant on met la date corrante
                    sinon on modifie pas sa date de derniere localisation, parece que elle est modifiee
                    quand on click sur le button curseur (in center) sur la map
                    */
                    if (selectedCar.getLastTrackDate() == null){
                        selectedCar.setLastTrackDate(new Date(System.currentTimeMillis()).toLocaleString());
                    }
                    MainActivity.currTrackedCar = selectedCar;
                    mainActivity.setMapFragment();
                    mainActivity.btmNavView.setSelectedItemId(R.id.mapButton);
                    break;

            }
        }

        @Override
        public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_bin)
                    .addSwipeLeftLabel("Supprimer")
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .setSwipeLeftLabelTextSize(1,15)

                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.violet))
                    .addSwipeRightActionIcon(R.drawable.ic_map3)
                    .addSwipeRightLabel("Localiser")
                    .setSwipeRightLabelColor(Color.WHITE)
                    .setSwipeRightLabelTextSize(1,15)

                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };
    public AlertDialog askToRemove(int pos, Car dltCar) {

        return new AlertDialog.Builder(getContext())
                // set message, title, and icon
                .setTitle("Supprimer voiture")
                .setMessage("êtes-vous sûr de supprimer cette voiture?")
                .setIcon(R.drawable.ic_bin)
                .setPositiveButton("OUI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        MainActivity.myCars.delete(adapter.carList.get(pos));
                        adapter.notifyItemRemoved(pos);
                        dialog.dismiss();
                        updateAdapterList();

                        //make a snack bar for safe back
                        Snackbar mySnackBar = Snackbar.make(recyclerView, dltCar.getMarque() + " " + dltCar.getModele()+" est supprimée", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MainActivity.myCars.insert(dltCar);
                                adapter.setCars(MainActivity.myCarsList);
                                adapter.notifyItemInserted(pos);
                            }
                        });
                        mySnackBar.setCallback(new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                checkIfLastCarDeleted();
                            }

                            @Override
                            public void onShown(Snackbar sb) {
                                super.onShown(sb);
                            }
                        });
                        mySnackBar.show();

                    }
                })
                .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //refresh the adapter
                        adapter.setCars(MainActivity.myCarsList);
                        dialog.dismiss();
                    }
                })
                .create();
    }
    private void updateAdapterList() {
        MainActivity.myCars.getAllCars().observe(getViewLifecycleOwner(), new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                MainActivity.myCarsList = cars;
                adapter.setCars(cars);
            }
        });
    }
    private void checkIfLastCarDeleted() {
        MainActivity.myCars.getAllCars().observe(getViewLifecycleOwner(), new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                if(cars.isEmpty()){
                    MainActivity.myCarsList = cars;
                    mainActivity.setHomeFragment();
                }
            }
        });
    }

}