package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.CHIPTOP_CMD;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.interfaces.SpiSlave;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FT_4222_Spi_Slave implements SpiSlave {
    private FT_4222_Device f630a;
    private FT_Device f631b;
    private Lock f632c;

    public FT_4222_Spi_Slave(FT_4222_Device pDevice) {
        this.f630a = pDevice;
        this.f631b = pDevice.mFtDev;
        this.f632c = new ReentrantLock();
    }

    public int init() {
        int i = 0;
        C0005b c0005b = this.f630a.mChipStatus;
        C0004a c0004a = this.f630a.mSpiMasterCfg;
        c0004a.f61a = 1;
        c0004a.f62b = 2;
        c0004a.f63c = 0;
        c0004a.f64d = 0;
        c0004a.f65e = (byte) 1;
        this.f632c.lock();
        this.f630a.cleanRxData();
        if (this.f631b.VendorCmdSet(33, (c0004a.f61a << 8) | 66) < 0) {
            i = 4;
        }
        if (this.f631b.VendorCmdSet(33, (c0004a.f62b << 8) | 68) < 0) {
            i = 4;
        }
        if (this.f631b.VendorCmdSet(33, (c0004a.f63c << 8) | 69) < 0) {
            i = 4;
        }
        if (this.f631b.VendorCmdSet(33, (c0004a.f64d << 8) | 70) < 0) {
            i = 4;
        }
        if (this.f631b.VendorCmdSet(33, 67) < 0) {
            i = 4;
        }
        if (this.f631b.VendorCmdSet(33, (c0004a.f65e << 8) | 72) < 0) {
            i = 4;
        }
        if (this.f631b.VendorCmdSet(33, 1029) < 0) {
            i = 4;
        }
        this.f632c.unlock();
        c0005b.f72g = (byte) 4;
        return i;
    }

    public int getRxStatus(int[] pRxSize) {
        if (pRxSize == null) {
            return FT4222_STATUS.FT4222_INVALID_POINTER;
        }
        int a = m397a();
        if (a != 0) {
            return a;
        }
        this.f632c.lock();
        a = this.f631b.getQueueStatus();
        this.f632c.unlock();
        if (a >= 0) {
            pRxSize[0] = a;
            return 0;
        }
        pRxSize[0] = -1;
        return 4;
    }

    public int read(byte[] buffer, int bufferSize, int[] sizeOfRead) {
        this.f632c.lock();
        if (this.f631b == null || !this.f631b.isOpen()) {
            this.f632c.unlock();
            return 3;
        }
        int read = this.f631b.read(buffer, bufferSize);
        this.f632c.unlock();
        sizeOfRead[0] = read;
        if (read < 0) {
            return 4;
        }
        return 0;
    }

    public int write(byte[] buffer, int bufferSize, int[] sizeTransferred) {
        if (sizeTransferred == null || buffer == null) {
            return FT4222_STATUS.FT4222_INVALID_POINTER;
        }
        int a = m397a();
        if (a != 0) {
            return a;
        }
        if (bufferSize > 512) {
            return FT4222_STATUS.FT4222_EXCEEDED_MAX_TRANSFER_SIZE;
        }
        this.f632c.lock();
        sizeTransferred[0] = this.f631b.write(buffer, bufferSize);
        this.f632c.unlock();
        if (sizeTransferred[0] == bufferSize) {
            return a;
        }
        Log.e("FTDI_Device::", "Error write =" + bufferSize + " tx=" + sizeTransferred[0]);
        return 4;
    }

    private int m397a() {
        if (this.f630a.mChipStatus.f72g != 4) {
            return FT4222_STATUS.FT4222_IS_NOT_SPI_MODE;
        }
        return 0;
    }

    public int reset() {
        int i = 0;
        this.f632c.lock();
        if (this.f631b.VendorCmdSet(33, 74) < 0) {
            i = 4;
        }
        this.f632c.unlock();
        return i;
    }

    public int setDrivingStrength(int clkStrength, int ioStrength, int ssoStregth) {
        int i = 3;
        int i2 = 4;
        int i3 = 0;
        C0005b c0005b = this.f630a.mChipStatus;
        if (c0005b.f72g != (byte) 3 && c0005b.f72g != (byte) 4) {
            return FT4222_STATUS.FT4222_IS_NOT_SPI_MODE;
        }
        int i4 = ((clkStrength << 4) | (ioStrength << 2)) | ssoStregth;
        if (c0005b.f72g != (byte) 3) {
            i = 4;
        }
        this.f632c.lock();
        if (this.f631b.VendorCmdSet(33, (i4 << 8) | CHIPTOP_CMD.CHIPTOP_SET_DS_CTL0_REG) < 0) {
            i3 = 4;
        }
        if (this.f631b.VendorCmdSet(33, (i << 8) | 5) >= 0) {
            i2 = i3;
        }
        this.f632c.unlock();
        return i2;
    }
}
