package com.example.driverapp.Models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driverapp.R;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    List<Car> carList = new ArrayList<Car>();
    Context context;

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
            TransitionManager.beginDelayedTransition(holder.cardView,new AutoTransition());
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
    }

    public void setCars (List<Car> cars){
        carList = cars;
        notifyDataSetChanged();
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
        Button localiserBtn;
        ConstraintLayout expandedCarView;
        CardView cardView;
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
            localiserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"button is clicked",Toast.LENGTH_SHORT).show();
                }
            });

            localiserRacco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"button is clicked",Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


}
