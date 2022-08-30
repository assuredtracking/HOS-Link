package com.hos.hoslink;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.hos.hoslink.receivers.Core;
import com.hos.hoslink.receivers.ECMReceiver;
import com.hos.hoslink.utils.ECMDoubleValue;
import com.hos.hoslink.utils.ECMStringValue;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DiagnosticActivity extends AppCompatActivity {
    private TextView  tvEngineData, tvRpm, tvOdometer, tvSpeed, tvEngineHours, tvFirmware, tvVin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Core.TIME_FORMAT_TZ, Locale.US);
    ECMDoubleValue odo = null;
    boolean rpmOk = false;
    boolean odometerOk = false;
    boolean speedOk = false;
    boolean engHoursOk = false;
    boolean engineDataOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecm);
        SendEnabledECMValues();
        LoadingUI();
        CreateObservers();
    }

    private void SendEnabledECMValues() {
        try {
            Intent intent = new Intent();
            intent.setAction(Core.ACTION_ENABLE_ECM_VALUES);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setPackage("apollo.hos");// Only if necessary after API 30 Android 11 (Package Name App Receiver)
            Bundle bundle = new Bundle();
            bundle.putString("packageName", MyApplication.GetAppContext().getPackageName());
            //Broadcast to ELD app
            sendBroadcast(intent);
        }catch (Exception e){

        }
    }

    private void CreateObservers() {
        ECMReceiver.getLastSpeedObserver().observe(this, new Observer<ECMDoubleValue>() {
            @Override
            public void onChanged(ECMDoubleValue ecmDoubleValue) {
                if (ecmDoubleValue == null) {
                    return;
                }
                updateLastSpeed(ecmDoubleValue, System.currentTimeMillis() / 1000);
                //Update others features
                //updateOtherFeatures();
                //engine state
                updateEngineState();
            }
        });
        ECMReceiver.getLastOdometerObserver().observe(this, new Observer<ECMDoubleValue>() {
            @Override
            public void onChanged(ECMDoubleValue ecmDoubleValue) {
                if (ecmDoubleValue == null) {
                    return;
                }
                updateLastOdometer(ecmDoubleValue, System.currentTimeMillis() / 1000);
                //engine state
                updateEngineState();
            }
        });
        ECMReceiver.getLastRPMObserver().observe(this, new Observer<ECMDoubleValue>() {
            @Override
            public void onChanged(ECMDoubleValue ecmDoubleValue) {
                if (ecmDoubleValue == null) {
                    return;
                }
                updateLastRPM(ecmDoubleValue, System.currentTimeMillis() / 1000);
                //engine state
                updateEngineState();
            }
        });
        ECMReceiver.getLastTotalEngineObserver().observe(this, new Observer<ECMDoubleValue>() {
            @Override
            public void onChanged(ECMDoubleValue ecmDoubleValue) {
                if (ecmDoubleValue == null) {
                    return;
                }
                updateLastEngineHours(ecmDoubleValue, System.currentTimeMillis() / 1000);
                //engine state
                updateEngineState();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        doDiagnostic();

    }

    private void LoadingUI() {
        tvEngineData = findViewById(R.id.tvEngineData);
        tvRpm = findViewById(R.id.tvRPM);
        tvOdometer = findViewById(R.id.tvOdometer);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvEngineHours = findViewById(R.id.tvEngineHours);
        tvFirmware = findViewById(R.id.tvFirmware);
        tvVin = findViewById(R.id.tvVin);
    }

    private void cleanUI() {
        tvEngineData.setText("");
        tvRpm.setText("");
        tvOdometer.setText("");
        tvSpeed.setText("");
        tvEngineHours.setText("");
        tvFirmware.setText("");
        tvVin.setText("");
    }


    private void doDiagnostic() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    long currentTimestamp = System.currentTimeMillis() / 1000;
                    //rpm
                    ECMDoubleValue tempValue = ECMReceiver.getLastRPMValue();
                    updateLastRPM(tempValue, currentTimestamp);
                    //odo
                    tempValue = ECMReceiver.getLastOdometerValue();
                    updateLastOdometer(tempValue, currentTimestamp);
                    //speed
                    tempValue = ECMReceiver.getLastSpeed();
                    updateLastSpeed(tempValue, currentTimestamp);
                    //engine hours
                    tempValue = ECMReceiver.getLastTotalEngineHoursValue();
                    updateLastEngineHours(tempValue, currentTimestamp);
                    //engine state
                    updateEngineState();
                    //Update others features
                    updateOtherFeatures();
                }
            });
        } catch (Exception e) {
        }
    }

    private void updateOtherFeatures() {
        //firmware
        updateFirmware();
        //vin
        updateVin();
    }

    private void updateFirmware() {
        try {
            ECMStringValue firmware = ECMReceiver.getFirmwareValue();
            if (firmware == null || firmware.getValue() == null || firmware.getValue().length() == 0) {
                tvFirmware.setText(getString(R.string.not_available));
            } else {
                tvFirmware.setText(firmware.getValue());
            }
        } catch (Exception e) {

        }
    }

    private void updateVin() {
        try {
            String vinText = getString(R.string.not_available);
            tvVin.setTextColor(Color.GRAY);
            tvVin.setText(vinText);
            if (ECMReceiver.getLastVINValue() != null && ECMReceiver.getLastVINValue().getValue() != null && ECMReceiver.getLastVINValue().getValue().length() > 0) {
                vinText = ECMReceiver.getLastVINValue().getValue();
                tvVin.setText(vinText);
                tvVin.setTextColor(android.graphics.Color.rgb(0, 153, 0));
            }
        } catch (Exception e) {
        }
    }

    private void updateEngineState() {
        try {
            engineDataOk = rpmOk && odometerOk && speedOk && engHoursOk;
            if (engineDataOk) {
                tvEngineData.setText(getString(R.string.passed));
                tvEngineData.setTextColor(android.graphics.Color.rgb(0, 153, 0));
            } else {
                tvEngineData.setText(getString(R.string.failure));
                tvEngineData.setTextColor(android.graphics.Color.RED);
            }
        } catch (Exception e) {
        }
    }

    private void updateLastSpeed(ECMDoubleValue tempValue, long currentTimestamp) {
        try {
            if (tempValue != null && tempValue.getTimestamp().getTime() > 0) {
                String speedUnit = getString(R.string.mph);
                int speed = (int) tempValue.getValue();

                if (currentTimestamp - Core.ECM_SPEED_TIME < tempValue.getTimestamp().getTime() / 1000) {
                    String speedText = dateFormat.format(tempValue.getTimestamp().getTime()) + " - " + String.valueOf(speed) + " " + speedUnit;
                    tvSpeed.setText(speedText);
                    tvSpeed.setTextColor(android.graphics.Color.rgb(0, 153, 0));
                    speedOk = true;
                } else {
                    tvSpeed.setText(getString(R.string.missing));
                    tvSpeed.setTextColor(android.graphics.Color.RED);
                }
            } else {
                tvSpeed.setText(getString(R.string.missing));
                tvSpeed.setTextColor(android.graphics.Color.RED);
            }
        } catch (Exception e) {
        }
    }

    private void updateLastEngineHours(ECMDoubleValue tempValue, long currentTimestamp) {
        try {
            if (tempValue != null && tempValue.getTimestamp().getTime() > 0) {
                tvEngineHours.setText(dateFormat.format(tempValue.getTimestamp().getTime()) + " - " + String.valueOf(tempValue.getValue()) + " " + getString(R.string.hours));
                if (currentTimestamp - Core.ECM_ENG_HOURS_TIME < tempValue.getTimestamp().getTime() / 1000) {
                    tvEngineHours.setTextColor(android.graphics.Color.rgb(0, 153, 0));
                    engHoursOk = true;
                } else {
                    tvEngineHours.setText(getString(R.string.missing));
                    tvEngineHours.setTextColor(android.graphics.Color.RED);
                }
            }else {
                tvEngineHours.setText(getString(R.string.missing));
                tvEngineHours.setTextColor(android.graphics.Color.RED);
            }
        } catch (Exception e) {
        }
    }

    private void updateLastRPM(ECMDoubleValue tempValue, long currentTimestamp) {
        try {
            if (tempValue != null && tempValue.getTimestamp().getTime() > 0) {
                tvRpm.setText(dateFormat.format(tempValue.getTimestamp().getTime()) + " - " + String.valueOf((int) (tempValue.getValue())));
                if (currentTimestamp - Core.ECM_RPM_TIME < tempValue.getTimestamp().getTime() / 1000) {
                    tvRpm.setTextColor(android.graphics.Color.rgb(0, 153, 0));
                    rpmOk = true;
                } else {
                    tvRpm.setText(getString(R.string.missing));
                    tvRpm.setTextColor(android.graphics.Color.RED);
                }

            } else {
                tvRpm.setText(getString(R.string.missing));
                tvRpm.setTextColor(android.graphics.Color.RED);
            }
        } catch (Exception e) {
        }
    }

    private void updateLastOdometer(ECMDoubleValue tempValue, long currentTimestamp) {
        try {
            if (tempValue != null && tempValue.getTimestamp().getTime() > 0) {

                String distanceUnit = getString(R.string.miles);
                int distance = (int) tempValue.getValue();

                if (currentTimestamp - Core.ECM_ODOMETER_TIME < tempValue.getTimestamp().getTime() / 1000) {
                    String odometerText = dateFormat.format(tempValue.getTimestamp().getTime()) + " - " + String.valueOf(distance) + " " + distanceUnit;
                    tvOdometer.setText(odometerText);
                    tvOdometer.setTextColor(android.graphics.Color.rgb(0, 153, 0));
                    odometerOk = true;
                    odo = tempValue;
                } else {
                    tvOdometer.setText(getString(R.string.missing));
                    tvOdometer.setTextColor(android.graphics.Color.RED);
                }
            } else {
                tvOdometer.setText(getString(R.string.missing));
                tvOdometer.setTextColor(android.graphics.Color.RED);
            }
        } catch (Exception e) {
        }
    }

}