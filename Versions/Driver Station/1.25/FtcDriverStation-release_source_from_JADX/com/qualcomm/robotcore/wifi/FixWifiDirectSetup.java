package com.qualcomm.robotcore.wifi;

import android.net.wifi.WifiManager;

public class FixWifiDirectSetup {
    public static final int WIFI_TOGGLE_DELAY = 2000;

    public static void fixWifiDirectSetup(WifiManager wifiManager) throws InterruptedException {
        m334a(false, wifiManager);
        m334a(true, wifiManager);
    }

    private static void m334a(boolean z, WifiManager wifiManager) throws InterruptedException {
        wifiManager.setWifiEnabled(z);
        Thread.sleep(2000);
    }
}
