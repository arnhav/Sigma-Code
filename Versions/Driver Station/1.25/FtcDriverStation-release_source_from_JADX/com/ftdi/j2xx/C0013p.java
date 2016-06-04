package com.ftdi.j2xx;

import android.util.Log;

/* renamed from: com.ftdi.j2xx.p */
class C0013p implements Runnable {
    int f117a;
    private C0012o f118b;

    C0013p(C0012o c0012o) {
        this.f118b = c0012o;
        this.f117a = this.f118b.m76b().getBufferNumber();
    }

    public void run() {
        int i = 0;
        do {
            try {
                C0011n c = this.f118b.m79c(i);
                if (c.m61b() > 0) {
                    this.f118b.m74a(c);
                    c.m64c();
                }
                this.f118b.m81d(i);
                i = (i + 1) % this.f117a;
            } catch (InterruptedException e) {
                Log.d("ProcessRequestThread::", "Device has been closed.");
                e.printStackTrace();
                return;
            } catch (Exception e2) {
                Log.e("ProcessRequestThread::", "Fatal error!");
                e2.printStackTrace();
                return;
            }
        } while (!Thread.interrupted());
        throw new InterruptedException();
    }
}
