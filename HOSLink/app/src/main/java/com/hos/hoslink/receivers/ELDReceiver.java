package com.hos.hoslink.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hos.hoslink.MainDashBoard;

import java.util.List;

public class ELDReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (Core.ACTION_ELD_RESPONSE.equals(intent.getAction()) && intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null && bundle.containsKey("message")) {
                    Intent i = new Intent(context, MainDashBoard.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(i);
                    Toast.makeText(context, bundle.getString("message"), Toast.LENGTH_LONG).show();
                }
            } else if (Core.ACTION_DRIVERS_IN_ELD_RESPONSE.equals(intent.getAction()) && intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null && bundle.containsKey("drivers")) {
                    List<String> drivers = bundle.getStringArrayList("drivers");
                    String driversNames = "";
                    if (drivers != null) {
                        for (String item : drivers) {
                            if (driversNames.length() > 0) {
                                driversNames = driversNames.concat(", ");
                            }
                            driversNames = driversNames.concat(item);
                        }
                    }
                    if (driversNames.length() == 0) {
                        driversNames = "Not exist drivers logged in the ELD";
                    } else {
                        driversNames = "Driver(s) logged in the ELD: (" + driversNames + ")";
                    }
                    Toast.makeText(context, driversNames, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Not exist drivers logged in the ELD", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.d("ELDReceiver", e.getMessage());
        }
    }
}
