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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextInputLayout etUserName, etPassword;
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
        etUserName = findViewById(R.id.tilUsername);
        etPassword = findViewById(R.id.tilPassword);

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
        lenguages_spinner.post(() -> {
            try {
                lenguages_spinner.setSelection(0);
            }
            catch (Exception e) {

            }
        });

        btnLogin.setOnClickListener(v -> {
            if (etUserName.getEditText().getText().toString().length() == 0 || etPassword.getEditText().getText().toString().length() == 0) {
                Toast.makeText(MainActivity.this, "Fill Required fields", Toast.LENGTH_LONG).show();
            }
            else {
                SaveKey("user", etUserName.getEditText().getText().toString().trim());
                SaveKey("password", etPassword.getEditText().getText().toString().trim());
                int indexLang = lenguages_spinner.getSelectedItemPosition();
                SaveKey("language", lenOption.get(indexLang));
                Intent intent = new Intent(MainActivity.this, MainDashBoard.class);
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