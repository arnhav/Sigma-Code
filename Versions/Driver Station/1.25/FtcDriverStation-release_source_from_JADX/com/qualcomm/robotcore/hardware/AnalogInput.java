package com.qualcomm.robotcore.hardware;

public class AnalogInput implements HardwareDevice {
    private AnalogInputController f676a;
    private int f677b;

    public AnalogInput(AnalogInputController controller, int channel) {
        this.f676a = null;
        this.f677b = -1;
        this.f676a = controller;
        this.f677b = channel;
    }

    public int getValue() {
        return this.f676a.getAnalogInputValue(this.f677b);
    }

    public String getDeviceName() {
        return "Analog Input";
    }

    public String getConnectionInfo() {
        return this.f676a.getConnectionInfo() + "; analog port " + this.f677b;
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }
}
