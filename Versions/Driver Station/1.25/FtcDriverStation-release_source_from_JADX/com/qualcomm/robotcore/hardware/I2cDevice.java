package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import com.qualcomm.robotcore.hardware.configuration.XMLConfigurationConstants;
import java.util.concurrent.locks.Lock;

public class I2cDevice implements HardwareDevice {
    private I2cController f689a;
    private int f690b;

    public I2cDevice(I2cController controller, int port) {
        this.f689a = null;
        this.f690b = -1;
        this.f689a = controller;
        this.f690b = port;
    }

    public void enableI2cReadMode(int i2cAddress, int memAddress, int length) {
        this.f689a.enableI2cReadMode(this.f690b, i2cAddress, memAddress, length);
    }

    public void enableI2cWriteMode(int i2cAddress, int memAddress, int length) {
        this.f689a.enableI2cWriteMode(this.f690b, i2cAddress, memAddress, length);
    }

    public byte[] getCopyOfReadBuffer() {
        return this.f689a.getCopyOfReadBuffer(this.f690b);
    }

    public byte[] getCopyOfWriteBuffer() {
        return this.f689a.getCopyOfWriteBuffer(this.f690b);
    }

    public void copyBufferIntoWriteBuffer(byte[] buffer) {
        this.f689a.copyBufferIntoWriteBuffer(this.f690b, buffer);
    }

    public void setI2cPortActionFlag() {
        this.f689a.setI2cPortActionFlag(this.f690b);
    }

    public boolean isI2cPortActionFlagSet() {
        return this.f689a.isI2cPortActionFlagSet(this.f690b);
    }

    public void readI2cCacheFromController() {
        this.f689a.readI2cCacheFromController(this.f690b);
    }

    public void writeI2cCacheToController() {
        this.f689a.writeI2cCacheToController(this.f690b);
    }

    public void writeI2cPortFlagOnlyToController() {
        this.f689a.writeI2cPortFlagOnlyToController(this.f690b);
    }

    public boolean isI2cPortInReadMode() {
        return this.f689a.isI2cPortInReadMode(this.f690b);
    }

    public boolean isI2cPortInWriteMode() {
        return this.f689a.isI2cPortInWriteMode(this.f690b);
    }

    public boolean isI2cPortReady() {
        return this.f689a.isI2cPortReady(this.f690b);
    }

    public Lock getI2cReadCacheLock() {
        return this.f689a.getI2cReadCacheLock(this.f690b);
    }

    public Lock getI2cWriteCacheLock() {
        return this.f689a.getI2cWriteCacheLock(this.f690b);
    }

    public byte[] getI2cReadCache() {
        return this.f689a.getI2cReadCache(this.f690b);
    }

    public byte[] getI2cWriteCache() {
        return this.f689a.getI2cWriteCache(this.f690b);
    }

    public void registerForI2cPortReadyCallback(I2cPortReadyCallback callback) {
        this.f689a.registerForI2cPortReadyCallback(callback, this.f690b);
    }

    public void deregisterForPortReadyCallback() {
        this.f689a.deregisterForPortReadyCallback(this.f690b);
    }

    public String getDeviceName() {
        return XMLConfigurationConstants.I2C_DEVICE;
    }

    public String getConnectionInfo() {
        return this.f689a.getConnectionInfo() + "; port " + this.f690b;
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }

    @Deprecated
    public void readI2cCacheFromModule() {
        readI2cCacheFromController();
    }

    @Deprecated
    public void writeI2cCacheToModule() {
        writeI2cCacheToController();
    }

    @Deprecated
    public void writeI2cPortFlagOnlyToModule() {
        writeI2cPortFlagOnlyToController();
    }
}
