package com.qualcomm.robotcore.hardware;

public abstract class AccelerationSensor implements HardwareDevice {

    public static class Acceleration {
        public double f432x;
        public double f433y;
        public double f434z;

        public Acceleration() {
            this(0.0d, 0.0d, 0.0d);
        }

        public Acceleration(double x, double y, double z) {
            this.f432x = x;
            this.f433y = y;
            this.f434z = z;
        }

        public String toString() {
            return String.format("Acceleration - x: %5.2f, y: %5.2f, z: %5.2f", new Object[]{Double.valueOf(this.f432x), Double.valueOf(this.f433y), Double.valueOf(this.f434z)});
        }
    }

    public abstract Acceleration getAcceleration();

    public abstract String status();

    public String toString() {
        return getAcceleration().toString();
    }
}
