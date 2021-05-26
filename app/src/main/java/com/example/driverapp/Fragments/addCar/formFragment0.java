package com.example.driverapp.Fragments.addCar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.driverapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class formFragment0 extends Fragment {

    private Button btnFillManually;
    private Button btnScanQR;
    private formFragment bottomSheetFragment;

    public formFragment0() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recuperer l'objet de bottomSheetFragment pour pouvoir utiliser le bouton annuler.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bottomSheetFragment = bundle.getParcelable("this"); // Key
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_form0, container, false);
        fillManually(v);
        scanQrCode(v);
        return v;
    }



    public void fillManually (View v){
        btnFillManually = v.findViewById(R.id.manualFill);

        btnFillManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetFragment.setFragment1();

            }
        });
    }


    public void scanQrCode (View v){

        btnScanQR = v.findViewById(R.id.scanQR);

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    QrCodeFragment QrFragment = new QrCodeFragment();
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

                    QrFragment.show(getActivity().getSupportFragmentManager(), "QrFragment");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }




}