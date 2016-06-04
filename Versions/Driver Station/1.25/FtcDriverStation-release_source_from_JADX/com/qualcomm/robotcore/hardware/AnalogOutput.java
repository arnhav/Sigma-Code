package com.qualcomm.robotcore.hardware;

public class AnalogOutput implements HardwareDevice {
    private AnalogOutputController f678a;
    private int f679b;

    public AnalogOutput(AnalogOutputController controller, int channel) {
        this.f678a = null;
        this.f679b = -1;
        this.f678a = controller;
        this.f679b = channel;
    }

    public void setAnalogOutputVoltage(int voltage) {
        this.f678a.setAnalogOutputVoltage(this.f679b, voltage);
    }

    public void setAnalogOutputFrequency(int freq) {
        this.f678a.setAnalogOutputFrequency(this.f679b, freq);
    }

    public void setAnalogOutputMode(byte mode) {
        this.f678a.setAnalogOutputMode(this.f679b, mode);
    }

    public String getDeviceName() {
        return "Analog Output";
    }

    public String getConnectionInfo() {
        return this.f678a.getConnectionInfo() + "; analog port " + this.f679b;
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }
}
