package com.qualcomm.robotcore.util;

import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;

public class ElapsedTime {
    private long f532a;
    private double f533b;

    /* renamed from: com.qualcomm.robotcore.util.ElapsedTime.1 */
    static /* synthetic */ class C01191 {
        static final /* synthetic */ int[] f530a;

        static {
            f530a = new int[Resolution.values().length];
            try {
                f530a[Resolution.SECONDS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f530a[Resolution.MILLISECONDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public enum Resolution {
        SECONDS,
        MILLISECONDS
    }

    public ElapsedTime() {
        this.f532a = 0;
        this.f533b = 1.0E9d;
        reset();
    }

    public ElapsedTime(long startTime) {
        this.f532a = 0;
        this.f533b = 1.0E9d;
        this.f532a = startTime;
    }

    public ElapsedTime(Resolution resolution) {
        this.f532a = 0;
        this.f533b = 1.0E9d;
        reset();
        switch (C01191.f530a[resolution.ordinal()]) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                this.f533b = 1.0E9d;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                this.f533b = 1000000.0d;
            default:
        }
    }

    public void reset() {
        this.f532a = System.nanoTime();
    }

    public double startTime() {
        return ((double) this.f532a) / this.f533b;
    }

    public double time() {
        return ((double) (System.nanoTime() - this.f532a)) / this.f533b;
    }

    private String m316a() {
        if (this.f533b == 1.0E9d) {
            return "seconds";
        }
        if (this.f533b == 1000000.0d) {
            return "milliseconds";
        }
        return "Unknown units";
    }

    public void log(String label) {
        RobotLog.m330v(String.format("TIMER: %20s - %1.3f %s", new Object[]{label, Double.valueOf(time()), m316a()}));
    }

    public String toString() {
        return String.format("%1.4f %s", new Object[]{Double.valueOf(time()), m316a()});
    }
}
