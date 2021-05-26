package com.example.driverapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

import com.example.driverapp.MainActivity;
import com.example.driverapp.Models.Coord;

public class SmsListener extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    String phoneNumber;


    @Override
    public void onReceive(Context context, Intent intent) {
        phoneNumber = MainActivity.currTrackedCar.getNumTele();
        boolean stop = false;

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                SmsMessage[] msgs = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(msgs[i].getMessageBody());
                }
                String sender = msgs[0].getOriginatingAddress();
                String message = sb.toString();
                //TODO : we have to make sure of the format of the phone number in the car app and the tracked app

                if(PhoneNumberUtils.compare(phoneNumber, sender))
                {
                    Coord coord = new Coord();
                    coord.recup_coord(message);
                    Intent i = new Intent();
                    i.setClassName("com.example.driverapp", "com.example.driverapp.Fragments.MapFragment");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        }

    }
}