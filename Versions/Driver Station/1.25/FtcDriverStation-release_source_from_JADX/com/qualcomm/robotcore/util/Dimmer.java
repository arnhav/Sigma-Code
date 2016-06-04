package com.qualcomm.robotcore.util;

import android.app.Activity;
import android.os.Handler;
import android.view.WindowManager.LayoutParams;

public class Dimmer {
    public static final int DEFAULT_DIM_TIME = 30000;
    public static final int LONG_BRIGHT_TIME = 60000;
    public static final float MAXIMUM_BRIGHTNESS = 1.0f;
    public static final float MINIMUM_BRIGHTNESS = 0.05f;
    Handler f525a;
    Activity f526b;
    final LayoutParams f527c;
    long f528d;
    float f529e;

    /* renamed from: com.qualcomm.robotcore.util.Dimmer.1 */
    class C01161 implements Runnable {
        final /* synthetic */ Dimmer f522a;

        C01161(Dimmer dimmer) {
            this.f522a = dimmer;
        }

        public void run() {
            this.f522a.m314a(this.f522a.m312a());
        }
    }

    /* renamed from: com.qualcomm.robotcore.util.Dimmer.2 */
    class C01172 implements Runnable {
        final /* synthetic */ Dimmer f523a;

        C01172(Dimmer dimmer) {
            this.f523a = dimmer;
        }

        public void run() {
            this.f523a.f526b.getWindow().setAttributes(this.f523a.f527c);
        }
    }

    /* renamed from: com.qualcomm.robotcore.util.Dimmer.3 */
    class C01183 implements Runnable {
        final /* synthetic */ Dimmer f524a;

        C01183(Dimmer dimmer) {
            this.f524a = dimmer;
        }

        public void run() {
            this.f524a.m314a(this.f524a.m312a());
        }
    }

    public Dimmer(Activity activity) {
        this(30000, activity);
    }

    public Dimmer(long waitTime, Activity activity) {
        this.f525a = new Handler();
        this.f529e = MAXIMUM_BRIGHTNESS;
        this.f528d = waitTime;
        this.f526b = activity;
        this.f527c = activity.getWindow().getAttributes();
        this.f529e = this.f527c.screenBrightness;
    }

    private float m312a() {
        float f = this.f529e * MINIMUM_BRIGHTNESS;
        if (f < MINIMUM_BRIGHTNESS) {
            return MINIMUM_BRIGHTNESS;
        }
        return f;
    }

    public void handleDimTimer() {
        m314a(this.f529e);
        this.f525a.removeCallbacks(null);
        this.f525a.postDelayed(new C01161(this), this.f528d);
    }

    private void m314a(float f) {
        this.f527c.screenBrightness = f;
        this.f526b.runOnUiThread(new C01172(this));
    }

    public void longBright() {
        m314a(this.f529e);
        Runnable c01183 = new C01183(this);
        this.f525a.removeCallbacksAndMessages(null);
        this.f525a.postDelayed(c01183, 60000);
    }
}
