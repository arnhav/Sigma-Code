package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.DigitalChannelController.Mode;

public class DigitalChannel implements HardwareDevice {
    private DigitalChannelController f680a;
    private int f681b;

    public DigitalChannel(DigitalChannelController controller, int channel) {
        this.f680a = null;
        this.f681b = -1;
        this.f680a = controller;
        this.f681b = channel;
    }

    public Mode getMode() {
        return this.f680a.getDigitalChannelMode(this.f681b);
    }

    public void setMode(Mode mode) {
        this.f680a.setDigitalChannelMode(this.f681b, mode);
    }

    public boolean getState() {
        return this.f680a.getDigitalChannelState(this.f681b);
    }

    public void setState(boolean state) {
        this.f680a.setDigitalChannelState(this.f681b, state);
    }

    public String getDeviceName() {
        return "Digital Channel";
    }

    public String getConnectionInfo() {
        return this.f680a.getConnectionInfo() + "; digital port " + this.f681b;
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }
}
