package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.interfaces.Gpio;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;

public class FT_4222_Gpio implements Gpio {
    boolean f620a;
    private FT_4222_Device f621b;
    private FT_Device f622c;

    public FT_4222_Gpio(FT_4222_Device ft4222Device) {
        this.f620a = true;
        this.f621b = ft4222Device;
        this.f622c = this.f621b.mFtDev;
    }

    public int cmdSet(int wValue1, int wValue2) {
        return this.f622c.VendorCmdSet(33, (wValue2 << 8) | wValue1);
    }

    public int cmdSet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.f622c.VendorCmdSet(33, (wValue2 << 8) | wValue1, buf, datalen);
    }

    public int cmdGet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.f622c.VendorCmdGet(32, (wValue2 << 8) | wValue1, buf, datalen);
    }

    public int init(int[] gpio) {
        C0005b c0005b = this.f621b.mChipStatus;
        C0007d c0007d = new C0007d();
        byte[] bArr = new byte[1];
        C0008e c0008e = new C0008e();
        cmdSet(7, 0);
        cmdSet(6, 0);
        int init = this.f621b.init();
        if (init != 0) {
            Log.e("GPIO_M", "FT4222_GPIO init - 1 NG ftStatus:" + init);
            return init;
        } else if (c0005b.f66a == 2 || c0005b.f66a == 3) {
            return FT4222_STATUS.FT4222_GPIO_NOT_SUPPORTED_IN_THIS_MODE;
        } else {
            m381a(c0007d);
            init = c0007d.f82c;
            bArr[0] = c0007d.f83d[0];
            for (int i = 0; i < 4; i++) {
                if (gpio[i] == 1) {
                    init = (byte) ((init | (1 << i)) & 15);
                } else {
                    init = (byte) ((init & ((1 << i) ^ -1)) & 15);
                }
            }
            c0008e.f86c = bArr[0];
            cmdSet(33, init);
            return 0;
        }
    }

    public int read(int portNum, boolean[] bValue) {
        C0007d c0007d = new C0007d();
        int a = m380a(portNum);
        if (a != 0) {
            return a;
        }
        a = m381a(c0007d);
        if (a != 0) {
            return a;
        }
        m382a(portNum, c0007d.f83d[0], bValue);
        return 0;
    }

    public int newRead(int portNum, boolean[] bValue) {
        int a = m380a(portNum);
        if (a != 0) {
            return a;
        }
        a = this.f622c.getQueueStatus();
        if (a <= 0) {
            return -1;
        }
        byte[] bArr = new byte[a];
        this.f622c.read(bArr, a);
        m382a(portNum, bArr[a - 1], bValue);
        return a;
    }

    public int write(int portNum, boolean bValue) {
        C0007d c0007d = new C0007d();
        int a = m380a(portNum);
        if (a != 0) {
            return a;
        }
        if (!m384c(portNum)) {
            return FT4222_STATUS.FT4222_GPIO_WRITE_NOT_SUPPORTED;
        }
        m381a(c0007d);
        byte[] bArr;
        if (bValue) {
            bArr = c0007d.f83d;
            bArr[0] = (byte) (bArr[0] | (1 << portNum));
        } else {
            bArr = c0007d.f83d;
            bArr[0] = (byte) (bArr[0] & (((1 << portNum) ^ -1) & 15));
        }
        return this.f622c.write(c0007d.f83d, 1);
    }

    public int newWrite(int portNum, boolean bValue) {
        boolean z = false;
        C0007d c0007d = new C0007d();
        int a = m380a(portNum);
        if (a != 0) {
            return a;
        }
        if (!m384c(portNum)) {
            return FT4222_STATUS.FT4222_GPIO_WRITE_NOT_SUPPORTED;
        }
        byte[] bArr;
        m381a(c0007d);
        if (bValue) {
            bArr = c0007d.f83d;
            bArr[0] = (byte) (bArr[0] | (1 << portNum));
        } else {
            bArr = c0007d.f83d;
            bArr[0] = (byte) (bArr[0] & (((1 << portNum) ^ -1) & 15));
        }
        if (this.f620a) {
            bArr = c0007d.f83d;
            bArr[0] = (byte) (bArr[0] | 8);
        } else {
            bArr = c0007d.f83d;
            bArr[0] = (byte) (bArr[0] & 7);
        }
        a = this.f622c.write(c0007d.f83d, 1);
        if (!this.f620a) {
            z = true;
        }
        this.f620a = z;
        return a;
    }

    int m380a(int i) {
        C0005b c0005b = this.f621b.mChipStatus;
        if (c0005b.f66a == 2 || c0005b.f66a == 3) {
            return FT4222_STATUS.FT4222_GPIO_NOT_SUPPORTED_IN_THIS_MODE;
        }
        if (i >= 4) {
            return FT4222_STATUS.FT4222_GPIO_EXCEEDED_MAX_PORTNUM;
        }
        return 0;
    }

    int m381a(C0007d c0007d) {
        r2 = new byte[8];
        int cmdGet = cmdGet(32, 0, r2, 8);
        c0007d.f80a.f77a = r2[0];
        c0007d.f80a.f78b = r2[1];
        c0007d.f81b = r2[5];
        c0007d.f82c = r2[6];
        c0007d.f83d[0] = r2[7];
        if (cmdGet == 8) {
            return 0;
        }
        return cmdGet;
    }

    void m382a(int i, byte b, boolean[] zArr) {
        zArr[0] = m385d((((1 << i) & b) >> i) & 1);
    }

    boolean m383b(int i) {
        boolean z = true;
        C0005b c0005b = this.f621b.mChipStatus;
        switch (c0005b.f66a) {
            case SpiSlaveResponseEvent.OK /*0*/:
                if ((i == 0 || i == 1) && (c0005b.f72g == (byte) 1 || c0005b.f72g == (byte) 2)) {
                    z = false;
                }
                if (m385d(c0005b.f74i) && i == 2) {
                    z = false;
                }
                if (m385d(c0005b.f75j) && i == 3) {
                    return false;
                }
                return z;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                if (i == 0 || i == 1) {
                    z = false;
                }
                if (m385d(c0005b.f74i) && i == 2) {
                    z = false;
                }
                if (m385d(c0005b.f75j) && i == 3) {
                    return false;
                }
                return z;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return false;
            default:
                return true;
        }
    }

    boolean m384c(int i) {
        C0007d c0007d = new C0007d();
        boolean b = m383b(i);
        m381a(c0007d);
        if (!b || ((c0007d.f82c >> i) & 1) == 1) {
            return b;
        }
        return false;
    }

    boolean m385d(int i) {
        return i != 0;
    }
}
