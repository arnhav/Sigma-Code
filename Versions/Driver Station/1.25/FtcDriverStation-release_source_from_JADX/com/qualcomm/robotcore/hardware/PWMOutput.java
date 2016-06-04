package com.qualcomm.robotcore.hardware;

public class PWMOutput implements HardwareDevice {
    private PWMOutputController f694a;
    private int f695b;

    public PWMOutput(PWMOutputController controller, int port) {
        this.f694a = null;
        this.f695b = -1;
        this.f694a = controller;
        this.f695b = port;
    }

    public void setPulseWidthOutputTime(int time) {
        this.f694a.setPulseWidthOutputTime(this.f695b, time);
    }

    public int getPulseWidthOutputTime() {
        return this.f694a.getPulseWidthOutputTime(this.f695b);
    }

    public void setPulseWidthPeriod(int period) {
        this.f694a.setPulseWidthPeriod(this.f695b, period);
    }

    public int getPulseWidthPeriod() {
        return this.f694a.getPulseWidthPeriod(this.f695b);
    }

    public String getDeviceName() {
        return "PWM Output";
    }

    public String getConnectionInfo() {
        return this.f694a.getConnectionInfo() + "; port " + this.f695b;
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }
}
