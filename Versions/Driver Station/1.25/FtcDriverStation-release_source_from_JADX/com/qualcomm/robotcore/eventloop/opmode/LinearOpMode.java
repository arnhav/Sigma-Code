package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

public abstract class LinearOpMode extends OpMode {
    private C0105a f672a;
    private Thread f673b;
    private ElapsedTime f674c;
    private volatile boolean f675d;

    /* renamed from: com.qualcomm.robotcore.eventloop.opmode.LinearOpMode.a */
    private static class C0105a implements Runnable {
        private RuntimeException f413a;
        private boolean f414b;
        private final LinearOpMode f415c;

        public C0105a(LinearOpMode linearOpMode) {
            this.f413a = null;
            this.f414b = false;
            this.f415c = linearOpMode;
        }

        public void run() {
            this.f413a = null;
            this.f414b = false;
            try {
                this.f415c.runOpMode();
            } catch (InterruptedException e) {
                RobotLog.m327d("LinearOpMode received an Interrupted Exception; shutting down this linear op mode");
            } catch (RuntimeException e2) {
                this.f413a = e2;
            } finally {
                this.f414b = true;
            }
        }

        public boolean m285a() {
            return this.f413a != null;
        }

        public RuntimeException m286b() {
            return this.f413a;
        }

        public boolean m287c() {
            return this.f414b;
        }
    }

    public abstract void runOpMode() throws InterruptedException;

    public LinearOpMode() {
        this.f672a = null;
        this.f673b = null;
        this.f674c = new ElapsedTime();
        this.f675d = false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void waitForStart() throws java.lang.InterruptedException {
        /*
        r1 = this;
        monitor-enter(r1);
    L_0x0001:
        r0 = r1.f675d;	 Catch:{ all -> 0x000e }
        if (r0 != 0) goto L_0x0011;
    L_0x0005:
        monitor-enter(r1);	 Catch:{ all -> 0x000e }
        r1.wait();	 Catch:{ all -> 0x000b }
        monitor-exit(r1);	 Catch:{ all -> 0x000b }
        goto L_0x0001;
    L_0x000b:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x000b }
        throw r0;	 Catch:{ all -> 0x000e }
    L_0x000e:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
    L_0x0011:
        monitor-exit(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qualcomm.robotcore.eventloop.opmode.LinearOpMode.waitForStart():void");
    }

    public void waitOneFullHardwareCycle() throws InterruptedException {
        waitForNextHardwareCycle();
        Thread.sleep(1);
        waitForNextHardwareCycle();
    }

    public void waitForNextHardwareCycle() throws InterruptedException {
        synchronized (this) {
            wait();
        }
    }

    public void sleep(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public boolean opModeIsActive() {
        return this.f675d;
    }

    public final void init() {
        this.f672a = new C0105a(this);
        this.f673b = new Thread(this.f672a, "Linear OpMode Helper");
        this.f673b.start();
    }

    public final void init_loop() {
        m447a();
    }

    public final void start() {
        this.f675d = true;
        synchronized (this) {
            notifyAll();
        }
    }

    public final void loop() {
        m447a();
    }

    public final void stop() {
        this.f675d = false;
        if (!this.f672a.m287c()) {
            this.f673b.interrupt();
        }
        this.f674c.reset();
        while (!this.f672a.m287c() && this.f674c.time() < 0.5d) {
            Thread.yield();
        }
        if (!this.f672a.m287c()) {
            RobotLog.m328e("*****************************************************************");
            RobotLog.m328e("User Linear Op Mode took too long to exit; emergency killing app.");
            RobotLog.m328e("Possible infinite loop in user code?");
            RobotLog.m328e("*****************************************************************");
            System.exit(-1);
        }
    }

    private void m447a() {
        if (this.f672a.m285a()) {
            throw this.f672a.m286b();
        }
        synchronized (this) {
            notifyAll();
        }
    }
}
