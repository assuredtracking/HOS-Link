package com.hos.hoslink.receivers;

import static com.hos.hoslink.MainDashBoard.SaveKey;

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
                    Intent i = new Intent(context, MainDashBoard.class).putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setAction(Core.ACTION_ELD_LOGIN_RESPONSE);
                    context.startActivity(i);
                    if (bundle.getInt("code") == 0){
                        Toast.makeText(context, "Response code: " + bundle.getInt("code") + ", Message: " + bundle.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            }
            else if (intent.getAction() != null && intent.getAction().equals(Core.ACTION_LOGOUT_DRIVER)) {
                processDataForLogout(context, intent.getExtras(), intent.getAction());
            }
            else if (Core.ACTION_DRIVERS_IN_ELD_RESPONSE.equals(intent.getAction()) && intent.getExtras() != null) {
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

    private void processDataForLogout(Context context, Bundle bundle, String action) {
        try {
            Intent intent = new Intent(context, MainDashBoard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setAction(action);
            intent.putExtras(bundle);
            context.startActivity(intent);

            SaveKey("user", "");
            SaveKey("password", "");
            SaveKey("language", "");

        }
        catch (Exception e) {
            Log.e("processDataForLogout","LogoutReceiver.processDataForLogout: ", e);
        }
    }
}
