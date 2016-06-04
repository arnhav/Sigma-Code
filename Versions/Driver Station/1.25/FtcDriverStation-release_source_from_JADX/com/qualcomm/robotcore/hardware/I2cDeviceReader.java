package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;

public class I2cDeviceReader {
    private final I2cDevice f442a;

    /* renamed from: com.qualcomm.robotcore.hardware.I2cDeviceReader.1 */
    class C01471 implements I2cPortReadyCallback {
        final /* synthetic */ I2cDeviceReader f691a;

        C01471(I2cDeviceReader i2cDeviceReader) {
            this.f691a = i2cDeviceReader;
        }

        public void portIsReady(int port) {
            this.f691a.m291a();
        }
    }

    public I2cDeviceReader(I2cDevice i2cDevice, int i2cAddress, int memAddress, int length) {
        this.f442a = i2cDevice;
        i2cDevice.enableI2cReadMode(i2cAddress, memAddress, length);
        i2cDevice.setI2cPortActionFlag();
        i2cDevice.writeI2cCacheToModule();
        i2cDevice.registerForI2cPortReadyCallback(new C01471(this));
    }

    public byte[] getReadBuffer() {
        return this.f442a.getCopyOfReadBuffer();
    }

    private void m291a() {
        this.f442a.setI2cPortActionFlag();
        this.f442a.readI2cCacheFromModule();
        this.f442a.writeI2cPortFlagOnlyToModule();
    }
}
