package com.qualcomm.robotcore.util;

public class DifferentialControlLoopCoefficients {
    public double f519d;
    public double f520i;
    public double f521p;

    public DifferentialControlLoopCoefficients() {
        this.f521p = 0.0d;
        this.f520i = 0.0d;
        this.f519d = 0.0d;
    }

    public DifferentialControlLoopCoefficients(double p, double i, double d) {
        this.f521p = 0.0d;
        this.f520i = 0.0d;
        this.f519d = 0.0d;
        this.f521p = p;
        this.f520i = i;
        this.f519d = d;
    }
}
