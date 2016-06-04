package com.qualcomm.robotcore.util;

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.qualcomm.robotcore.robocol.RobocolConfig;

public class ImmersiveMode {
    View f538a;
    Handler f539b;

    /* renamed from: com.qualcomm.robotcore.util.ImmersiveMode.1 */
    class C01211 extends Handler {
        final /* synthetic */ ImmersiveMode f537a;

        C01211(ImmersiveMode immersiveMode) {
            this.f537a = immersiveMode;
        }

        public void handleMessage(Message msg) {
            this.f537a.hideSystemUI();
        }
    }

    public ImmersiveMode(View decorView) {
        this.f539b = new C01211(this);
        this.f538a = decorView;
    }

    public void cancelSystemUIHide() {
        this.f539b.removeMessages(0);
    }

    public void hideSystemUI() {
        this.f538a.setSystemUiVisibility(RobocolConfig.MAX_PACKET_SIZE);
    }

    public static boolean apiOver19() {
        return VERSION.SDK_INT >= 19;
    }
}
