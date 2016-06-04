package com.qualcomm.robotcore.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

public class BatteryChecker {
    Runnable f514a;
    private Context f515b;
    protected Handler batteryHandler;
    private long f516c;
    private long f517d;
    private BatteryWatcher f518e;

    /* renamed from: com.qualcomm.robotcore.util.BatteryChecker.1 */
    class C01151 implements Runnable {
        final /* synthetic */ BatteryChecker f513a;

        C01151(BatteryChecker batteryChecker) {
            this.f513a = batteryChecker;
        }

        public void run() {
            float batteryLevel = this.f513a.getBatteryLevel();
            this.f513a.f518e.updateBatteryLevel(batteryLevel);
            RobotLog.m329i("Battery Checker, Level Remaining: " + batteryLevel);
            this.f513a.batteryHandler.postDelayed(this.f513a.f514a, this.f513a.f516c);
        }
    }

    public interface BatteryWatcher {
        void updateBatteryLevel(float f);
    }

    public BatteryChecker(Context context, BatteryWatcher watcher, long delay) {
        this.f517d = 5000;
        this.f514a = new C01151(this);
        this.f515b = context;
        this.f518e = watcher;
        this.f516c = delay;
        this.batteryHandler = new Handler();
    }

    public float getBatteryLevel() {
        int i = -1;
        Intent registerReceiver = this.f515b.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        int intExtra = registerReceiver.getIntExtra("level", -1);
        int intExtra2 = registerReceiver.getIntExtra("scale", -1);
        if (intExtra >= 0 && intExtra2 > 0) {
            i = (intExtra * 100) / intExtra2;
        }
        return (float) i;
    }

    public void startBatteryMonitoring() {
        this.batteryHandler.postDelayed(this.f514a, this.f517d);
    }

    public void endBatteryMonitoring() {
        this.batteryHandler.removeCallbacks(this.f514a);
    }
}
