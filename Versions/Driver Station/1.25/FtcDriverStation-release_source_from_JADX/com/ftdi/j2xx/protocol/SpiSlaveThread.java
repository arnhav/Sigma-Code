package com.ftdi.j2xx.protocol;

import android.util.Log;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SpiSlaveThread extends Thread {
    public static final int THREAD_DESTORYED = 2;
    public static final int THREAD_INIT = 0;
    public static final int THREAD_RUNNING = 1;
    private Queue<SpiSlaveEvent> f133a;
    private Lock f134b;
    private Object f135c;
    private Object f136d;
    private boolean f137e;
    private boolean f138f;
    private int f139g;

    protected abstract boolean isTerminateEvent(SpiSlaveEvent spiSlaveEvent);

    protected abstract boolean pollData();

    protected abstract void requestEvent(SpiSlaveEvent spiSlaveEvent);

    public SpiSlaveThread() {
        this.f133a = new LinkedList();
        this.f135c = new Object();
        this.f136d = new Object();
        this.f134b = new ReentrantLock();
        this.f139g = THREAD_INIT;
        setName("SpiSlaveThread");
    }

    public boolean sendMessage(SpiSlaveEvent event) {
        while (this.f139g != THREAD_RUNNING) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        this.f134b.lock();
        if (this.f133a.size() > 10) {
            this.f134b.unlock();
            Log.d("FTDI", "SpiSlaveThread sendMessage Buffer full!!");
            return false;
        }
        this.f133a.add(event);
        if (this.f133a.size() == THREAD_RUNNING) {
            synchronized (this.f135c) {
                this.f137e = true;
                this.f135c.notify();
            }
        }
        this.f134b.unlock();
        if (event.getSync()) {
            synchronized (this.f136d) {
                this.f138f = false;
                while (!this.f138f) {
                    try {
                        this.f136d.wait();
                    } catch (InterruptedException e2) {
                        this.f138f = true;
                    }
                }
            }
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r4 = this;
        r1 = 1;
        r0 = 0;
        r4.f139g = r1;
    L_0x0004:
        r1 = java.lang.Thread.interrupted();
        if (r1 != 0) goto L_0x000c;
    L_0x000a:
        if (r0 == 0) goto L_0x0010;
    L_0x000c:
        r0 = 2;
        r4.f139g = r0;
        return;
    L_0x0010:
        r4.pollData();
        r1 = r4.f134b;
        r1.lock();
        r1 = r4.f133a;
        r1 = r1.size();
        if (r1 > 0) goto L_0x0026;
    L_0x0020:
        r1 = r4.f134b;
        r1.unlock();
        goto L_0x0004;
    L_0x0026:
        r0 = r4.f133a;
        r0 = r0.peek();
        r0 = (com.ftdi.j2xx.protocol.SpiSlaveEvent) r0;
        r1 = r4.f133a;
        r1.remove();
        r1 = r4.f134b;
        r1.unlock();
        r4.requestEvent(r0);
        r1 = r0.getSync();
        if (r1 == 0) goto L_0x0051;
    L_0x0041:
        r1 = r4.f136d;
        monitor-enter(r1);
    L_0x0044:
        r2 = r4.f138f;	 Catch:{ all -> 0x005e }
        if (r2 != 0) goto L_0x0056;
    L_0x0048:
        r2 = 1;
        r4.f138f = r2;	 Catch:{ all -> 0x005e }
        r2 = r4.f136d;	 Catch:{ all -> 0x005e }
        r2.notify();	 Catch:{ all -> 0x005e }
        monitor-exit(r1);	 Catch:{ all -> 0x005e }
    L_0x0051:
        r0 = r4.isTerminateEvent(r0);
        goto L_0x0004;
    L_0x0056:
        r2 = 100;
        java.lang.Thread.sleep(r2);	 Catch:{ InterruptedException -> 0x005c }
        goto L_0x0044;
    L_0x005c:
        r2 = move-exception;
        goto L_0x0044;
    L_0x005e:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x005e }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ftdi.j2xx.protocol.SpiSlaveThread.run():void");
    }
}
