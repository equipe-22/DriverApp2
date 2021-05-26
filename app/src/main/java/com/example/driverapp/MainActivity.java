package com.example.driverapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.driverapp.Fragments.CarDetailsFragment;
import com.example.driverapp.Fragments.EmptyStateFragment;
import com.example.driverapp.Fragments.ListFragment;
import com.example.driverapp.Fragments.MapFragment;
import com.example.driverapp.Fragments.SearchFragment;
import com.example.driverapp.Fragments.addCar.formFragment;
import com.example.driverapp.Models.Car;
import com.example.driverapp.Models.CarDao;
import com.example.driverapp.Models.CarViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    public MapFragment mapFragment = new MapFragment();
    public CarDetailsFragment carDetails;
    public Boolean isInHome;

    //UI
    public BottomNavigationView btmNavView;
    public FloatingActionButton fab;
    public ImageButton profil;
    public ImageButton settings;
    public ImageButton info;
    public ImageButton logout;
    public AppBarLayout appBar;

    public static CarViewModel myCars ;
    public static List<Car> myCarsList = new List<Car>(){

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
    public static Car currTrackedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
       //intantiate the CarViewModel

        myCars = new  ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(CarViewModel.class);

        updateCarsList();
        setHomeFragment();
        fabClick();
        btmNavViewClicks();
        searchBtnClick();
        cancelSearchClick();
        profilAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHomeFragment();
    }
    public void updateCarsList(){
        myCars.getAllCars().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                myCarsList = cars;
            }
        });
    }
    //setting main fragments
    public void setHomeFragment() {
        //set the btm UI
        fab.setImageResource(R.drawable.ic_add);
        //set the fragments
        updateCarsList();
        if(myCarsList.isEmpty()) {
            EmptyStateFragment fragment = new EmptyStateFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment,"Empty").commit();
        } else {
            Fragment fragment = new ListFragment(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment,"List").commit();
        }
        isInHome = true;
        appBar.setVisibility(View.VISIBLE);
    }

    public void setMapFragment(){
        //set the btm UI
        fab.setImageResource(R.drawable.ic_cursor_outline);

        //set the fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mapFragment, "map").addToBackStack("map").commit();
        if(currTrackedCar != null) showCarDetails(currTrackedCar);
        isInHome = false;
        appBar.setVisibility(View.INVISIBLE);
    }

    //handling user clicks
    public void fabClick () {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInHome){
                    formFragment bottomSheetFragment = new formFragment();
                    bottomSheetFragment.show( getSupportFragmentManager(), bottomSheetFragment.getTag());
                }else {
                    currTrackedCar.setLastTrackDate(new Date(System.currentTimeMillis()).toLocaleString());
                    requestLocation();
                }
            }
        });

    }
    public void btmNavViewClicks(){
        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeButton:
                        setHomeFragment();
                    return true;
                    case R.id.mapButton:
                        setMapFragment();
                    return true;
                }
                return false;
            }
        });
    }

    //Car Details Fragment Transactions
    public void showCarDetails(Car car){
        carDetails = new CarDetailsFragment(car);
        FragmentTransaction fragmentsTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        if(!carDetails.isAdded()){
            fragmentsTransaction.setCustomAnimations(R.anim.up, R.anim.down)
                    .add(R.id.fragmentContainer,carDetails,"details")
                    .addToBackStack(null)
                    .commit();
        }else{
            fragmentsTransaction.setCustomAnimations(R.anim.up, R.anim.down)
                    .show(getSupportFragmentManager().findFragmentByTag("details"))
                    .addToBackStack(null)
                    .commit();
        }
    }
    public void hideCarDetails(){
        FragmentTransaction fragmentsTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        if(carDetails.isAdded()){
            fragmentsTransaction.setCustomAnimations(R.anim.up, R.anim.down).hide(carDetails).commit();
        }
    }

    //search bar animations
    public void searchBtnClick(){
        ImageButton search = (ImageButton) findViewById(R.id.search_btn);
        ConstraintLayout topAppBar= (ConstraintLayout) findViewById(R.id.top_app_bar);
        RelativeLayout userIcon = (RelativeLayout) findViewById(R.id.userBtn);
        ImageButton cancelBtn = (ImageButton) findViewById(R.id.cansel_btn);
        EditText searchBar = (EditText) findViewById(R.id.editText_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(topAppBar,new AutoTransition());
                userIcon.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                animSearchAppaire(searchBar, topAppBar);
                searchBar.setVisibility(View.VISIBLE);
                searchBar.setWidth(topAppBar.getWidth()-80);

                // change the fragment
                Fragment fragment = new SearchFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
            }
        });
    }
    public void cancelSearchClick(){
        ImageButton cancelBtn = (ImageButton) findViewById(R.id.cansel_btn);
        ImageButton search = (ImageButton) findViewById(R.id.search_btn);
        ConstraintLayout topAppBar= (ConstraintLayout) findViewById(R.id.top_app_bar);
        RelativeLayout userIcon = (RelativeLayout) findViewById(R.id.userBtn);
        EditText searchBar = (EditText) findViewById(R.id.editText_search);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(topAppBar,new AutoTransition());
                userIcon.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
                searchBar.setVisibility(View.GONE);
                animSearchDisappaire(searchBar, topAppBar);
                searchBar.setWidth(0);
                // change the fragment
                setHomeFragment();
            }
        });
    }

    //profil animations
    public void profilAnim (){

        profil.setOnClickListener(new View.OnClickListener() {
            boolean clicked = false;
            @Override
            public void onClick(View v) {
                if(!clicked){
                    animSettingsIN ();
                    animInfoIN ();
                    animLogoutIN ();
                    clicked = true;
                }else {
                    animSettingsOUT ();
                    animInfoOUT ();
                    animLogoutOUT ();
                    clicked = false;
                }

            }
        });

    }
    public void animSettingsIN (){
        Animation animation = new TranslateAnimation(0, 100,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        settings.startAnimation(animation);
        settings.setVisibility( View.VISIBLE );
    }
    public void animInfoIN (){

        Animation animation = new TranslateAnimation(0, 170,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        info.startAnimation(animation);
        info.setVisibility( View.VISIBLE );

    }
    public void animLogoutIN (){
        Animation animation = new TranslateAnimation(0, 240,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        logout.startAnimation(animation);
        logout.setVisibility( View.VISIBLE );

    }
    public void animSettingsOUT (){

        Animation animation = new TranslateAnimation(100, 0,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        settings.startAnimation(animation);
        settings.setVisibility( View.VISIBLE );
    }
    public void animInfoOUT (){

        Animation animation = new TranslateAnimation(170, 0,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        info.startAnimation(animation);
        info.setVisibility( View.VISIBLE );

    }
    public void animLogoutOUT (){

        Animation animation = new TranslateAnimation(240, 0,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        logout.startAnimation(animation);
        logout.setVisibility( View.VISIBLE );

    }
    public void animSearchAppaire (EditText searchbar, ConstraintLayout topAppBar) {
        Animation animation = new TranslateAnimation( topAppBar.getWidth() +80, 0,0, 0);
        animation.setDuration(700);
        animation.setFillAfter(true);
        searchbar.startAnimation(animation);
    }
    public void animSearchDisappaire (EditText searchbar, ConstraintLayout topAppBar) {
        Animation animation = new TranslateAnimation(0,  topAppBar.getWidth() +80,0, 0);
        animation.setDuration(700);
        searchbar.startAnimation(animation);


    }

    //SMS
    public void sendSMS(String phoneNo,String SMS ) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, SMS, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }
    //-------------------------------------
    public void requestLocation(){
    if(this.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
        Log.d("SendPosRequestFragment", "onClick: permission is granted");

        //on prend le numero et le code secret de la voiture selectionee a tracker
        String phoneNumber = currTrackedCar.getNumTele();
        String secretMessage = currTrackedCar.getCodeSecret();

        sendSMS(phoneNumber, secretMessage);

        Log.d("SendPosRequestFragment", "onClick: message sent");
    }else{
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
    }
}

    private void findViews(){
          btmNavView =(BottomNavigationView) findViewById(R.id.bottomNavigationView);
          fab = (FloatingActionButton) findViewById(R.id.fab);
          profil= (ImageButton) findViewById(R.id.user);
          settings =(ImageButton) findViewById(R.id.settings);
         info =(ImageButton) findViewById(R.id.info);
        logout= (ImageButton) findViewById(R.id.logout);
        appBar =(AppBarLayout) findViewById(R.id.appBar);
    }
}
