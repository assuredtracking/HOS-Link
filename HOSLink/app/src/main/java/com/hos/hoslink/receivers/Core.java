package com.hos.hoslink.receivers;

public class Core {
    public static final String ACTION_ECM_VALUES = "ACTION_ECM_VALUES";
    public static final String ACTION_LOGIN_DRIVER = "ACTION_LOGIN_DRIVER";
    public static final String ACTION_LOGOUT_DRIVER = "ACTION_LOGOUT_DRIVER";
    public static final String ACTION_ELD_LOGIN_RESPONSE = "ACTION_ELD_LOGIN_RESPONSE";
    public static final String ACTION_ELD_RESPONSE = "ACTION_ELD_RESPONSE";
    public static final String ACTION_DRIVERS_IN_ELD_REQUEST = "ACTION_DRIVERS_IN_ELD_REQUEST";
    public static final String ACTION_DRIVERS_IN_ELD_RESPONSE = "ACTION_DRIVERS_IN_ELD_RESPONSE";
    public static final String ACTION_ENABLE_ECM_VALUES = "ACTION_ENABLE_ECM_VALUES";
    public static final String TIME_FORMAT_TZ = "hh:mm:ss a (z)";
    public static final String ECM_ODO = "ODO";
    public static final String ECM_SPEED = "SPEED";
    public static final String ECM_RPM = "RPM";
    public static final String ECM_ENGINE_HOURS = "EH";
    public static final String ECM_VIN = "VIN";
    public static final String ECM_FIRMWARE = "FIRMWARE";
    public static final long ECM_RPM_TIME = 120; //2 Min
    public static final long ECM_SPEED_TIME = 120; //2 Min
    public static final long ECM_ENG_HOURS_TIME = 120; //2 Min
    public static final long ECM_ODOMETER_TIME = 120; //2 Min
}
