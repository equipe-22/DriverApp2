package com.example.driverapp.Models;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driverapp.MainActivity;
import com.example.driverapp.R;

import org.jetbrains.annotations.NotNull;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    public List<Car> carList = new List<Car>(){

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable @org.jetbrains.annotations.Nullable Object o) {
            return false;
        }

        @NonNull
        @NotNull
        @Override
        public Iterator<Car> iterator() {
            return null;
        }

        @NonNull
        @NotNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @NotNull
        @Override
        public <T> T[] toArray(@NonNull @NotNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Car car) {
            return false;
        }

        @Override
        public boolean remove(@Nullable @org.jetbrains.annotations.Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull @NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull @NotNull Collection<? extends Car> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull @NotNull Collection<? extends Car> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull @NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull @NotNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Car get(int index) {
            return null;
        }

        @Override
        public Car set(int index, Car element) {
            return null;
        }

        @Override
        public void add(int index, Car element) {

        }

        @Override
        public Car remove(int index) {
            return null;
        }

        @Override
        public int indexOf(@Nullable @org.jetbrains.annotations.Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable @org.jetbrains.annotations.Nullable Object o) {
            return 0;
        }

        @NonNull
        @NotNull
        @Override
        public ListIterator<Car> listIterator() {
            return null;
        }

        @NonNull
        @NotNull
        @Override
        public ListIterator<Car> listIterator(int index) {
            return null;
        }

        @NonNull
        @NotNull
        @Override
        public List<Car> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    Context context;
    MainActivity mainActivity;

    public RecyclerViewAdapter(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        setCars(MainActivity.myCarsList);
    }

    public void setCars (List<Car> cars){
        this.carList = cars;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_list_item,parent,false);
        context = view.getContext();
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == 0){
            holder.expandedCarView.setVisibility(View.VISIBLE);
            holder.localiserRacco.setVisibility(View.GONE);
            holder.letter.setVisibility(View.GONE);
            holder.smallPhoneNumber.setVisibility(View.GONE);
        }
        holder.marqueModele.setText(carList.get(position).getMarque() +" " +carList.get(position).getModele());
        holder.matricule.setText(carList.get(position).getMatricule());
        holder.codeSecret.setText(carList.get(position).getCodeSecret());
        holder.numTele.setText(carList.get(position).getNumTele());
        holder.smallPhoneNumber.setText(carList.get(position).getNumTele());
        holder.letter.setText(String.valueOf(carList.get(position).getModele().charAt(0)));
        Resources res = context.getResources();
        Drawable myImage = ResourcesCompat.getDrawable(res, R.drawable.car, null);

        holder.localiserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                si la voiture selectionee est jamais trackee avant on met la date corrante
                sinon on modifie pas sa date de derniere localisation, parece que elle est modifiee
                quand on click sur le button curseur (in center) sur la map
                */
                if (carList.get(position).getLastTrackDate() == null){
                    carList.get(position).setLastTrackDate(new Date(System.currentTimeMillis()).toLocaleString());
                }

                MainActivity.currTrackedCar = carList.get(position);
                mainActivity.setMapFragment();
                mainActivity.btmNavView.setSelectedItemId(R.id.mapButton);
            }
        });
        holder.localiserRacco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                si la voiture selectionee est jamais trackee avant on met la date corrante
                sinon on modifie pas sa date de derniere localisation, parece que elle est modifiee
                quand on click sur le button curseur (in center) sur la map
                */
                if (carList.get(position).getLastTrackDate() == null){
                    carList.get(position).setLastTrackDate(new Date(System.currentTimeMillis()).toLocaleString());
                }
                MainActivity.currTrackedCar = carList.get(position);
                mainActivity.setMapFragment();
                mainActivity.btmNavView.setSelectedItemId(R.id.mapButton);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView marqueModele;
        TextView matricule;
        TextView codeSecret;
        TextView numTele;
        ConstraintLayout expandedCarView;
        CardView cardView;
        Button localiserBtn;
        ImageButton localiserRacco;
        TextView letter;
        TextView smallPhoneNumber;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            marqueModele = itemView.findViewById(R.id.marque_modele);
            matricule = itemView.findViewById(R.id.value_matricule);
            codeSecret = itemView.findViewById(R.id.value_code_secret);
            numTele = itemView.findViewById(R.id.value_num_tele);
            localiserBtn = itemView.findViewById(R.id.localiser_btn);
            expandedCarView = itemView.findViewById(R.id.expanded_cardView);
            cardView = itemView.findViewById(R.id.cardView);
            localiserRacco = itemView.findViewById(R.id.locate_racco);
            letter = itemView.findViewById(R.id.letter);
            smallPhoneNumber = itemView.findViewById(R.id.small_phoneNumber);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandedCarView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        expandedCarView.setVisibility(View.VISIBLE);
                        localiserRacco.setVisibility(View.GONE);
                        letter.setVisibility(View.GONE);
                        smallPhoneNumber.setVisibility(View.GONE);
                    }
                    else{
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        expandedCarView.setVisibility(View.GONE);
                        localiserRacco.setVisibility(View.VISIBLE);
                        letter.setVisibility(View.VISIBLE);
                        smallPhoneNumber.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }
}
