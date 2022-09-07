package com.hos.hoslink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;
import com.hos.hoslink.receivers.Core;

public class MainDashBoard extends AppCompatActivity {

    private LinearLayout llBtnELD, llBtnECM, llBtnDrivers;
    private ImageView ivLogout;
    private TextView tvUserName;
    private SwitchMaterial switchCoDriver;
    private String user = "";
    private String password = "";
    private String language = "";
    private Button btnLogout;
    private MaterialTextView mtvVersion;

    private String pkgName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash);

        isPackageInstalled("apolloVEO.hos");
        if (pkgName.isEmpty()){
            isPackageInstalled("apollo.hos");
        }

        user = GetValue("user");
        password = GetValue("password");
        language = GetValue("language");

        if (user.isEmpty() || password.isEmpty()){
            Intent intent = new Intent(MainDashBoard.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (getIntent().getAction() != null && getIntent().getAction().equals(Core.ACTION_LOGOUT_DRIVER)){
            Intent intent = new Intent(MainDashBoard.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        llBtnELD = findViewById(R.id.llBtnELD);
        llBtnECM = findViewById(R.id.llBtnECM);
        llBtnDrivers = findViewById(R.id.llBtnDrivers);
        ivLogout = findViewById(R.id.ivLogout);
        tvUserName = findViewById(R.id.tvUserName);
        switchCoDriver = findViewById(R.id.switchCoDriver);
        btnLogout = findViewById(R.id.btnLogout);
        mtvVersion = findViewById(R.id.mtvVersion);
        String versionName = "version " + BuildConfig.VERSION_NAME;
        mtvVersion.setText(versionName);

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
            if (pkgName.isEmpty()) {
                validate = false;
                Toast.makeText(this, "Install Apollo ELD or Veosphere in this device", Toast.LENGTH_SHORT).show();
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
                intent.setPackage(pkgName);// Only if necessary after API 30 Android 11 (Package Name App Receiver)
                bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
                intent.putExtras(bundle);
                //Broadcast to ELD app
                sendBroadcast(intent);
                finish();
            }
        });

        llBtnECM.setOnClickListener(v -> {
            if (pkgName.isEmpty()) {
                Toast.makeText(this, "Install Apollo ELD or Veosphere in this device", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(MainDashBoard.this, DiagnosticActivity.class);
                startActivity(intent);   
            }
        });

        llBtnDrivers.setOnClickListener(v -> {
            if (pkgName.isEmpty()) {
                Toast.makeText(this, "Install Apollo ELD or Veosphere in this device", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent();
                intent.setAction(Core.ACTION_DRIVERS_IN_ELD_REQUEST);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setPackage(pkgName);// Only if necessary after API 30 Android 11 (Package Name App Receiver)
                Bundle bundle = new Bundle();
                bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
                intent.putExtras(bundle);
                //Broadcast to ELD app
                sendBroadcast(intent);
            }
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
            if (pkgName.isEmpty()) {
                Toast.makeText(this, "Install Apollo ELD or Veosphere in this device", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent();
                intent.setAction(Core.ACTION_LOGOUT_DRIVER);
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.setPackage(pkgName); // Only if necessary after API 30 Android 11 (Package Name App Receiver)
                Bundle bundle = new Bundle();
                bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
                bundle.putString("user", GetValue("user"));
                intent.putExtras(bundle);
                //Broadcast to ELD app
                sendBroadcast(intent);
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
        }
        catch (Exception e) {

        }
    }

    public static String GetValue(String key) {
        String value = "";
        try {
            SharedPreferences example = MyApplication.GetAppContext().getSharedPreferences("data", 0);
            value = example.getString(key, "");
        }
        catch (Exception e) {

        }
        return value;
    }

    private void isPackageInstalled(String packageName) {
        try {
            PackageManager packageManager = getPackageManager();
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            pkgName = packageName;
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.e("ERROR", "MainDashBoard: isPackageInstalled: ", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
