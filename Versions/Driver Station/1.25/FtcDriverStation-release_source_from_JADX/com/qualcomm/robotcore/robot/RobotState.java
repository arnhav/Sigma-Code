package com.qualcomm.robotcore.robot;

import com.qualcomm.robotcore.util.RobotLog;

public enum RobotState {
    NOT_STARTED(0),
    INIT(1),
    RUNNING(2),
    STOPPED(3),
    EMERGENCY_STOP(4),
    DROPPED_CONNECTION(5);
    
    private static final RobotState[] f504b;
    private int f506a;

    static {
        f504b = values();
    }

    private RobotState(int state) {
        this.f506a = state;
    }

    public byte asByte() {
        return (byte) this.f506a;
    }

    public static RobotState fromByte(byte b) {
        RobotState robotState = NOT_STARTED;
        try {
            return f504b[b];
        } catch (ArrayIndexOutOfBoundsException e) {
            RobotLog.m331w(String.format("Cannot convert %d to RobotState: %s", new Object[]{Byte.valueOf(b), e.toString()}));
            return robotState;
        }
    }
}
