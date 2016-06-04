package com.qualcomm.robotcore.exception;

public class RobotCoreException extends Exception {
    private Exception f431a;

    public RobotCoreException(String message) {
        super(message);
        this.f431a = null;
    }

    public RobotCoreException(String message, Exception e) {
        super(message);
        this.f431a = null;
        this.f431a = e;
    }

    public boolean isChainedException() {
        return this.f431a != null;
    }

    public Exception getChainedException() {
        return this.f431a;
    }
}
