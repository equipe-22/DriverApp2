package com.example.driverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.example.driverapp.Fragments.formFragment;
import com.example.driverapp.Models.Car;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MapFragment mapFragment = new MapFragment();
    CarDetailsFragment carDetails = new CarDetailsFragment();
    BottomNavigationView btmNavView;
    FloatingActionButton fab;

    Boolean isInHome;
    Boolean carDetailsShowed;
    ImageButton profil;
    ImageButton settings;
    ImageButton info;
    ImageButton logout;
    AppBarLayout appBar;


    List<Car> carList = new ArrayList<Car>();

    public void appendCarList(Car car) {
        this.carList.add(car);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
//        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
//        Car car = new Car("kia","cerato","098259","iiRR55~~","088475899");
//        carList.add(car);
        setHomeFragment(carList);
        fabClick();
        btmNavViewClicks();
        searchBtnClick();
        cancelSearchClick();
        profilAnim();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.receiveData();
    }

    //setting main fragments
    public void setHomeFragment(List<Car> carList) {
        FragmentTransaction fragmentsTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        if (carList.size() > 0) {
            Fragment fragment = new ListFragment(carList);
            fragmentsTransaction.replace(R.id.fragmentContainer, fragment).commit();
        }else {
            Fragment fragment = new EmptyStateFragment();
            fragmentsTransaction.replace(R.id.fragmentContainer,fragment).commit();
        }
        isInHome = true;
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        appBar.setVisibility(View.VISIBLE);

    }
    public void setMapFragment(){
        FragmentTransaction fragmentsTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fragmentsTransaction.replace(R.id.fragmentContainer, mapFragment, "map").addToBackStack("map").commit();
        showCarDetails();
        isInHome = false;
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        appBar.setVisibility(View.INVISIBLE);
    }

    //handling user clicks
    public void fabClick () {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInHome){
                    formFragment bottomSheetFragment = new formFragment();
                    bottomSheetFragment.show( getSupportFragmentManager(), bottomSheetFragment.getTag());
                }else {
                    if(carDetailsShowed){
                        hideCarDetails();
                        carDetailsShowed = false;
                    }else {
                        showCarDetails();
                        carDetailsShowed = true;
                    }
                }

            }
        });

    }
    public void btmNavViewClicks(){
        btmNavView =(BottomNavigationView) findViewById(R.id.bottomNavigationView);
        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeIcon:
                        if(!isInHome){
                            hideCarDetails();
                            setHomeFragment(carList);
                            fab.setImageResource(R.drawable.ic_add);
                            isInHome = true;
                        }
                        return true;
                    case R.id.locationIcon:
                        if(isInHome){
                            setMapFragment();
                            fab.setImageResource(R.drawable.ic_cursor_outline);
                            isInHome = false;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    //Car Details Fragment Transactions
    void showCarDetails(){
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
        carDetailsShowed = true;
    }
    void hideCarDetails(){
        FragmentTransaction fragmentsTransaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        if(carDetails.isAdded()){
            fragmentsTransaction.setCustomAnimations(R.anim.up, R.anim.down).hide(carDetails).commit();
            carDetailsShowed = false;
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
                setHomeFragment(carList);
            }
        });
    }

    //profil animations
    public void profilAnim (){

        profil = findViewById(R.id.user);
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
        settings = findViewById(R.id.settings);

        Animation animation = new TranslateAnimation(0, 100,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        settings.startAnimation(animation);
        settings.setVisibility( View.VISIBLE );
    }
    public void animInfoIN (){
        info = findViewById(R.id.info);

        Animation animation = new TranslateAnimation(0, 170,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        info.startAnimation(animation);
        info.setVisibility( View.VISIBLE );

    }
    public void animLogoutIN (){
        logout = findViewById(R.id.logout);

        Animation animation = new TranslateAnimation(0, 240,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        logout.startAnimation(animation);
        logout.setVisibility( View.VISIBLE );

    }
    public void animSettingsOUT (){
        settings = findViewById(R.id.settings);

        Animation animation = new TranslateAnimation(100, 0,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        settings.startAnimation(animation);
        settings.setVisibility( View.VISIBLE );
    }
    public void animInfoOUT (){
        info = findViewById(R.id.info);

        Animation animation = new TranslateAnimation(170, 0,0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        info.startAnimation(animation);
        info.setVisibility( View.VISIBLE );

    }
    public void animLogoutOUT (){
        logout = findViewById(R.id.logout);

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

    private void receiveData() {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        if(i.getExtras() != null ) {
            if (i.getExtras().getString("SENDER_KEY") == "QRscanner") {
                Car car = (Car) i.getSerializableExtra("CarDataList");
                appendCarList(car);
                setHomeFragment(this.carList);
            }
        }
    }
}