package com.qualcomm.robotcore.util;

import java.util.LinkedList;
import java.util.Queue;

public class RollingAverage {
    public static final int DEFAULT_SIZE = 100;
    private final Queue<Integer> f573a;
    private long f574b;
    private int f575c;

    public RollingAverage() {
        this.f573a = new LinkedList();
        resize(DEFAULT_SIZE);
    }

    public RollingAverage(int size) {
        this.f573a = new LinkedList();
        resize(size);
    }

    public int size() {
        return this.f575c;
    }

    public void resize(int size) {
        this.f575c = size;
        this.f573a.clear();
    }

    public void addNumber(int number) {
        if (this.f573a.size() >= this.f575c) {
            this.f574b -= (long) ((Integer) this.f573a.remove()).intValue();
        }
        this.f573a.add(Integer.valueOf(number));
        this.f574b += (long) number;
    }

    public int getAverage() {
        if (this.f573a.isEmpty()) {
            return 0;
        }
        return (int) (this.f574b / ((long) this.f573a.size()));
    }

    public void reset() {
        this.f573a.clear();
    }
}
