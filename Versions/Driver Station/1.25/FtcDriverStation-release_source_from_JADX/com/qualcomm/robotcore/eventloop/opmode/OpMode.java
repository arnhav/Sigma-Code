package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;
import java.util.concurrent.TimeUnit;

public abstract class OpMode {
    private long f416a;
    public Gamepad gamepad1;
    public Gamepad gamepad2;
    public HardwareMap hardwareMap;
    public Telemetry telemetry;
    public double time;

    public abstract void init();

    public abstract void loop();

    public OpMode() {
        this.gamepad1 = new Gamepad();
        this.gamepad2 = new Gamepad();
        this.telemetry = new Telemetry();
        this.hardwareMap = new HardwareMap();
        this.time = 0.0d;
        this.f416a = 0;
        this.f416a = System.nanoTime();
    }

    public void init_loop() {
    }

    public void start() {
    }

    public void stop() {
    }

    public double getRuntime() {
        return ((double) (System.nanoTime() - this.f416a)) / ((double) TimeUnit.SECONDS.toNanos(1));
    }

    public void resetStartTime() {
        this.f416a = System.nanoTime();
    }
}
