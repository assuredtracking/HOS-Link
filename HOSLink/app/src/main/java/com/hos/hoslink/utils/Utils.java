package com.hos.hoslink.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import com.hos.hoslink.MyApplication;

@SuppressLint("Wakelock")
@SuppressWarnings("deprecation")
public class Utils {
    private static final String TAG = "ERROR";
    private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String endLine = "\r";


    /*Package Name                    Registration ID                   Identifier             Operator
    apollo.hos                       000B                              APOLLO                 XOR
    in.HoursOfService                2PR8                              ITELD1                 AND
    fl.HoursOfService                EKU5                              FLELD1                 XOR
    su.HoursOfService                YPLH                              SNST20                 XOR
    ag.HoursOfService                001A                              ALERT1                 XOR
    ct.HoursOfService                JH3D                              ELD365                 XOR
    se.HoursOfService                IX6N                              SEELD3                 XOR
    tt.HoursOfService                986V                              TTSP3T                 XOR
    as.HoursOfService                JW25                              ATSAP1/TLELD1          XOR
    ats.HoursOfService               JW25                              ATSAP1/TLELD1          XOR
    mt.HoursOfService                4G2I                              MNEL21	              XOR
    is.HoursOfService                WLXX                              IELD01	              XOR
    bs.HoursOfService                U7PC                              BSE193	              XOR

        CANADA
    Package Name                    Registration ID                   Identifier             Operator
     apollo.hos                       6T01                              APLIOXCDT21            XOR
     apollo.hos                       6T05                              APLGEO                 XOR
     fh.HoursOfService                6T08                              FH1313	               XOR
     is.HoursOfService                6T09                              ISO100	               XOR
     pi.HoursOfService                6T10                              PROELD	               XOR
     fz.HoursOfService                6T11                              ELD FLWZ87             XOR
    */

    public static void SaveKey(String key, String value) {
        try {
            SharedPreferences example = MyApplication.GetAppContext().getSharedPreferences("data", 0);
            SharedPreferences.Editor edit = example.edit();
            edit.putString(key, value);
            edit.commit();
        }
        catch (Exception e) {
            Log.e(TAG, "SaveKey: ", e);
        }
    }
}

