package com.qualcomm.ftccommon;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.ftccommon.UpdateUI.Callback;
import com.qualcomm.ftcdriverstation.BuildConfig;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager.EventLoopMonitor;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.WifiDirectAssistantCallback;
import java.lang.Thread.State;

public class FtcRobotControllerService extends Service implements WifiDirectAssistantCallback {
    private final IBinder f662a;
    private WifiDirectAssistant f663b;
    private Robot f664c;
    private EventLoop f665d;
    private Event f666e;
    private String f667f;
    private Callback f668g;
    private final C0144a f669h;
    private final ElapsedTime f670i;
    private Thread f671j;

    /* renamed from: com.qualcomm.ftccommon.FtcRobotControllerService.1 */
    static /* synthetic */ class C00261 {
        static final /* synthetic */ int[] f167a;
        static final /* synthetic */ int[] f168b;

        static {
            f168b = new int[Event.values().length];
            try {
                f168b[Event.CONNECTED_AS_GROUP_OWNER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f168b[Event.CONNECTED_AS_PEER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f168b[Event.CONNECTION_INFO_AVAILABLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f168b[Event.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            f167a = new int[RobotState.values().length];
            try {
                f167a[RobotState.INIT.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f167a[RobotState.NOT_STARTED.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f167a[RobotState.RUNNING.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f167a[RobotState.STOPPED.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f167a[RobotState.EMERGENCY_STOP.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f167a[RobotState.DROPPED_CONNECTION.ordinal()] = 6;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    public class FtcRobotControllerBinder extends Binder {
        final /* synthetic */ FtcRobotControllerService f169a;

        public FtcRobotControllerBinder(FtcRobotControllerService ftcRobotControllerService) {
            this.f169a = ftcRobotControllerService;
        }

        public FtcRobotControllerService getService() {
            return this.f169a;
        }
    }

    /* renamed from: com.qualcomm.ftccommon.FtcRobotControllerService.b */
    private class C0027b implements Runnable {
        final /* synthetic */ FtcRobotControllerService f170a;

        private C0027b(FtcRobotControllerService ftcRobotControllerService) {
            this.f170a = ftcRobotControllerService;
        }

        public void run() {
            try {
                if (this.f170a.f664c != null) {
                    this.f170a.f664c.shutdown();
                    this.f170a.f664c = null;
                }
                this.f170a.m441a("Robot Status: scanning for USB devices");
                try {
                    Thread.sleep(5000);
                    this.f170a.f664c = RobotFactory.createRobot();
                    this.f170a.m441a("Robot Status: waiting on network");
                    this.f170a.f670i.reset();
                    do {
                        if (this.f170a.f663b.isConnected()) {
                            this.f170a.m441a("Robot Status: starting robot");
                            try {
                                this.f170a.f664c.eventLoopManager.setMonitor(this.f170a.f669h);
                                this.f170a.f664c.start(this.f170a.f663b.getGroupOwnerAddress(), this.f170a.f665d);
                                return;
                            } catch (RobotCoreException e) {
                                this.f170a.m441a("Robot Status: failed to start robot");
                                RobotLog.setGlobalErrorMsg(e.getMessage());
                                return;
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e2) {
                            DbgLog.msg("interrupt waiting for network; aborting setup");
                            return;
                        }
                    } while (this.f170a.f670i.time() <= 120.0d);
                    this.f170a.m441a("Robot Status: network timed out");
                } catch (InterruptedException e3) {
                    this.f170a.m441a("Robot Status: abort due to interrupt");
                }
            } catch (RobotCoreException e4) {
                this.f170a.m441a("Robot Status: Unable to create robot!");
                RobotLog.setGlobalErrorMsg(e4.getMessage());
            }
        }
    }

    /* renamed from: com.qualcomm.ftccommon.FtcRobotControllerService.a */
    private class C0144a implements EventLoopMonitor {
        final /* synthetic */ FtcRobotControllerService f661a;

        private C0144a(FtcRobotControllerService ftcRobotControllerService) {
            this.f661a = ftcRobotControllerService;
        }

        public void onStateChange(RobotState state) {
            if (this.f661a.f668g != null) {
                switch (C00261.f167a[state.ordinal()]) {
                    case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                        this.f661a.f668g.robotUpdate("Robot Status: init");
                    case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                        this.f661a.f668g.robotUpdate("Robot Status: not started");
                    case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                        this.f661a.f668g.robotUpdate("Robot Status: running");
                    case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                        this.f661a.f668g.robotUpdate("Robot Status: stopped");
                    case BuildConfig.VERSION_CODE /*5*/:
                        this.f661a.f668g.robotUpdate("Robot Status: EMERGENCY STOP");
                    case FT4222_STATUS.FT4222_INVALID_PARAMETER /*6*/:
                        this.f661a.f668g.robotUpdate("Robot Status: dropped connection");
                    default:
                }
            }
        }
    }

    public FtcRobotControllerService() {
        this.f662a = new FtcRobotControllerBinder(this);
        this.f666e = Event.DISCONNECTED;
        this.f667f = "Robot Status: null";
        this.f668g = null;
        this.f669h = new C0144a();
        this.f670i = new ElapsedTime();
        this.f671j = null;
    }

    public WifiDirectAssistant getWifiDirectAssistant() {
        return this.f663b;
    }

    public Event getWifiDirectStatus() {
        return this.f666e;
    }

    public String getRobotStatus() {
        return this.f667f;
    }

    public IBinder onBind(Intent intent) {
        DbgLog.msg("Starting FTC Controller Service");
        DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
        this.f663b = WifiDirectAssistant.getWifiDirectAssistant(this);
        this.f663b.setCallback(this);
        this.f663b.enable();
        if (Build.MODEL.equals(Device.MODEL_FOXDA_FL7007)) {
            this.f663b.discoverPeers();
        } else {
            this.f663b.createGroup();
        }
        return this.f662a;
    }

    public boolean onUnbind(Intent intent) {
        DbgLog.msg("Stopping FTC Controller Service");
        this.f663b.disable();
        shutdownRobot();
        return false;
    }

    public synchronized void setCallback(Callback callback) {
        this.f668g = callback;
    }

    public synchronized void setupRobot(EventLoop eventLoop) {
        if (this.f671j != null && this.f671j.isAlive()) {
            DbgLog.msg("FtcRobotControllerService.setupRobot() is currently running, stopping old setup");
            this.f671j.interrupt();
            while (this.f671j.isAlive()) {
                Thread.yield();
            }
            DbgLog.msg("Old setup stopped; restarting setup");
        }
        RobotLog.clearGlobalErrorMsg();
        DbgLog.msg("Processing robot setup");
        this.f665d = eventLoop;
        this.f671j = new Thread(new C0027b(), "Robot Setup");
        this.f671j.start();
        while (this.f671j.getState() == State.NEW) {
            Thread.yield();
        }
    }

    public synchronized void shutdownRobot() {
        if (this.f671j != null && this.f671j.isAlive()) {
            this.f671j.interrupt();
        }
        if (this.f664c != null) {
            this.f664c.shutdown();
        }
        this.f664c = null;
        m441a("Robot Status: null");
    }

    public void onWifiDirectEvent(Event event) {
        switch (C00261.f168b[event.ordinal()]) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                DbgLog.msg("Wifi Direct - Group Owner");
                this.f663b.cancelDiscoverPeers();
                break;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
                break;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                DbgLog.msg("Wifi Direct Passphrase: " + this.f663b.getPassphrase());
                break;
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                DbgLog.error("Wifi Direct Error: " + this.f663b.getFailureReason());
                break;
        }
        m440a(event);
    }

    private void m440a(Event event) {
        this.f666e = event;
        if (this.f668g != null) {
            this.f668g.wifiDirectUpdate(this.f666e);
        }
    }

    private void m441a(String str) {
        this.f667f = str;
        if (this.f668g != null) {
            this.f668g.robotUpdate(str);
        }
    }
}
