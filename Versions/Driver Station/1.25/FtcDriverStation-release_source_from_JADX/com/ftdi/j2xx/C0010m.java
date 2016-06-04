package com.ftdi.j2xx;

/* renamed from: com.ftdi.j2xx.m */
class C0010m {
    private int f91a;
    private int f92b;

    C0010m(int i, int i2) {
        this.f91a = i;
        this.f92b = i2;
    }

    C0010m() {
        this.f91a = 0;
        this.f92b = 0;
    }

    public int m57a() {
        return this.f91a;
    }

    public int m58b() {
        return this.f92b;
    }

    public String toString() {
        return "Vendor: " + String.format("%04x", new Object[]{Integer.valueOf(this.f91a)}) + ", Product: " + String.format("%04x", new Object[]{Integer.valueOf(this.f92b)});
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof C0010m)) {
            return false;
        }
        C0010m c0010m = (C0010m) o;
        if (this.f91a != c0010m.f91a) {
            return false;
        }
        if (this.f92b != c0010m.f92b) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
