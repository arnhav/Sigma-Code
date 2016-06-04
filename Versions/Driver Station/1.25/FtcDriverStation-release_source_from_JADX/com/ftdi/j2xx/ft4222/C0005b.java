package com.ftdi.j2xx.ft4222;

/* renamed from: com.ftdi.j2xx.ft4222.b */
class C0005b {
    byte f66a;
    byte f67b;
    byte f68c;
    byte f69d;
    byte f70e;
    byte f71f;
    byte f72g;
    byte f73h;
    byte f74i;
    byte f75j;
    byte[] f76k;

    public C0005b() {
        this.f76k = new byte[3];
    }

    void m40a(byte[] bArr) {
        this.f66a = bArr[0];
        this.f67b = bArr[1];
        this.f68c = bArr[2];
        this.f69d = bArr[3];
        this.f70e = bArr[4];
        this.f71f = bArr[5];
        this.f72g = bArr[6];
        this.f73h = bArr[7];
        this.f74i = bArr[8];
        this.f75j = bArr[9];
        this.f76k[0] = bArr[10];
        this.f76k[1] = bArr[11];
        this.f76k[2] = bArr[12];
    }
}
