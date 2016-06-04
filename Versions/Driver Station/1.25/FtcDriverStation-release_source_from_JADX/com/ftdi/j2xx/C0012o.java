package com.ftdi.j2xx;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.ftdi.j2xx.D2xxManager.D2xxException;
import com.ftdi.j2xx.D2xxManager.DriverParameters;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.qualcomm.robotcore.robocol.Command;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: com.ftdi.j2xx.o */
class C0012o {
    private Semaphore[] f97a;
    private Semaphore[] f98b;
    private C0011n[] f99c;
    private ByteBuffer f100d;
    private ByteBuffer[] f101e;
    private Pipe f102f;
    private SinkChannel f103g;
    private SourceChannel f104h;
    private int f105i;
    private int f106j;
    private Object f107k;
    private FT_Device f108l;
    private DriverParameters f109m;
    private Lock f110n;
    private Condition f111o;
    private boolean f112p;
    private Lock f113q;
    private Condition f114r;
    private Object f115s;
    private int f116t;

    public C0012o(FT_Device fT_Device) {
        int i = 0;
        this.f108l = fT_Device;
        this.f109m = this.f108l.m29d();
        this.f105i = this.f109m.getBufferNumber();
        int maxBufferSize = this.f109m.getMaxBufferSize();
        this.f116t = this.f108l.m30e();
        this.f97a = new Semaphore[this.f105i];
        this.f98b = new Semaphore[this.f105i];
        this.f99c = new C0011n[this.f105i];
        this.f101e = new ByteBuffer[Command.MAX_COMMAND_LENGTH];
        this.f110n = new ReentrantLock();
        this.f111o = this.f110n.newCondition();
        this.f112p = false;
        this.f113q = new ReentrantLock();
        this.f114r = this.f113q.newCondition();
        this.f107k = new Object();
        this.f115s = new Object();
        m70h();
        this.f100d = ByteBuffer.allocateDirect(maxBufferSize);
        try {
            this.f102f = Pipe.open();
            this.f103g = this.f102f.sink();
            this.f104h = this.f102f.source();
        } catch (IOException e) {
            Log.d("ProcessInCtrl", "Create mMainPipe failed!");
            e.printStackTrace();
        }
        while (i < this.f105i) {
            this.f99c[i] = new C0011n(maxBufferSize);
            this.f98b[i] = new Semaphore(1);
            this.f97a[i] = new Semaphore(1);
            try {
                m79c(i);
            } catch (Exception e2) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + i + " failed!");
                e2.printStackTrace();
            }
            i++;
        }
    }

    boolean m75a() {
        return this.f112p;
    }

    DriverParameters m76b() {
        return this.f109m;
    }

    C0011n m73a(int i) {
        C0011n c0011n = null;
        synchronized (this.f99c) {
            if (i >= 0) {
                if (i < this.f105i) {
                    c0011n = this.f99c[i];
                }
            }
        }
        return c0011n;
    }

    C0011n m77b(int i) throws InterruptedException {
        this.f97a[i].acquire();
        C0011n a = m73a(i);
        if (a.m63c(i) == null) {
            return null;
        }
        return a;
    }

    C0011n m79c(int i) throws InterruptedException {
        this.f98b[i].acquire();
        return m73a(i);
    }

    public void m81d(int i) throws InterruptedException {
        synchronized (this.f99c) {
            this.f99c[i].m66d(i);
        }
        this.f97a[i].release();
    }

    public void m83e(int i) throws InterruptedException {
        this.f98b[i].release();
    }

    public void m74a(C0011n c0011n) throws D2xxException {
        try {
            int b = c0011n.m61b();
            if (b < 2) {
                c0011n.m59a().clear();
                return;
            }
            int d;
            synchronized (this.f115s) {
                d = m80d();
                b -= 2;
                if (d < b) {
                    Log.d("ProcessBulkIn::", " Buffer is full, waiting for read....");
                    m71a(false, (short) 0, (short) 0);
                    this.f110n.lock();
                    this.f112p = true;
                }
            }
            if (d < b) {
                this.f111o.await();
                this.f110n.unlock();
            }
            m67b(c0011n);
        } catch (InterruptedException e) {
            this.f110n.unlock();
            Log.e("ProcessInCtrl", "Exception in Full await!");
            e.printStackTrace();
        } catch (Exception e2) {
            Log.e("ProcessInCtrl", "Exception in ProcessBulkIN");
            e2.printStackTrace();
            throw new D2xxException("Fatal error in BulkIn.");
        }
    }

    private void m67b(C0011n c0011n) throws InterruptedException {
        boolean z = true;
        ByteBuffer a = c0011n.m59a();
        int b = c0011n.m61b();
        if (b > 0) {
            int i = (b / this.f116t) + (b % this.f116t > 0 ? 1 : 0);
            int i2 = 0;
            short s = (short) 0;
            short s2 = (short) 0;
            int i3 = 0;
            while (i2 < i) {
                int i4;
                int i5;
                if (i2 == i - 1) {
                    a.limit(b);
                    int i6 = this.f116t * i2;
                    a.position(i6);
                    byte b2 = a.get();
                    s2 = (short) (this.f108l.f20g.modemStatus ^ ((short) (b2 & 240)));
                    this.f108l.f20g.modemStatus = (short) (b2 & 240);
                    this.f108l.f20g.lineStatus = (short) (a.get() & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
                    i4 = i6 + 2;
                    if (a.hasRemaining()) {
                        s = (short) (this.f108l.f20g.lineStatus & 30);
                        i5 = i4;
                        i4 = b;
                    } else {
                        s = (short) 0;
                        i5 = i4;
                        i4 = b;
                    }
                } else {
                    i4 = (i2 + 1) * this.f116t;
                    a.limit(i4);
                    i5 = (this.f116t * i2) + 2;
                    a.position(i5);
                }
                i5 = i3 + (i4 - i5);
                this.f101e[i2] = a.slice();
                i2++;
                i3 = i5;
            }
            if (i3 != 0) {
                try {
                    long write = this.f103g.write(this.f101e, 0, i);
                    if (write != ((long) i3)) {
                        Log.d("extractReadData::", "written != totalData, written= " + write + " totalData=" + i3);
                    }
                    m68f((int) write);
                    this.f113q.lock();
                    this.f114r.signalAll();
                    this.f113q.unlock();
                } catch (Exception e) {
                    Log.d("extractReadData::", "Write data to sink failed!!");
                    e.printStackTrace();
                }
            } else {
                z = false;
            }
            a.clear();
            m71a(z, s2, s);
        }
    }

    public int m72a(byte[] bArr, int i, long j) {
        this.f109m.getMaxBufferSize();
        long currentTimeMillis = System.currentTimeMillis();
        ByteBuffer wrap = ByteBuffer.wrap(bArr, 0, i);
        if (j == 0) {
            j = (long) this.f109m.getReadTimeout();
        }
        while (this.f108l.isOpen()) {
            if (m78c() >= i) {
                synchronized (this.f104h) {
                    try {
                        this.f104h.read(wrap);
                        m69g(i);
                    } catch (Exception e) {
                        Log.d("readBulkInData::", "Cannot read data from Source!!");
                        e.printStackTrace();
                    }
                }
                synchronized (this.f115s) {
                    if (this.f112p) {
                        Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                        this.f110n.lock();
                        this.f111o.signalAll();
                        this.f112p = false;
                        this.f110n.unlock();
                    }
                }
                return i;
            }
            try {
                this.f113q.lock();
                this.f114r.await(System.currentTimeMillis() - currentTimeMillis, TimeUnit.MILLISECONDS);
                this.f113q.unlock();
            } catch (InterruptedException e2) {
                Log.d("readBulkInData::", "Cannot wait to read data!!");
                e2.printStackTrace();
                this.f113q.unlock();
            }
            if (System.currentTimeMillis() - currentTimeMillis >= j) {
                return 0;
            }
        }
        return 0;
    }

    private int m68f(int i) {
        int i2;
        synchronized (this.f107k) {
            this.f106j += i;
            i2 = this.f106j;
        }
        return i2;
    }

    private int m69g(int i) {
        int i2;
        synchronized (this.f107k) {
            this.f106j -= i;
            i2 = this.f106j;
        }
        return i2;
    }

    private void m70h() {
        synchronized (this.f107k) {
            this.f106j = 0;
        }
    }

    public int m78c() {
        int i;
        synchronized (this.f107k) {
            i = this.f106j;
        }
        return i;
    }

    public int m80d() {
        return (this.f109m.getMaxBufferSize() - m78c()) - 1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int m82e() {
        /*
        r7 = this;
        r1 = 0;
        r0 = r7.f109m;
        r2 = r0.getBufferNumber();
        r3 = r7.f100d;
        monitor-enter(r3);
    L_0x000a:
        r0 = r7.f104h;	 Catch:{ Exception -> 0x0027 }
        r4 = 0;
        r0.configureBlocking(r4);	 Catch:{ Exception -> 0x0027 }
        r0 = r7.f104h;	 Catch:{ Exception -> 0x0027 }
        r4 = r7.f100d;	 Catch:{ Exception -> 0x0027 }
        r0 = r0.read(r4);	 Catch:{ Exception -> 0x0027 }
        r4 = r7.f100d;	 Catch:{ Exception -> 0x0027 }
        r4.clear();	 Catch:{ Exception -> 0x0027 }
        if (r0 != 0) goto L_0x000a;
    L_0x001f:
        r7.m70h();	 Catch:{ all -> 0x002c }
        r0 = r1;
    L_0x0023:
        if (r0 < r2) goto L_0x002f;
    L_0x0025:
        monitor-exit(r3);	 Catch:{ all -> 0x002c }
        return r1;
    L_0x0027:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x002c }
        goto L_0x001f;
    L_0x002c:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x002c }
        throw r0;
    L_0x002f:
        r4 = r7.m73a(r0);	 Catch:{ all -> 0x002c }
        r5 = r4.m65d();	 Catch:{ all -> 0x002c }
        if (r5 == 0) goto L_0x0043;
    L_0x0039:
        r5 = r4.m61b();	 Catch:{ all -> 0x002c }
        r6 = 2;
        if (r5 <= r6) goto L_0x0043;
    L_0x0040:
        r4.m64c();	 Catch:{ all -> 0x002c }
    L_0x0043:
        r0 = r0 + 1;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ftdi.j2xx.o.e():int");
    }

    public int m71a(boolean z, short s, short s2) throws InterruptedException {
        C0015q c0015q = new C0015q();
        c0015q.f140a = this.f108l.f22i.f140a;
        if (z && (c0015q.f140a & 1) != 0 && (this.f108l.f14a ^ 1) == 1) {
            FT_Device fT_Device = this.f108l;
            fT_Device.f14a |= 1;
            Intent intent = new Intent("FT_EVENT_RXCHAR");
            intent.putExtra("message", "FT_EVENT_RXCHAR");
            LocalBroadcastManager.getInstance(this.f108l.f23j).sendBroadcast(intent);
        }
        if (!(s == (short) 0 || (c0015q.f140a & 2) == 0 || (this.f108l.f14a ^ 2) != 2)) {
            fT_Device = this.f108l;
            fT_Device.f14a |= 2;
            intent = new Intent("FT_EVENT_MODEM_STATUS");
            intent.putExtra("message", "FT_EVENT_MODEM_STATUS");
            LocalBroadcastManager.getInstance(this.f108l.f23j).sendBroadcast(intent);
        }
        if (!(s2 == (short) 0 || (c0015q.f140a & 4) == 0 || (this.f108l.f14a ^ 4) != 4)) {
            FT_Device fT_Device2 = this.f108l;
            fT_Device2.f14a |= 4;
            Intent intent2 = new Intent("FT_EVENT_LINE_STATUS");
            intent2.putExtra("message", "FT_EVENT_LINE_STATUS");
            LocalBroadcastManager.getInstance(this.f108l.f23j).sendBroadcast(intent2);
        }
        return 0;
    }

    public void m84f() throws InterruptedException {
        int bufferNumber = this.f109m.getBufferNumber();
        for (int i = 0; i < bufferNumber; i++) {
            if (m73a(i).m65d()) {
                m81d(i);
            }
        }
    }

    void m85g() {
        int i = 0;
        for (int i2 = 0; i2 < this.f105i; i2++) {
            try {
                m83e(i2);
            } catch (Exception e) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + i2 + " failed!");
                e.printStackTrace();
            }
            this.f99c[i2] = null;
            this.f98b[i2] = null;
            this.f97a[i2] = null;
        }
        while (i < Command.MAX_COMMAND_LENGTH) {
            this.f101e[i] = null;
            i++;
        }
        this.f97a = null;
        this.f98b = null;
        this.f99c = null;
        this.f101e = null;
        this.f100d = null;
        if (this.f112p) {
            this.f110n.lock();
            this.f111o.signalAll();
            this.f110n.unlock();
        }
        this.f113q.lock();
        this.f114r.signalAll();
        this.f113q.unlock();
        this.f110n = null;
        this.f111o = null;
        this.f107k = null;
        this.f113q = null;
        this.f114r = null;
        try {
            this.f103g.close();
            this.f103g = null;
            this.f104h.close();
            this.f104h = null;
            this.f102f = null;
        } catch (IOException e2) {
            Log.d("ProcessInCtrl", "Close mMainPipe failed!");
            e2.printStackTrace();
        }
        this.f108l = null;
        this.f109m = null;
    }
}
