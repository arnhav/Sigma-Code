package com.ftdi.j2xx.protocol;

public class SpiSlaveEvent {
    private int f128a;
    private boolean f129b;
    private Object f130c;
    private Object f131d;
    private Object f132e;

    public SpiSlaveEvent(int iEventType, boolean bSync, Object pArg0, Object pArg1, Object pArg2) {
        this.f128a = iEventType;
        this.f129b = bSync;
        this.f130c = pArg0;
        this.f131d = pArg1;
        this.f132e = pArg2;
    }

    public Object getArg0() {
        return this.f130c;
    }

    public void setArg0(Object arg) {
        this.f130c = arg;
    }

    public Object getArg1() {
        return this.f131d;
    }

    public void setArg1(Object arg) {
        this.f131d = arg;
    }

    public Object getArg2() {
        return this.f132e;
    }

    public void setArg2(Object arg) {
        this.f132e = arg;
    }

    public int getEventType() {
        return this.f128a;
    }

    public void setEventType(int type) {
        this.f128a = type;
    }

    public boolean getSync() {
        return this.f129b;
    }

    public void setSync(boolean bSync) {
        this.f129b = bSync;
    }
}
