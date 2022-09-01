package com.hos.hoslink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hos.hoslink.receivers.Core;

public class MainDashBoard extends AppCompatActivity {
    LinearLayout llBtnELD, llBtnECM, llBtnDrivers;
    ImageView ivLogout;
    TextView tvUserName;
    Switch switchCoDriver;
    String user = "";
    String password = "";
    String language = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash);

        this.llBtnELD = findViewById(R.id.llBtnELD);
        this.llBtnECM = findViewById(R.id.llBtnECM);
        this.llBtnDrivers = findViewById(R.id.llBtnDrivers);
        this.ivLogout = findViewById(R.id.ivLogout);
        this.tvUserName = findViewById(R.id.tvUserName);
        this.switchCoDriver = findViewById(R.id.switchCoDriver);

        this.user = GetValue("user");
        this.password = GetValue("password");
        this.language = GetValue("language");
        this.tvUserName.setText(user);
        this.switchCoDriver.setChecked(false);

        llBtnELD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = true;
                if(user == null || user.length() == 0){
                    validate = false;
                }
                if(password == null || password.length() == 0){
                    validate = false;
                }
                if(language == null || language.length() == 0){
                    validate = false;
                }
                if(validate){
                    Intent intent = new Intent();
                    intent.setAction(Core.ACTION_LOGIN_DRIVER);
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    //Data
                    Bundle bundle = new Bundle();
                    bundle.putString("user", user);
                    bundle.putString("password", password);
                    bundle.putString("language", language);
                    bundle.putInt("coDriver", switchCoDriver.isChecked() ? 1 : 0);
                    intent.setPackage("apollo.hos");// Only if necessary after API 30 Android 11 (Package Name App Receiver)
                    bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
                    intent.putExtras(bundle);
                    //Broadcast to ELD app
                    sendBroadcast(intent);
                    finish();
                }else {
                    ivLogout.callOnClick();
                }
            }
        });

        llBtnECM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDashBoard.this, DiagnosticActivity.class);
                startActivity(intent);
            }
        });

        llBtnDrivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Core.ACTION_DRIVERS_IN_ELD_REQUEST);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setPackage("apollo.hos");// Only if necessary after API 30 Android 11 (Package Name App Receiver)
                Bundle bundle = new Bundle();
                bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
                intent.putExtras(bundle);
                //Broadcast to ELD app
                sendBroadcast(intent);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveKey("user", "");
                SaveKey("password", "");
                SaveKey("language", "");
                Intent intent = new Intent(MainDashBoard.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void SaveKey(String key, String value) {
        try {
            SharedPreferences example = MyApplication.GetAppContext().getSharedPreferences("data", 0);
            SharedPreferences.Editor edit = example.edit();
            edit.putString(key, value);
            edit.commit();
        } catch (Exception e) {
        }
    }

    public static String GetValue(String key) {
        String value = "";
        try {
            SharedPreferences example = MyApplication.GetAppContext().getSharedPreferences("data", 0);
            value = example.getString(key, "");
        } catch (Exception e) {
        }
        return value;
    }
}
