package com.hos.hoslink;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.hos.hoslink.receivers.Core;

public class MainDashBoard extends AppCompatActivity {
    LinearLayout llBtnELD, llBtnECM, llBtnDrivers;
    ImageView ivLogout;
    TextView tvUserName;
    Switch switchCoDriver;
    String user = "";
    String password = "";
    String language = "";
    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash);

        InitUI();
        checkResponse();
        LoadingUI();
        LoadingEvents();
    }

    private void InitUI() {
        user = GetValue("user");
        password = GetValue("password");
        language = GetValue("language");
    }

    private void checkResponse() {
        if (getIntent().getAction() != null && getIntent().getAction().equals(Core.ACTION_ELD_LOGIN_RESPONSE)){
            if (getIntent().getIntExtra("code", 0) == 1)
                Snackbar.make(findViewById(android.R.id.content), "Response code: " + getIntent().getIntExtra("code", 0) + ", Message: " + getIntent().getStringExtra("message"), Snackbar.LENGTH_LONG).show();
        }
        else if (getIntent().getAction() != null && getIntent().getAction().equals(Core.ACTION_LOGOUT_DRIVER)){
            SaveKey("user", "");
            SaveKey("password", "");
            SaveKey("language", "");
            Intent intent = new Intent(MainDashBoard.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void LoadingUI() {
        llBtnELD = findViewById(R.id.llBtnELD);
        llBtnECM = findViewById(R.id.llBtnECM);
        llBtnDrivers = findViewById(R.id.llBtnDrivers);
        ivLogout = findViewById(R.id.ivLogout);
        tvUserName = findViewById(R.id.tvUserName);
        switchCoDriver = findViewById(R.id.switchCoDriver);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void LoadingEvents() {
        tvUserName.setText(user);
        switchCoDriver.setChecked(false);

        llBtnELD.setOnClickListener(v -> {
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
            }
            else {
                ivLogout.callOnClick();
            }
        });

        llBtnECM.setOnClickListener(v -> {
            Intent intent = new Intent(MainDashBoard.this, DiagnosticActivity.class);
            startActivity(intent);
        });

        llBtnDrivers.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Core.ACTION_DRIVERS_IN_ELD_REQUEST);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setPackage("apollo.hos");// Only if necessary after API 30 Android 11 (Package Name App Receiver)
            Bundle bundle = new Bundle();
            bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
            intent.putExtras(bundle);
            //Broadcast to ELD app
            sendBroadcast(intent);
        });

        ivLogout.setOnClickListener(v -> {
            SaveKey("user", "");
            SaveKey("password", "");
            SaveKey("language", "");
            Intent intent = new Intent(MainDashBoard.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Core.ACTION_LOGOUT_DRIVER);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setPackage("apollo.hos"); // Only if necessary after API 30 Android 11 (Package Name App Receiver)
            Bundle bundle = new Bundle();
            bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
            bundle.putString("user", GetValue("user"));
            intent.putExtras(bundle);
            //Broadcast to ELD app
            sendBroadcast(intent);
            finish();
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

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentLogged = getIntent();
    }
}
