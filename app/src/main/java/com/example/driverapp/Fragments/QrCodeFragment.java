package com.example.driverapp.Fragments;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.driverapp.MainActivity;
import com.example.driverapp.Models.Car;
import com.example.driverapp.R;
import com.google.zxing.Result;

import java.util.Objects;

public class QrCodeFragment extends DialogFragment {
    private CodeScanner mCodeScanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_qr_code, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendData(result.getText().toString());
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    private void sendData(String carDataStr) {
        String[] data = carDataStr.split(" ");
        for (int i=0; i<data.length; i++){
            Log.d("DATA", "splitString["+i+"] is " + data[i]);
    }
    Intent i = new Intent(requireActivity().getBaseContext(), MainActivity.class);
        i.putExtra("SENDER_KEY","QRscanner");
        Car car = new Car(data);
        i.putExtra("CarDataList", car);
        getActivity().startActivity(i);
    }
}