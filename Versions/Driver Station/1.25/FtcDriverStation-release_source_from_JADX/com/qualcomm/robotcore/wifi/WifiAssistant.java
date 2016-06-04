package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import com.qualcomm.robotcore.util.RobotLog;

public class WifiAssistant {
    private final IntentFilter f584a;
    private final Context f585b;
    private final C0127a f586c;

    public interface WifiAssistantCallback {
        void wifiEventCallback(WifiState wifiState);
    }

    public enum WifiState {
        CONNECTED,
        NOT_CONNECTED
    }

    /* renamed from: com.qualcomm.robotcore.wifi.WifiAssistant.a */
    private static class C0127a extends BroadcastReceiver {
        private WifiState f582a;
        private final WifiAssistantCallback f583b;

        public C0127a(WifiAssistantCallback wifiAssistantCallback) {
            this.f582a = null;
            this.f583b = wifiAssistantCallback;
        }

        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
                return;
            }
            if (((NetworkInfo) intent.getParcelableExtra("networkInfo")).isConnected()) {
                m335a(WifiState.CONNECTED);
            } else {
                m335a(WifiState.NOT_CONNECTED);
            }
        }

        private void m335a(WifiState wifiState) {
            if (this.f582a != wifiState) {
                this.f582a = wifiState;
                if (this.f583b != null) {
                    this.f583b.wifiEventCallback(this.f582a);
                }
            }
        }
    }

    public WifiAssistant(Context context, WifiAssistantCallback callback) {
        this.f585b = context;
        if (callback == null) {
            RobotLog.m330v("WifiAssistantCallback is null");
        }
        this.f586c = new C0127a(callback);
        this.f584a = new IntentFilter();
        this.f584a.addAction("android.net.wifi.STATE_CHANGE");
    }

    public void enable() {
        this.f585b.registerReceiver(this.f586c, this.f584a);
    }

    public void disable() {
        this.f585b.unregisterReceiver(this.f586c);
    }
}
