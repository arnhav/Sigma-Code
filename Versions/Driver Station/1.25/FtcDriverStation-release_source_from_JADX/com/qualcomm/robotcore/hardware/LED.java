package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.DigitalChannelController.Mode;

public class LED implements HardwareDevice {
    private DigitalChannelController f692a;
    private int f693b;

    public LED(DigitalChannelController controller, int physicalPort) {
        this.f692a = null;
        this.f693b = -1;
        this.f692a = controller;
        this.f693b = physicalPort;
        controller.setDigitalChannelMode(physicalPort, Mode.OUTPUT);
    }

    public void enable(boolean set) {
        this.f692a.setDigitalChannelState(this.f693b, set);
    }

    public String getDeviceName() {
        return null;
    }

    public String getConnectionInfo() {
        return null;
    }

    public int getVersion() {
        return 0;
    }

    public void close() {
    }
}
