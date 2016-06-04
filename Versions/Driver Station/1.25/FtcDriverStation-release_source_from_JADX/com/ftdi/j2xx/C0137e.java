package com.ftdi.j2xx;

import com.ftdi.j2xx.ft4222.FT_4222_Defines;

/* renamed from: com.ftdi.j2xx.e */
class C0137e extends C0009k {
    C0137e(FT_Device fT_Device) {
        super(fT_Device);
    }

    short m372a(FT_EEPROM ft_eeprom) {
        int[] iArr = new int[64];
        if (ft_eeprom.getClass() != FT_EEPROM.class) {
            return (short) 1;
        }
        try {
            iArr[1] = ft_eeprom.VendorId;
            iArr[2] = ft_eeprom.ProductId;
            iArr[3] = 512;
            iArr[4] = m42a((Object) ft_eeprom);
            int a = m43a(ft_eeprom.Product, iArr, m43a(ft_eeprom.Manufacturer, iArr, 10, 7, true) + (ft_eeprom.Manufacturer.length() + 2), 8, true) + (ft_eeprom.Product.length() + 2);
            if (ft_eeprom.SerNumEnable) {
                int a2 = m43a(ft_eeprom.SerialNumber, iArr, a, 9, true) + (ft_eeprom.SerialNumber.length() + 2);
            }
            if (iArr[1] == 0 || iArr[2] == 0) {
                return (short) 2;
            }
            if (m52a(iArr, 63)) {
                return (short) 0;
            }
            return (short) 1;
        } catch (Exception e) {
            e.printStackTrace();
            return (short) 0;
        }
    }

    FT_EEPROM m371a() {
        int i;
        FT_EEPROM ft_eeprom = new FT_EEPROM();
        int[] iArr = new int[64];
        for (i = 0; i < 64; i++) {
            iArr[i] = m44a((short) i);
        }
        try {
            ft_eeprom.VendorId = (short) iArr[1];
            ft_eeprom.ProductId = (short) iArr[2];
            m49a(ft_eeprom, iArr[4]);
            ft_eeprom.Manufacturer = m47a(10, iArr);
            i = 10 + (ft_eeprom.Manufacturer.length() + 1);
            ft_eeprom.Product = m47a(i, iArr);
            ft_eeprom.SerialNumber = m47a(i + (ft_eeprom.Product.length() + 1), iArr);
            return ft_eeprom;
        } catch (Exception e) {
            return null;
        }
    }

    int m374b() {
        return (((63 - ((((((m44a((short) 7) & 65280) >> 8) / 2) + 10) + (((m44a((short) 8) & 65280) >> 8) / 2)) + 1)) - 1) - (((m44a((short) 9) & 65280) >> 8) / 2)) * 2;
    }

    int m370a(byte[] bArr) {
        if (bArr.length > m374b()) {
            return 0;
        }
        int[] iArr = new int[64];
        for (short s = (short) 0; s < (short) 64; s = (short) (s + 1)) {
            iArr[s] = m44a(s);
        }
        int b = (short) (((short) ((63 - (m374b() / 2)) - 1)) & 65535);
        int i = 0;
        while (i < bArr.length) {
            int i2;
            if (i + 1 < bArr.length) {
                i2 = bArr[i + 1] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            } else {
                i2 = 0;
            }
            int i3 = (bArr[i] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) | (i2 << 8);
            short s2 = (short) (b + 1);
            iArr[b] = i3;
            i += 2;
            short s3 = s2;
        }
        if (iArr[1] == 0 || iArr[2] == 0 || !m52a(iArr, 63)) {
            return 0;
        }
        return bArr.length;
    }

    byte[] m373a(int i) {
        byte[] bArr = new byte[i];
        if (i == 0 || i > m374b() - 1) {
            return null;
        }
        short b = (short) (((short) ((63 - (m374b() / 2)) - 1)) & 65535);
        int i2 = 0;
        while (i2 < i) {
            short s = (short) (b + 1);
            int a = m44a(b);
            if (i2 + 1 < bArr.length) {
                bArr[i2 + 1] = (byte) (a & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            }
            bArr[i2] = (byte) ((a & 65280) >> 8);
            i2 += 2;
            b = s;
        }
        return bArr;
    }
}
