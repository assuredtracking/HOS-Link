package com.hos.hoslink.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ECMStringValue  implements Serializable {

    private String value;
    private Date timestamp;

    public ECMStringValue(String pValue) {
        value = pValue;
        Calendar cal = Calendar.getInstance();
        timestamp = cal.getTime();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public final Date getTimestamp() {
        return timestamp;
    }

    public final void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
