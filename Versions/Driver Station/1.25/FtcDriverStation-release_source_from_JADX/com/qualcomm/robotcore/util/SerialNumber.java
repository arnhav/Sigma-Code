package com.qualcomm.robotcore.util;

import java.io.Serializable;

public class SerialNumber implements Serializable {
    private String f577a;

    public SerialNumber() {
        this.f577a = "N/A";
    }

    public SerialNumber(String serialNumber) {
        this.f577a = serialNumber;
    }

    public String getSerialNumber() {
        return this.f577a;
    }

    public void setSerialNumber(String serialNumber) {
        this.f577a = serialNumber;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object instanceof SerialNumber) {
            return this.f577a.equals(((SerialNumber) object).getSerialNumber());
        }
        if (object instanceof String) {
            return this.f577a.equals(object);
        }
        return false;
    }

    public int hashCode() {
        return this.f577a.hashCode();
    }

    public String toString() {
        return this.f577a;
    }
}
