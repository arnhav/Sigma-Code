package com.qualcomm.ftccommon;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Dimmer;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;

public class UpdateUI {
    Restarter f204a;
    FtcRobotControllerService f205b;
    Activity f206c;
    Dimmer f207d;
    protected TextView textDeviceName;
    protected TextView textErrorMessage;
    protected TextView[] textGamepad;
    protected TextView textOpMode;
    protected TextView textRobotStatus;
    protected TextView textWifiDirectStatus;

    /* renamed from: com.qualcomm.ftccommon.UpdateUI.1 */
    class C00361 implements Runnable {
        final /* synthetic */ String f190a;
        final /* synthetic */ UpdateUI f191b;

        C00361(UpdateUI updateUI, String str) {
            this.f191b = updateUI;
            this.f190a = str;
        }

        public void run() {
            this.f191b.textWifiDirectStatus.setText(this.f190a);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.UpdateUI.2 */
    class C00372 implements Runnable {
        final /* synthetic */ String f192a;
        final /* synthetic */ UpdateUI f193b;

        C00372(UpdateUI updateUI, String str) {
            this.f193b = updateUI;
            this.f192a = str;
        }

        public void run() {
            this.f193b.textDeviceName.setText(this.f192a);
        }
    }

    /* renamed from: com.qualcomm.ftccommon.UpdateUI.3 */
    static /* synthetic */ class C00383 {
        static final /* synthetic */ int[] f194a;

        static {
            f194a = new int[Event.values().length];
            try {
                f194a[Event.DISCONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f194a[Event.CONNECTED_AS_GROUP_OWNER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f194a[Event.ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f194a[Event.CONNECTION_INFO_AVAILABLE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public class Callback {
        final /* synthetic */ UpdateUI f203a;

        /* renamed from: com.qualcomm.ftccommon.UpdateUI.Callback.1 */
        class C00391 implements Runnable {
            final /* synthetic */ Callback f195a;

            C00391(Callback callback) {
                this.f195a = callback;
            }

            public void run() {
                Toast.makeText(this.f195a.f203a.f206c, "Restarting Robot", 0).show();
            }
        }

        /* renamed from: com.qualcomm.ftccommon.UpdateUI.Callback.2 */
        class C00412 extends Thread {
            final /* synthetic */ Callback f197a;

            /* renamed from: com.qualcomm.ftccommon.UpdateUI.Callback.2.1 */
            class C00401 implements Runnable {
                final /* synthetic */ C00412 f196a;

                C00401(C00412 c00412) {
                    this.f196a = c00412;
                }

                public void run() {
                    this.f196a.f197a.f203a.m104a();
                }
            }

            C00412(Callback callback) {
                this.f197a = callback;
            }

            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }
                this.f197a.f203a.f206c.runOnUiThread(new C00401(this));
            }
        }

        /* renamed from: com.qualcomm.ftccommon.UpdateUI.Callback.3 */
        class C00423 implements Runnable {
            final /* synthetic */ Gamepad[] f198a;
            final /* synthetic */ String f199b;
            final /* synthetic */ Callback f200c;

            C00423(Callback callback, Gamepad[] gamepadArr, String str) {
                this.f200c = callback;
                this.f198a = gamepadArr;
                this.f199b = str;
            }

            public void run() {
                int i = 0;
                while (i < this.f200c.f203a.textGamepad.length && i < this.f198a.length) {
                    if (this.f198a[i].id == -1) {
                        this.f200c.f203a.textGamepad[i].setText(" ");
                    } else {
                        this.f200c.f203a.textGamepad[i].setText(this.f198a[i].toString());
                    }
                    i++;
                }
                this.f200c.f203a.textOpMode.setText("Op Mode: " + this.f199b);
                this.f200c.f203a.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
            }
        }

        /* renamed from: com.qualcomm.ftccommon.UpdateUI.Callback.4 */
        class C00434 implements Runnable {
            final /* synthetic */ String f201a;
            final /* synthetic */ Callback f202b;

            C00434(Callback callback, String str) {
                this.f202b = callback;
                this.f201a = str;
            }

            public void run() {
                this.f202b.f203a.textRobotStatus.setText(this.f201a);
                this.f202b.f203a.textErrorMessage.setText(RobotLog.getGlobalErrorMsg());
                if (RobotLog.hasGlobalErrorMsg()) {
                    this.f202b.f203a.f207d.longBright();
                }
            }
        }

        public Callback(UpdateUI updateUI) {
            this.f203a = updateUI;
        }

        public void restartRobot() {
            this.f203a.f206c.runOnUiThread(new C00391(this));
            new C00412(this).start();
        }

        public void updateUi(String opModeName, Gamepad[] gamepads) {
            this.f203a.f206c.runOnUiThread(new C00423(this, gamepads, opModeName));
        }

        public void wifiDirectUpdate(Event event) {
            String str = "Wifi Direct - ";
            switch (C00383.f194a[event.ordinal()]) {
                case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                    this.f203a.m107a("Wifi Direct - disconnected");
                case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                    this.f203a.m107a("Wifi Direct - enabled");
                case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                    this.f203a.m107a("Wifi Direct - ERROR");
                case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                    this.f203a.m109b(this.f203a.f205b.getWifiDirectAssistant().getDeviceName());
                default:
            }
        }

        public void robotUpdate(String status) {
            DbgLog.msg(status);
            this.f203a.f206c.runOnUiThread(new C00434(this, status));
        }
    }

    public UpdateUI(Activity activity, Dimmer dimmer) {
        this.textGamepad = new TextView[2];
        this.f206c = activity;
        this.f207d = dimmer;
    }

    public void setTextViews(TextView textWifiDirectStatus, TextView textRobotStatus, TextView[] textGamepad, TextView textOpMode, TextView textErrorMessage, TextView textDeviceName) {
        this.textWifiDirectStatus = textWifiDirectStatus;
        this.textRobotStatus = textRobotStatus;
        this.textGamepad = textGamepad;
        this.textOpMode = textOpMode;
        this.textErrorMessage = textErrorMessage;
        this.textDeviceName = textDeviceName;
    }

    public void setControllerService(FtcRobotControllerService controllerService) {
        this.f205b = controllerService;
    }

    public void setRestarter(Restarter restarter) {
        this.f204a = restarter;
    }

    private void m107a(String str) {
        DbgLog.msg(str);
        this.f206c.runOnUiThread(new C00361(this, str));
    }

    private void m109b(String str) {
        this.f206c.runOnUiThread(new C00372(this, str));
    }

    private void m104a() {
        this.f204a.requestRestart();
    }
}
