package com.ftdi.j2xx;

import java.nio.ByteBuffer;

/* renamed from: com.ftdi.j2xx.n */
class C0011n {
    private int f93a;
    private ByteBuffer f94b;
    private int f95c;
    private boolean f96d;

    public C0011n(int i) {
        this.f94b = ByteBuffer.allocate(i);
        m62b(0);
    }

    void m60a(int i) {
        this.f93a = i;
    }

    ByteBuffer m59a() {
        return this.f94b;
    }

    int m61b() {
        return this.f95c;
    }

    void m62b(int i) {
        this.f95c = i;
    }

    synchronized void m64c() {
        this.f94b.clear();
        m62b(0);
    }

    synchronized boolean m65d() {
        return this.f96d;
    }

    synchronized ByteBuffer m63c(int i) {
        ByteBuffer byteBuffer;
        byteBuffer = null;
        if (!this.f96d) {
            this.f96d = true;
            this.f93a = i;
            byteBuffer = this.f94b;
        }
        return byteBuffer;
    }

    synchronized boolean m66d(int i) {
        boolean z = false;
        synchronized (this) {
            if (this.f96d && i == this.f93a) {
                this.f96d = false;
                z = true;
            }
        }
        return z;
    }
}
