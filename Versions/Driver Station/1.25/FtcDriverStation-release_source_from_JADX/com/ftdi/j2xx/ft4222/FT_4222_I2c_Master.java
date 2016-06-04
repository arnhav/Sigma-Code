package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.CHIPTOP_CMD;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.SPI_SLAVE_CMD;
import com.ftdi.j2xx.interfaces.I2cMaster;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.RobocolConfig;

public class FT_4222_I2c_Master implements I2cMaster {
    FT_4222_Device f623a;
    FT_Device f624b;
    int f625c;

    public FT_4222_I2c_Master(FT_4222_Device ft4222Device) {
        this.f623a = ft4222Device;
        this.f624b = this.f623a.mFtDev;
    }

    public int cmdSet(int wValue1, int wValue2) {
        return this.f624b.VendorCmdSet(33, (wValue2 << 8) | wValue1);
    }

    public int cmdSet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.f624b.VendorCmdSet(33, (wValue2 << 8) | wValue1, buf, datalen);
    }

    public int cmdGet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.f624b.VendorCmdGet(32, (wValue2 << 8) | wValue1, buf, datalen);
    }

    public int init(int kbps) {
        byte[] bArr = new byte[1];
        int init = this.f623a.init();
        if (init != 0) {
            return init;
        }
        if (!m390a()) {
            return FT4222_STATUS.FT4222_I2C_NOT_SUPPORTED_IN_THIS_MODE;
        }
        cmdSet(81, 0);
        init = this.f623a.getClock(bArr);
        if (init != 0) {
            return init;
        }
        int a = m386a(bArr[0], kbps);
        init = cmdSet(5, 1);
        if (init < 0) {
            return init;
        }
        this.f623a.mChipStatus.f72g = (byte) 1;
        init = cmdSet(82, a);
        if (init < 0) {
            return init;
        }
        this.f625c = kbps;
        return 0;
    }

    public int reset() {
        int a = m388a(true);
        return a != 0 ? a : cmdSet(81, 1);
    }

    public int read(int deviceAddress, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
        short s = (short) (65535 & deviceAddress);
        short s2 = (short) ((deviceAddress & 896) >> 7);
        short s3 = (short) sizeToTransfer;
        int[] iArr = new int[1];
        byte[] bArr = new byte[4];
        long currentTimeMillis = System.currentTimeMillis();
        int readTimeout = this.f624b.getReadTimeout();
        int a = m387a(deviceAddress);
        if (a != 0) {
            return a;
        }
        if (sizeToTransfer < 1) {
            return 6;
        }
        a = m388a(true);
        if (a != 0) {
            return a;
        }
        a = m389a(iArr);
        if (a != 0) {
            return a;
        }
        if (sizeToTransfer > iArr[0]) {
            return FT4222_STATUS.FT4222_EXCEEDED_MAX_TRANSFER_SIZE;
        }
        sizeTransferred[0] = 0;
        bArr[0] = (byte) ((short) ((s << 1) + 1));
        bArr[1] = (byte) s2;
        bArr[2] = (byte) ((s3 >> 8) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        bArr[3] = (byte) (s3 & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        if (4 != this.f624b.write(bArr, 4)) {
            return FT4222_STATUS.FT4222_FAILED_TO_READ_DEVICE;
        }
        a = this.f624b.getQueueStatus();
        while (a < sizeToTransfer && System.currentTimeMillis() - currentTimeMillis < ((long) readTimeout)) {
            a = this.f624b.getQueueStatus();
        }
        if (a <= sizeToTransfer) {
            sizeToTransfer = a;
        }
        a = this.f624b.read(buffer, sizeToTransfer);
        sizeTransferred[0] = a;
        if (a >= 0) {
            return 0;
        }
        return FT4222_STATUS.FT4222_FAILED_TO_READ_DEVICE;
    }

    public int write(int deviceAddress, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
        short s = (short) deviceAddress;
        short s2 = (short) ((deviceAddress & 896) >> 7);
        short s3 = (short) sizeToTransfer;
        byte[] bArr = new byte[(sizeToTransfer + 4)];
        int[] iArr = new int[1];
        int a = m387a(deviceAddress);
        if (a != 0) {
            return a;
        }
        if (sizeToTransfer < 1) {
            return 6;
        }
        a = m388a(true);
        if (a != 0) {
            return a;
        }
        a = m389a(iArr);
        if (a != 0) {
            return a;
        }
        if (sizeToTransfer > iArr[0]) {
            return FT4222_STATUS.FT4222_EXCEEDED_MAX_TRANSFER_SIZE;
        }
        sizeTransferred[0] = 0;
        bArr[0] = (byte) ((short) (s << 1));
        bArr[1] = (byte) s2;
        bArr[2] = (byte) ((s3 >> 8) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        bArr[3] = (byte) (s3 & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        for (a = 0; a < sizeToTransfer; a++) {
            bArr[a + 4] = buffer[a];
        }
        sizeTransferred[0] = this.f624b.write(bArr, sizeToTransfer + 4) - 4;
        if (sizeToTransfer == sizeTransferred[0]) {
            return 0;
        }
        return 10;
    }

    boolean m390a() {
        if (this.f623a.mChipStatus.f66a == null || this.f623a.mChipStatus.f66a == 3) {
            return true;
        }
        return false;
    }

    int m388a(boolean z) {
        if (z) {
            if (this.f623a.mChipStatus.f72g != 1) {
                return FT4222_STATUS.FT4222_IS_NOT_I2C_MODE;
            }
        } else if (this.f623a.mChipStatus.f72g != 2) {
            return FT4222_STATUS.FT4222_IS_NOT_I2C_MODE;
        }
        return 0;
    }

    int m387a(int i) {
        if ((64512 & i) > 0) {
            return FT4222_STATUS.FT4222_WRONG_I2C_ADDR;
        }
        return 0;
    }

    private int m386a(int i, int i2) {
        double d;
        switch (i) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                d = 41.666666666666664d;
                break;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                d = 20.833333333333332d;
                break;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                d = 12.5d;
                break;
            default:
                d = 16.666666666666668d;
                break;
        }
        if (60 <= i2 && i2 <= 100) {
            int i3 = (int) ((((1000000.0d / ((double) i2)) / (d * 8.0d)) - Servo.MAX_POSITION) + 0.5d);
            if (i3 > 127) {
                return 127;
            }
            return i3;
        } else if (100 < i2 && i2 <= 400) {
            return ((int) ((((1000000.0d / ((double) i2)) / (d * 6.0d)) - Servo.MAX_POSITION) + 0.5d)) | CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE;
        } else {
            if (400 < i2 && i2 <= RobocolConfig.TIMEOUT) {
                return ((int) ((((1000000.0d / ((double) i2)) / (d * 6.0d)) - Servo.MAX_POSITION) + 0.5d)) | CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE;
            }
            if (RobocolConfig.TIMEOUT >= i2 || i2 > 3400) {
                return 74;
            }
            return (((int) ((((1000000.0d / ((double) i2)) / (d * 6.0d)) - Servo.MAX_POSITION) + 0.5d)) | SPI_SLAVE_CMD.SPI_MASTER_TRANSFER) & -65;
        }
    }

    int m389a(int[] iArr) {
        iArr[0] = 0;
        int maxBuckSize = this.f623a.getMaxBuckSize();
        switch (this.f623a.mChipStatus.f72g) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                iArr[0] = maxBuckSize - 4;
                return 0;
            default:
                return 17;
        }
    }
}
