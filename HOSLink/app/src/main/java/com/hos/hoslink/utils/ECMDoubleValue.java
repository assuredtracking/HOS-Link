package com.hos.hoslink.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ECMDoubleValue implements Serializable {

    private double value;
    private Date timestamp;

    public ECMDoubleValue(double pValue) {
        value = pValue;
        Calendar cal = Calendar.getInstance();
        timestamp = cal.getTime();
    }

    public ECMDoubleValue(double pValue, Date date) {
        value = pValue;
        timestamp = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public final Date getTimestamp() {
        return timestamp;
    }

    public final void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
