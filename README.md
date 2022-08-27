# HOS-Link

... HOS Link is a Client Android App to connect to different functionalities to HOS App ELD....

-------

<p align="center">
    <a href="#setup">Setup 🛠️</a> &bull;
    <a href="#main-funtionalities">Main Funtionalities 🚀</a> &bull;
    <a href="#flow-of-process">Flow Process 🧬</a> &bull;
    <a href="#get-ecm-values">Get ECM Values 📖</a> &bull;
    <a href="#core-action-names">Core Action Names 🖍️</a> &bull;
    <a href="#documentation">Documentation📖</a>
</p>

-------

## SETUP

🛠️ Latest releases:

>	Android Studio Version 2021.2.1 Chipmunk

>	Runtime Version 11.0.12 (JRE)

>	Android Gradle Public Version 4.0.2

>	Gradle Version 6.0.1

>	Compile SDK Version API 32

>	Minimum SDK Version 22

>	Android 12 (S)

>	Java Version 1.8

```gradle
dependencies {

    implementation 'androidx.appcompat:appcompat:1.5.0'
    implementation 'com.google.android.material:material:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
}
```

## MAIN FUNTIONALITIES

HOS Link is a Client Android App to connect to different functionalities to HOS App ELD.

Main Functionalities:

>	Login Driver in the ELD App

>	Login Co-Driver in the ELD App

>	Know users name of users logged in the ELD App

>	Get ECM Values:

o	RPM

o	Odometer

o	Speed

o	Engine Hours

o	Tractor VIN

o	Firmware of Device connected to ELD APP

## FLOW OF PROCESS

![HOSLink](https://user-images.githubusercontent.com/15363215/187051132-0fc9e67f-d65c-4a8d-94e3-0bc3d946623e.PNG)


## GET ECM VALUES

o	RPM - (Type: Double)

o	Odometer - (Type: Double)

o	Speed - (Type: Double)

o	Engine Hours - (Type: Double)

o	Tractor VIN - (Type: String)

o	Firmware of Device connected to ELD APP - (Type: String)

1-	The App Client need to implement class for ECM Receiver and create listener in the manifest file:

AndroidManifest.xml

```java
<receiver
    android:name=".receivers.ECMReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="ACTION_ECM_VALUES" />
    </intent-filter>
</receiver>

public class ECMReceiver extends BroadcastReceiver {
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


```

2-	If the Device is not connected and some values are failing the value are no send to Client App
3-	If the Device is connected and values are good. The values are sent in real time each time the device send the information.

![image](https://user-images.githubusercontent.com/15363215/187050951-c88f7ddf-33f3-40cc-83e4-3f0d35ed40b2.png)
![image](https://user-images.githubusercontent.com/15363215/187050952-90a3d800-a462-4e21-815d-7ed62b6c57a8.png)
![image](https://user-images.githubusercontent.com/15363215/187050957-78b3493f-238a-489e-9583-a5217c02a40b.png)



# CORE ACTION NAMES

ACTION_ECM_VALUES = "ACTION_ECM_VALUES";

ACTION_LOGIN_DRIVER = "ACTION_LOGIN_DRIVER";

ACTION_ELD_RESPONSE = "ACTION_ELD_RESPONSE";

ACTION_DRIVERS_IN_ELD_REQUEST = "ACTION_DRIVERS_IN_ELD_REQUEST";

ACTION_DRIVERS_IN_ELD_RESPONSE = "ACTION_DRIVERS_IN_ELD_RESPONSE";

ECM_ODO = "ODO";

ECM_SPEED = "SPEED";

ECM_RPM = "RPM";

ECM_ENGINE_HOURS = "EH";

ECM_VIN = "VIN";

ECM_FIRMWARE = "FIRMWARE";

# Documentation

[MOO_Android_HOS_Link_Version_1.0_08282022.pdf](https://github.com/assuredtracking/HOS-Link/files/9438808/MOO_Android_HOS_Link_Version_1.0_08282022.pdf)

# Developed By

- Assured Techmatics Developer Team

# License

    Copyright 2022 Assured Techmatics

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
