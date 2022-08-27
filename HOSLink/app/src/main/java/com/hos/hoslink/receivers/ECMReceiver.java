package com.hos.hoslink.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hos.hoslink.utils.ECMDoubleValue;
import com.hos.hoslink.utils.ECMStringValue;

public class ECMReceiver extends BroadcastReceiver {

    private static MutableLiveData<ECMDoubleValue> lastSpeed = new MutableLiveData<>();
    private static MutableLiveData<ECMDoubleValue> lastOdometerValue = new MutableLiveData<>();
    private static MutableLiveData<ECMDoubleValue> lastRPMValue = new MutableLiveData<>();
    private static MutableLiveData<ECMDoubleValue> lastTotalEngineHoursValue = new MutableLiveData<>();
    private static ECMStringValue lastVINValue = null;
    private static ECMStringValue firmwareValue = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (Core.ACTION_ECM_VALUES.equals(intent.getAction()) && intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                //Speed
                if (bundle.containsKey(Core.ECM_SPEED)) {
                    double speedValue = bundle.getDouble(Core.ECM_SPEED);
                    ECMDoubleValue speed = new ECMDoubleValue(speedValue);
                    OnSpeed(speed);
                }
                //Odometer
                if (bundle.containsKey(Core.ECM_ODO)) {
                    double odoValue = bundle.getDouble(Core.ECM_ODO);
                    ECMDoubleValue odo = new ECMDoubleValue(odoValue);
                    OnOdometer(odo);
                }
                //Rpm
                if (bundle.containsKey(Core.ECM_RPM)) {
                    double rpmValue = bundle.getDouble(Core.ECM_RPM);
                    ECMDoubleValue rpm = new ECMDoubleValue(rpmValue);
                    OnRPM(rpm);
                }
                //Engine Hours
                if (bundle.containsKey(Core.ECM_ENGINE_HOURS)) {
                    double ehValue = bundle.getDouble(Core.ECM_ENGINE_HOURS);
                    ECMDoubleValue eh = new ECMDoubleValue(ehValue);
                    OnTotalEngineHours(eh);
                }
                //Vehicle VIN
                if (bundle.containsKey(Core.ECM_VIN)) {
                    String vinValue = bundle.getString(Core.ECM_VIN);
                    ECMStringValue vin = new ECMStringValue(vinValue);
                    OnVIN(vin);
                }
                //Device Firmware
                if (bundle.containsKey(Core.ECM_FIRMWARE)) {
                    String firmwareValue = bundle.getString(Core.ECM_FIRMWARE);
                    ECMStringValue firmware = new ECMStringValue(firmwareValue);
                    OnFirmware(firmware);
                }
            }
        } catch (Exception e) {
        }
    }

    public static LiveData<ECMDoubleValue> getLastSpeedObserver() {
        if (lastSpeed == null) {
            lastSpeed = new MutableLiveData<>();
        }
        return lastSpeed;
    }

    public static LiveData<ECMDoubleValue> getLastOdometerObserver() {
        if (lastOdometerValue == null) {
            lastOdometerValue = new MutableLiveData<>();
        }
        return lastOdometerValue;
    }

    public static LiveData<ECMDoubleValue> getLastRPMObserver() {
        if (lastRPMValue == null) {
            lastRPMValue = new MutableLiveData<>();
        }
        return lastRPMValue;
    }

    public static LiveData<ECMDoubleValue> getLastTotalEngineObserver() {
        if (lastTotalEngineHoursValue == null) {
            lastTotalEngineHoursValue = new MutableLiveData<>();
        }
        return lastTotalEngineHoursValue;
    }


    public  void OnOdometer(ECMDoubleValue value) {
        lastOdometerValue.postValue(value);
    }
    public static void OnRPM(ECMDoubleValue item) {
        lastRPMValue.postValue(item);
    }

    public  void OnTotalEngineHours(ECMDoubleValue value) {
        lastTotalEngineHoursValue.postValue(value);
    }

    public  void OnVIN(ECMStringValue value) {
        lastVINValue = value;
    }

    public  void OnFirmware(ECMStringValue value) {
        firmwareValue = value;
    }

    public  void OnSpeed(ECMDoubleValue value) {
        lastSpeed.postValue(value);
    }

    //GET
    public static ECMDoubleValue getLastOdometerValue() {
        if(lastOdometerValue == null){
            lastOdometerValue = new MutableLiveData<>();
        }
        return lastOdometerValue.getValue();
    }

    public static ECMDoubleValue getLastRPMValue() {
        if(lastRPMValue == null){
            lastRPMValue = new MutableLiveData<>();
        }
        return lastRPMValue.getValue();
    }

    public static ECMStringValue getLastVINValue() {
        return lastVINValue;
    }

    public static ECMStringValue getFirmwareValue() {
        return firmwareValue;
    }

    public static ECMDoubleValue getLastTotalEngineHoursValue() {
        if(lastTotalEngineHoursValue == null){
            lastTotalEngineHoursValue = new MutableLiveData<>();
        }
        return lastTotalEngineHoursValue.getValue();
    }

    public static ECMDoubleValue getLastSpeed() {
        if(lastSpeed == null){
            lastSpeed = new MutableLiveData<>();
        }
        return lastSpeed.getValue();
    }

    public static void ResetECMVariables() {
        lastOdometerValue.postValue(null);
        lastRPMValue.postValue(null);
        lastTotalEngineHoursValue.postValue(null);
        lastSpeed.postValue(null);
        lastVINValue = null;
    }
}
