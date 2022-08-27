package com.hos.hoslink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etUserName, etPassword;
    private Spinner lenguages_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String user = GetValue("user");
        if(user != null && user.length() > 0){
            Intent intent = new Intent(MainActivity.this, MainDashBoard.class);
            startActivity(intent);
            finish();
        }

        btnLogin = findViewById(R.id.btnLogin);
        etUserName = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPass);

        lenguages_spinner = findViewById(R.id.lenguages_spinner);
        ArrayList<String> lenguageOption = new ArrayList<>();
        lenguageOption.add("English");
        lenguageOption.add("Español");
        lenguageOption.add("Français");

        ArrayList<String> lenOption = new ArrayList<>();
        lenOption.add("en");
        lenOption.add("es");
        lenOption.add("fr");

        ArrayAdapter<String> adapterLenguageOption = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lenguageOption);
        adapterLenguageOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lenguages_spinner.setAdapter(adapterLenguageOption);
        lenguages_spinner.post(new Runnable() {
            @Override
            public void run() {
                try {
                    lenguages_spinner.setSelection(0);
                } catch (Exception e) {
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserName.getText().toString().length() == 0 || etPassword.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Fill Required fields", Toast.LENGTH_LONG).show();
                }else {
                    SaveKey("user", etUserName.getText().toString());
                    SaveKey("password", etPassword.getText().toString());
                    int indexLang = lenguages_spinner.getSelectedItemPosition();
                    SaveKey("language", lenOption.get(indexLang));
                    Intent intent = new Intent(MainActivity.this, MainDashBoard.class);
                    startActivity(intent);
                    finish();
                }
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