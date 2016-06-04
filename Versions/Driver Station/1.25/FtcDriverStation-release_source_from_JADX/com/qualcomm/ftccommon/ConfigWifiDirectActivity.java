package com.qualcomm.ftccommon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import com.qualcomm.robotcore.wifi.FixWifiDirectSetup;

public class ConfigWifiDirectActivity extends Activity {
    private ProgressDialog f165a;
    private Context f166b;

    /* renamed from: com.qualcomm.ftccommon.ConfigWifiDirectActivity.a */
    private class C0025a implements Runnable {
        final /* synthetic */ ConfigWifiDirectActivity f164a;

        /* renamed from: com.qualcomm.ftccommon.ConfigWifiDirectActivity.a.1 */
        class C00231 implements Runnable {
            final /* synthetic */ C0025a f162a;

            C00231(C0025a c0025a) {
                this.f162a = c0025a;
            }

            public void run() {
                this.f162a.f164a.f165a = new ProgressDialog(this.f162a.f164a.f166b, C0035R.style.CustomAlertDialog);
                this.f162a.f164a.f165a.setMessage("Please wait");
                this.f162a.f164a.f165a.setTitle("Configuring Wifi Direct");
                this.f162a.f164a.f165a.setIndeterminate(true);
                this.f162a.f164a.f165a.show();
            }
        }

        /* renamed from: com.qualcomm.ftccommon.ConfigWifiDirectActivity.a.2 */
        class C00242 implements Runnable {
            final /* synthetic */ C0025a f163a;

            C00242(C0025a c0025a) {
                this.f163a = c0025a;
            }

            public void run() {
                this.f163a.f164a.f165a.dismiss();
                this.f163a.f164a.finish();
            }
        }

        private C0025a(ConfigWifiDirectActivity configWifiDirectActivity) {
            this.f164a = configWifiDirectActivity;
        }

        public void run() {
            DbgLog.msg("attempting to reconfigure Wifi Direct");
            this.f164a.runOnUiThread(new C00231(this));
            try {
                FixWifiDirectSetup.fixWifiDirectSetup((WifiManager) this.f164a.getSystemService("wifi"));
            } catch (InterruptedException e) {
                DbgLog.msg("Cannot fix wifi setup - interrupted");
            }
            this.f164a.runOnUiThread(new C00242(this));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0035R.layout.activity_config_wifi_direct);
        this.f166b = this;
    }

    protected void onResume() {
        super.onResume();
        new Thread(new C0025a()).start();
    }
}
