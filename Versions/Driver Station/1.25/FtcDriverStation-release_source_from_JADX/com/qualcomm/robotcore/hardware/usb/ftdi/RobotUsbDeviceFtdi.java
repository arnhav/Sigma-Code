package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;

public class RobotUsbDeviceFtdi implements RobotUsbDevice {
    private FT_Device f698a;

    /* renamed from: com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbDeviceFtdi.1 */
    static /* synthetic */ class C01091 {
        static final /* synthetic */ int[] f482a;

        static {
            f482a = new int[Channel.values().length];
            try {
                f482a[Channel.RX.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f482a[Channel.TX.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f482a[Channel.BOTH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public RobotUsbDeviceFtdi(FT_Device device) {
        this.f698a = device;
    }

    public void setBaudRate(int rate) throws RobotCoreException {
        if (!this.f698a.setBaudRate(rate)) {
            throw new RobotCoreException("FTDI driver failed to set baud rate to " + rate);
        }
    }

    public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity) throws RobotCoreException {
        if (!this.f698a.setDataCharacteristics(dataBits, stopBits, parity)) {
            throw new RobotCoreException("FTDI driver failed to set data characteristics");
        }
    }

    public void setLatencyTimer(int latencyTimer) throws RobotCoreException {
        if (!this.f698a.setLatencyTimer((byte) latencyTimer)) {
            throw new RobotCoreException("FTDI driver failed to set latency timer to " + latencyTimer);
        }
    }

    public void purge(Channel channel) throws RobotCoreException {
        byte b = (byte) 0;
        switch (C01091.f482a[channel.ordinal()]) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                b = (byte) 1;
                break;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                b = (byte) 2;
                break;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                b = (byte) 3;
                break;
        }
        this.f698a.purge(b);
    }

    public void write(byte[] data) throws RobotCoreException {
        this.f698a.write(data);
    }

    public int read(byte[] data) throws RobotCoreException {
        return this.f698a.read(data);
    }

    public int read(byte[] data, int length, int timeout) throws RobotCoreException {
        return this.f698a.read(data, length, (long) timeout);
    }

    public void close() {
        this.f698a.close();
    }
}
