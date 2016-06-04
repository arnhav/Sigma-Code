package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ftdi.j2xx.ft4222.FT_4222_Defines;
import com.ftdi.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.qualcomm.analytics.Analytics;
import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.ftccommon.ConfigWifiDirectActivity;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.FtcEventLoopHandler;
import com.qualcomm.ftcdriverstation.OpModeSelectionDialogFragment.OpModeSelectionDialogListener;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.hardware.logitech.LogitechGamepadF310;
import com.qualcomm.robotcore.hardware.microsoft.MicrosoftGamepadXbox360;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.PeerDiscoveryManager;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.robocol.RobocolParsable.MsgType;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.BatteryChecker.BatteryWatcher;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ImmersiveMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.RollingAverage;
import com.qualcomm.robotcore.util.Util;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.ConnectStatus;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.WifiDirectAssistantCallback;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FtcDriverStationActivity extends Activity implements WifiDirectAssistantCallback, OnSharedPreferenceChangeListener, OpModeSelectionDialogListener, BatteryWatcher {
    public static final double ASSUME_DISCONNECT_TIMER = 2.0d;
    protected static final float FULLY_OPAQUE = 1.0f;
    protected static final int MAX_COMMAND_ATTEMPTS = 10;
    protected static final int MAX_LOG_SIZE = 2048;
    protected static final float PARTLY_OPAQUE = 0.3f;
    protected Analytics analytics;
    AnimationListener animationListener_icon1;
    AnimationListener animationListener_icon2;
    protected BatteryChecker batteryChecker;
    protected View batteryInfo;
    protected ImageButton buttonInit;
    protected ImageButton buttonInitStop;
    protected ImageButton buttonMenu;
    protected Button buttonSelect;
    protected ImageButton buttonStart;
    protected ImageButton buttonStartTimed;
    protected ImageButton buttonStop;
    protected boolean clientConnected;
    protected Context context;
    protected View controlPanelBack;
    protected ImageView dsBatteryIcon;
    protected TextView dsBatteryInfo;
    protected boolean enableNetworkTrafficLogging;
    protected Map<Integer, Gamepad> gamepads;
    protected String groupOwnerMac;
    protected Heartbeat heartbeatRecv;
    protected Heartbeat heartbeatSend;
    protected ImmersiveMode immersion;
    protected ElapsedTime lastRecvPacket;
    protected ElapsedTime lastUiUpdate;
    protected OpModeCountDownTimer opModeCountDown;
    protected boolean opModeUseTimer;
    protected Set<String> opModes;
    protected PeerDiscoveryManager peerDiscoveryManager;
    protected Set<Command> pendingCommands;
    protected RollingAverage pingAverage;
    protected SharedPreferences preferences;
    protected String queuedOpMode;
    protected ImageView rcBatteryIcon;
    protected TextView rcBatteryTelemetry;
    protected ExecutorService recvLoopService;
    protected InetAddress remoteAddr;
    protected TextView robotBatteryTelemetry;
    protected RobotState robotState;
    protected ScheduledFuture<?> sendLoopFuture;
    protected ScheduledExecutorService sendLoopService;
    protected boolean setupNeeded;
    protected RobocolDatagramSocket socket;
    protected TextView systemTelemetry;
    protected TextView textDeviceName;
    protected TextView textPingStatus;
    protected TextView textTelemetry;
    protected TextView textTimer;
    protected TextView textWifiDirectStatus;
    protected ImageView userIcon_1_active;
    protected ImageView userIcon_1_base;
    protected ImageView userIcon_2_active;
    protected ImageView userIcon_2_base;
    protected Map<Integer, Integer> userToGamepadMap;
    protected Utility utility;
    protected WifiDirectAssistant wifiDirect;
    protected View wifiInfo;

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ View val$view;
        final /* synthetic */ int val$visibility;

        AnonymousClass10(View view, int i) {
            this.val$view = view;
            this.val$visibility = i;
        }

        public void run() {
            this.val$view.setVisibility(this.val$visibility);
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.11 */
    class AnonymousClass11 implements Runnable {
        final /* synthetic */ boolean val$enabled;
        final /* synthetic */ View val$view;

        AnonymousClass11(View view, boolean z) {
            this.val$view = view;
            this.val$enabled = z;
        }

        public void run() {
            this.val$view.setEnabled(this.val$enabled);
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.14 */
    static /* synthetic */ class AnonymousClass14 {
        static final /* synthetic */ int[] f390x658239ea;
        static final /* synthetic */ int[] $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event;

        static {
            $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event = new int[Event.values().length];
            try {
                $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[Event.PEERS_AVAILABLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[Event.CONNECTED_AS_GROUP_OWNER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[Event.CONNECTING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[Event.CONNECTED_AS_PEER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[Event.CONNECTION_INFO_AVAILABLE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[Event.DISCONNECTED.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[Event.ERROR.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            f390x658239ea = new int[MsgType.values().length];
            try {
                f390x658239ea[MsgType.PEER_DISCOVERY.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f390x658239ea[MsgType.HEARTBEAT.ordinal()] = 2;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f390x658239ea[MsgType.COMMAND.ordinal()] = 3;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f390x658239ea[MsgType.TELEMETRY.ordinal()] = 4;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.1 */
    class C00861 implements Runnable {
        final /* synthetic */ ImageView val$icon;
        final /* synthetic */ float val$percent;

        C00861(float f, ImageView imageView) {
            this.val$percent = f;
            this.val$icon = imageView;
        }

        public void run() {
            if (this.val$percent <= 15.0f) {
                this.val$icon.setImageResource(C0099R.drawable.icon_battery0);
            } else if (this.val$percent > 15.0f && this.val$percent <= 45.0f) {
                this.val$icon.setImageResource(C0099R.drawable.icon_battery25);
            } else if (this.val$percent > 45.0f && this.val$percent <= 65.0f) {
                this.val$icon.setImageResource(C0099R.drawable.icon_battery50);
            } else if (this.val$percent <= 65.0f || this.val$percent > 85.0f) {
                this.val$icon.setImageResource(C0099R.drawable.icon_battery100);
            } else {
                this.val$icon.setImageResource(C0099R.drawable.icon_battery75);
            }
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.2 */
    class C00872 implements OnClickListener {
        C00872() {
        }

        public void onClick(View v) {
            FtcDriverStationActivity.this.openOptionsMenu();
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.3 */
    class C00883 implements Runnable {
        final /* synthetic */ Toast val$toast;

        C00883(Toast toast) {
            this.val$toast = toast;
        }

        public void run() {
            this.val$toast.show();
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.4 */
    class C00894 implements Runnable {
        final /* synthetic */ String val$status;

        C00894(String str) {
            this.val$status = str;
        }

        public void run() {
            FtcDriverStationActivity.this.textWifiDirectStatus.setText(this.val$status);
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.5 */
    class C00905 implements Runnable {
        final /* synthetic */ String val$name;

        C00905(String str) {
            this.val$name = str;
        }

        public void run() {
            FtcDriverStationActivity.this.textDeviceName.setText(this.val$name);
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.6 */
    class C00916 implements Runnable {
        final /* synthetic */ Button val$button;
        final /* synthetic */ String val$text;

        C00916(Button button, String str) {
            this.val$button = button;
            this.val$text = str;
        }

        public void run() {
            this.val$button.setText(this.val$text);
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.7 */
    class C00927 implements Runnable {
        final /* synthetic */ String val$text;
        final /* synthetic */ TextView val$textView;

        C00927(TextView textView, String str) {
            this.val$textView = textView;
            this.val$text = str;
        }

        public void run() {
            this.val$textView.setText(this.val$text);
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.8 */
    class C00938 implements Runnable {
        final /* synthetic */ float val$opacity;
        final /* synthetic */ View val$v;

        C00938(View view, float f) {
            this.val$v = view;
            this.val$opacity = f;
        }

        public void run() {
            this.val$v.setAlpha(this.val$opacity);
        }
    }

    /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.9 */
    class C00949 implements Runnable {
        final /* synthetic */ ImageButton val$button;
        final /* synthetic */ int val$resourceId;

        C00949(ImageButton imageButton, int i) {
            this.val$button = imageButton;
            this.val$resourceId = i;
        }

        public void run() {
            this.val$button.setImageResource(this.val$resourceId);
        }
    }

    private class OpModeCountDownTimer {
        public static final long COUNTDOWN_INTERVAL = 30;
        public static final long MILLISECONDS_PER_SECOND = 1000;
        public static final long TICK = 1000;
        public static final long TICK_INTERVAL = 1;
        private long countdown;
        boolean running;
        CountDownTimer timer;

        /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.OpModeCountDownTimer.1 */
        class C00961 implements Runnable {

            /* renamed from: com.qualcomm.ftcdriverstation.FtcDriverStationActivity.OpModeCountDownTimer.1.1 */
            class C00951 extends CountDownTimer {
                C00951(long x0, long x1) {
                    super(x0, x1);
                }

                public void onTick(long timeRemaining) {
                    long timeRemainingInSeconds = timeRemaining / OpModeCountDownTimer.TICK;
                    FtcDriverStationActivity.this.setTextView(FtcDriverStationActivity.this.textTimer, String.valueOf(timeRemainingInSeconds));
                    DbgLog.msg("Running current op mode for " + timeRemainingInSeconds + " seconds");
                }

                public void onFinish() {
                    OpModeCountDownTimer.this.running = false;
                    DbgLog.msg("Stopping current op mode, timer expired");
                    OpModeCountDownTimer.this.setCountdown(OpModeCountDownTimer.COUNTDOWN_INTERVAL);
                    FtcDriverStationActivity.this.setTextView(FtcDriverStationActivity.this.textTimer, BuildConfig.VERSION_NAME);
                    FtcDriverStationActivity.this.setImageResource(FtcDriverStationActivity.this.buttonStartTimed, C0099R.drawable.icon_timeroff);
                    FtcDriverStationActivity.this.handleOpModeStop();
                }
            }

            C00961() {
            }

            public void run() {
                OpModeCountDownTimer.this.timer = new C00951(OpModeCountDownTimer.this.countdown, OpModeCountDownTimer.TICK).start();
            }
        }

        private OpModeCountDownTimer() {
            this.countdown = 30000;
            this.timer = null;
            this.running = false;
        }

        public void start() {
            DbgLog.msg("Running current op mode for " + getTimeRemainingInSeconds() + " seconds");
            this.running = true;
            FtcDriverStationActivity.this.runOnUiThread(new C00961());
        }

        public void stop() {
            if (this.running) {
                this.running = false;
                DbgLog.msg("Stopping current op mode timer");
                if (this.timer != null) {
                    this.timer.cancel();
                }
            }
        }

        public long getTimeRemainingInSeconds() {
            return this.countdown / TICK;
        }

        public void setCountdown(long remaining) {
            this.countdown = TICK * remaining;
        }
    }

    private class RecvLoopRunnable implements Runnable {
        private RecvLoopRunnable() {
        }

        public void run() {
            while (true) {
                RobocolDatagram packet = FtcDriverStationActivity.this.socket.recv();
                if (packet != null) {
                    FtcDriverStationActivity.this.lastRecvPacket.reset();
                    switch (AnonymousClass14.f390x658239ea[packet.getMsgType().ordinal()]) {
                        case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                            FtcDriverStationActivity.this.peerDiscoveryEvent(packet);
                            break;
                        case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                            FtcDriverStationActivity.this.heartbeatEvent(packet);
                            break;
                        case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                            FtcDriverStationActivity.this.commandEvent(packet);
                            break;
                        case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                            FtcDriverStationActivity.this.telemetryEvent(packet);
                            break;
                        default:
                            DbgLog.msg("Unhandled message type: " + packet.getMsgType().name());
                            break;
                    }
                } else if (!FtcDriverStationActivity.this.socket.isClosed()) {
                    Thread.yield();
                } else {
                    return;
                }
            }
        }
    }

    private class SendLoopRunnable implements Runnable {
        private static final long GAMEPAD_UPDATE_THRESHOLD = 1000;

        private SendLoopRunnable() {
        }

        public void run() {
            try {
                long now = SystemClock.uptimeMillis();
                if (FtcDriverStationActivity.this.lastRecvPacket.time() > FtcDriverStationActivity.ASSUME_DISCONNECT_TIMER) {
                    if (FtcDriverStationActivity.this.clientConnected) {
                        FtcDriverStationActivity.this.assumeClientDisconnect();
                        return;
                    }
                    return;
                }
                if (FtcDriverStationActivity.this.heartbeatSend.getElapsedTime() > 0.1d) {
                    FtcDriverStationActivity ftcDriverStationActivity = FtcDriverStationActivity.this;
                    ftcDriverStationActivity.heartbeatSend = new Heartbeat();
                    RobocolDatagram packetHeartbeat = new RobocolDatagram((RobocolParsable) FtcDriverStationActivity.this.heartbeatSend);
                    FtcDriverStationActivity.this.socket.send(packetHeartbeat);
                }
                for (Entry<Integer, Integer> userEntry : FtcDriverStationActivity.this.userToGamepadMap.entrySet()) {
                    int user = ((Integer) userEntry.getKey()).intValue();
                    int id = ((Integer) userEntry.getValue()).intValue();
                    RobocolParsable gamepad = (Gamepad) FtcDriverStationActivity.this.gamepads.get(Integer.valueOf(id));
                    gamepad.user = (byte) user;
                    if (now - gamepad.timestamp <= GAMEPAD_UPDATE_THRESHOLD || !gamepad.atRest()) {
                        RobocolDatagram packetGamepad = new RobocolDatagram(gamepad);
                        FtcDriverStationActivity.this.socket.send(packetGamepad);
                    }
                }
                Iterator<Command> i = FtcDriverStationActivity.this.pendingCommands.iterator();
                while (i.hasNext()) {
                    RobocolParsable command = (Command) i.next();
                    if (command.getAttempts() > FtcDriverStationActivity.MAX_COMMAND_ATTEMPTS) {
                        String msg = String.format("Giving up on command %s after %d attempts", new Object[]{command.getName(), Integer.valueOf(FtcDriverStationActivity.MAX_COMMAND_ATTEMPTS)});
                        FtcDriverStationActivity.this.showToast(msg, 0);
                        i.remove();
                    } else {
                        if (!command.isAcknowledged()) {
                            DbgLog.msg("    sending command: " + command.getName() + ", attempt: " + command.getAttempts());
                        }
                        RobocolDatagram packetCommand = new RobocolDatagram(command);
                        FtcDriverStationActivity.this.socket.send(packetCommand);
                        if (command.isAcknowledged()) {
                            FtcDriverStationActivity.this.pendingCommands.remove(command);
                        }
                    }
                }
            } catch (RobotCoreException e) {
                e.printStackTrace();
            }
        }
    }

    private class SetupRunnable implements Runnable {
        private SetupRunnable() {
        }

        public void run() {
            try {
                if (FtcDriverStationActivity.this.socket != null) {
                    FtcDriverStationActivity.this.socket.close();
                }
                FtcDriverStationActivity.this.socket = new RobocolDatagramSocket();
                FtcDriverStationActivity.this.socket.listen(FtcDriverStationActivity.this.wifiDirect.getGroupOwnerAddress());
                FtcDriverStationActivity.this.socket.connect(FtcDriverStationActivity.this.wifiDirect.getGroupOwnerAddress());
            } catch (SocketException e) {
                DbgLog.error("Failed to open socket: " + e.toString());
            }
            if (FtcDriverStationActivity.this.peerDiscoveryManager != null) {
                FtcDriverStationActivity.this.peerDiscoveryManager.stop();
            }
            FtcDriverStationActivity.this.peerDiscoveryManager = new PeerDiscoveryManager(FtcDriverStationActivity.this.socket);
            FtcDriverStationActivity.this.peerDiscoveryManager.start(FtcDriverStationActivity.this.wifiDirect.getGroupOwnerAddress());
            FtcDriverStationActivity.this.recvLoopService = Executors.newSingleThreadExecutor();
            FtcDriverStationActivity.this.recvLoopService.execute(new RecvLoopRunnable(null));
            DbgLog.msg("Setup complete");
        }
    }

    public FtcDriverStationActivity() {
        this.clientConnected = false;
        this.gamepads = new HashMap();
        this.userToGamepadMap = new HashMap();
        this.heartbeatSend = new Heartbeat();
        this.heartbeatRecv = new Heartbeat();
        this.sendLoopService = Executors.newSingleThreadScheduledExecutor();
        this.queuedOpMode = OpModeManager.DEFAULT_OP_MODE_NAME;
        this.opModes = new LinkedHashSet();
        this.opModeUseTimer = false;
        this.opModeCountDown = new OpModeCountDownTimer();
        this.pingAverage = new RollingAverage(MAX_COMMAND_ATTEMPTS);
        this.lastUiUpdate = new ElapsedTime();
        this.lastRecvPacket = new ElapsedTime();
        this.pendingCommands = Collections.newSetFromMap(new ConcurrentHashMap());
        this.setupNeeded = true;
        this.enableNetworkTrafficLogging = false;
        this.animationListener_icon1 = new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                FtcDriverStationActivity.this.userIcon_1_active.setImageResource(C0099R.drawable.icon_controller);
            }

            public void onAnimationRepeat(Animation animation) {
                FtcDriverStationActivity.this.userIcon_1_active.setImageResource(C0099R.drawable.icon_controller);
            }
        };
        this.animationListener_icon2 = new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                FtcDriverStationActivity.this.userIcon_2_active.setImageResource(C0099R.drawable.icon_controller);
            }

            public void onAnimationRepeat(Animation animation) {
                FtcDriverStationActivity.this.userIcon_2_active.setImageResource(C0099R.drawable.icon_controller);
            }
        };
    }

    public void updateBatteryLevel(float percent) {
        setTextView(this.dsBatteryInfo, percent + "%");
        setBatteryIcon(percent, this.dsBatteryIcon);
    }

    private void setBatteryIcon(float percent, ImageView icon) {
        runOnUiThread(new C00861(percent, icon));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0099R.layout.activity_ftc_driver_station);
        this.context = this;
        this.utility = new Utility(this);
        this.textDeviceName = (TextView) findViewById(C0099R.id.textDeviceName);
        this.textWifiDirectStatus = (TextView) findViewById(C0099R.id.textWifiDirectStatus);
        this.textPingStatus = (TextView) findViewById(C0099R.id.textPingStatus);
        this.textTelemetry = (TextView) findViewById(C0099R.id.textTelemetry);
        this.systemTelemetry = (TextView) findViewById(C0099R.id.textSystemTelemetry);
        this.rcBatteryTelemetry = (TextView) findViewById(C0099R.id.rcBatteryTelemetry);
        this.rcBatteryIcon = (ImageView) findViewById(C0099R.id.rc_battery_icon);
        this.dsBatteryInfo = (TextView) findViewById(C0099R.id.dsBatteryInfo);
        this.robotBatteryTelemetry = (TextView) findViewById(C0099R.id.robotBatteryTelemetry);
        this.dsBatteryIcon = (ImageView) findViewById(C0099R.id.DS_battery_icon);
        this.immersion = new ImmersiveMode(getWindow().getDecorView());
        this.buttonInit = (ImageButton) findViewById(C0099R.id.buttonInit);
        this.buttonInitStop = (ImageButton) findViewById(C0099R.id.buttonInitStop);
        this.buttonStart = (ImageButton) findViewById(C0099R.id.buttonStart);
        this.controlPanelBack = findViewById(C0099R.id.controlPanel);
        this.batteryInfo = findViewById(C0099R.id.battery_info_layout);
        this.wifiInfo = findViewById(C0099R.id.wifi_info_layout);
        this.userIcon_1_active = (ImageView) findViewById(C0099R.id.user1_icon_clicked);
        this.userIcon_2_active = (ImageView) findViewById(C0099R.id.user2_icon_clicked);
        this.userIcon_1_base = (ImageView) findViewById(C0099R.id.user1_icon_base);
        this.userIcon_2_base = (ImageView) findViewById(C0099R.id.user2_icon_base);
        this.buttonStartTimed = (ImageButton) findViewById(C0099R.id.buttonStartTimed);
        this.textTimer = (TextView) findViewById(C0099R.id.textTimer);
        this.buttonSelect = (Button) findViewById(C0099R.id.buttonSelect);
        this.buttonStop = (ImageButton) findViewById(C0099R.id.buttonStop);
        this.buttonMenu = (ImageButton) findViewById(C0099R.id.menu_buttons);
        this.buttonMenu.setOnClickListener(new C00872());
        PreferenceManager.setDefaultValues(this, C0099R.xml.preferences, false);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.preferences.registerOnSharedPreferenceChangeListener(this);
        this.wifiDirect = WifiDirectAssistant.getWifiDirectAssistant(getApplicationContext());
        this.wifiDirect.setCallback(this);
        this.analytics = new Analytics(this.context, Analytics.DS_COMMAND_STRING, new HardwareMap());
        this.batteryChecker = new BatteryChecker(this, this, (long) 300000);
        this.batteryChecker.startBatteryMonitoring();
    }

    protected void onStart() {
        super.onStart();
        RobotLog.writeLogcatToDisk(this, MAX_LOG_SIZE);
        wifiDirectStatus("Disconnected");
        this.groupOwnerMac = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(C0099R.string.pref_driver_station_mac), getString(C0099R.string.pref_driver_station_mac_default));
        assumeClientDisconnect();
        this.wifiDirect.enable();
        if (!this.wifiDirect.isConnected()) {
            this.wifiDirect.discoverPeers();
        } else if (!this.groupOwnerMac.equalsIgnoreCase(this.wifiDirect.getGroupOwnerMacAddress())) {
            DbgLog.error("Wifi Direct - connected to " + this.wifiDirect.getGroupOwnerMacAddress() + ", expected " + this.groupOwnerMac);
            wifiDirectStatus("Error: Connected to wrong device");
            startActivity(new Intent(getBaseContext(), ConfigWifiDirectActivity.class));
            return;
        }
        setVisibility(this.userIcon_1_active, 4);
        setVisibility(this.userIcon_2_active, 4);
        setVisibility(this.userIcon_1_base, 4);
        setVisibility(this.userIcon_2_base, 4);
        DbgLog.msg("App Started");
    }

    protected void onResume() {
        super.onResume();
        this.analytics.register();
        this.setupNeeded = true;
        this.enableNetworkTrafficLogging = this.preferences.getBoolean(getString(C0099R.string.pref_log_network_traffic_key), false);
        this.wifiDirect.setCallback(this);
        if (this.wifiDirect.isConnected()) {
            RobotLog.m329i("Spoofing a wifi direct event...");
            onWifiDirectEvent(Event.CONNECTION_INFO_AVAILABLE);
        }
    }

    protected void onPause() {
        super.onPause();
        this.analytics.unregister();
        this.userToGamepadMap.clear();
        this.pendingCommands.add(new Command(CommandList.CMD_INIT_OP_MODE, OpModeManager.DEFAULT_OP_MODE_NAME));
    }

    protected void onStop() {
        super.onStop();
        this.wifiDirect.disable();
        shutdown();
        DbgLog.msg("App Stopped");
        RobotLog.cancelWriteLogcatToDisk(this);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            this.immersion.cancelSystemUIHide();
        } else if (ImmersiveMode.apiOver19()) {
            this.immersion.hideSystemUI();
        }
    }

    public void showToast(String msg, int duration) {
        showToast(Toast.makeText(this.context, msg, duration));
    }

    public void showToast(Toast toast) {
        runOnUiThread(new C00883(toast));
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(this.context.getString(C0099R.string.pref_gamepad_user1_type_key))) {
            this.gamepads.remove(this.userToGamepadMap.get(Integer.valueOf(1)));
            this.userToGamepadMap.remove(Integer.valueOf(1));
        } else if (key.equals(this.context.getString(C0099R.string.pref_gamepad_user2_type_key))) {
            this.gamepads.remove(this.userToGamepadMap.get(Integer.valueOf(2)));
            this.userToGamepadMap.remove(Integer.valueOf(2));
        } else if (key.equals(this.context.getString(C0099R.string.pref_log_network_traffic_key))) {
            this.enableNetworkTrafficLogging = this.preferences.getBoolean(getString(C0099R.string.pref_log_network_traffic_key), false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0099R.menu.ftc_driver_station, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0099R.id.action_settings:
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                return true;
            case C0099R.id.action_restart_robot:
                this.pendingCommands.add(new Command(CommandList.CMD_RESTART_ROBOT));
                return true;
            case C0099R.id.action_about:
                startActivity(new Intent("com.qualcomm.ftccommon.configuration.AboutActivity.intent.action.Launch"));
                return true;
            case C0099R.id.action_exit_app:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (!Gamepad.isGamepadDevice(event.getDeviceId())) {
            return super.dispatchGenericMotionEvent(event);
        }
        handleGamepadEvent(event);
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!Gamepad.isGamepadDevice(event.getDeviceId())) {
            return super.dispatchKeyEvent(event);
        }
        handleGamepadEvent(event);
        return true;
    }

    public void onWifiDirectEvent(Event event) {
        String msg;
        switch (AnonymousClass14.$SwitchMap$com$qualcomm$robotcore$wifi$WifiDirectAssistant$Event[event.ordinal()]) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                if (this.wifiDirect.getConnectStatus() != ConnectStatus.CONNECTED && this.wifiDirect.getConnectStatus() != ConnectStatus.CONNECTING) {
                    if (this.groupOwnerMac.equals(getString(C0099R.string.pref_driver_station_mac_default))) {
                        wifiDirectStatus("Not Paired");
                    } else {
                        wifiDirectStatus("Searching");
                    }
                    for (WifiP2pDevice peer : this.wifiDirect.getPeers()) {
                        if (peer.deviceAddress.equalsIgnoreCase(this.groupOwnerMac)) {
                            this.wifiDirect.connect(peer);
                            return;
                        }
                    }
                }
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                DbgLog.error("Wifi Direct - connected as Group Owner, was expecting Peer");
                wifiDirectStatus("Error: Connected as Group Owner");
                startActivity(new Intent(getBaseContext(), ConfigWifiDirectActivity.class));
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                wifiDirectStatus("Connecting");
                this.wifiDirect.cancelDiscoverPeers();
            case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                this.wifiDirect.cancelDiscoverPeers();
                wifiDirectStatus("Connected");
            case BuildConfig.VERSION_CODE /*5*/:
                wifiDirectStatus(this.wifiDirect.getGroupOwnerName());
                displayDeviceName(this.wifiDirect.getDeviceName());
                if (this.groupOwnerMac.equalsIgnoreCase(this.wifiDirect.getGroupOwnerMacAddress())) {
                    synchronized (this) {
                        if (this.wifiDirect.isConnected() && this.setupNeeded) {
                            this.setupNeeded = false;
                            new Thread(new SetupRunnable()).start();
                        }
                        break;
                    }
                    return;
                }
                DbgLog.error("Wifi Direct - connected to " + this.wifiDirect.getGroupOwnerMacAddress() + ", expected " + this.groupOwnerMac);
                wifiDirectStatus("Error: Connected to wrong device");
                startActivity(new Intent(getBaseContext(), ConfigWifiDirectActivity.class));
            case FT4222_STATUS.FT4222_INVALID_PARAMETER /*6*/:
                msg = "Disconnected";
                wifiDirectStatus(msg);
                DbgLog.msg("Wifi Direct - " + msg);
                this.wifiDirect.discoverPeers();
            case FT4222_STATUS.FT4222_INVALID_BAUD_RATE /*7*/:
                msg = "Error: " + this.wifiDirect.getFailureReason();
                wifiDirectStatus(msg);
                DbgLog.msg("Wifi Direct - " + msg);
            default:
        }
    }

    public void onClickButtonInit(View view) {
        handleOpModeInit();
    }

    public void onClickButtonStart(View view) {
        handleOpModeStart();
    }

    public void onClickButtonStartTimed(View view) {
        if (this.opModeUseTimer) {
            setImageResource(this.buttonStartTimed, C0099R.drawable.icon_timeroff);
            setTextView(this.textTimer, BuildConfig.VERSION_NAME);
        } else {
            setImageResource(this.buttonStartTimed, C0099R.drawable.icon_timeron);
            setTextView(this.textTimer, "30");
            this.opModeCountDown.setCountdown(30);
        }
        this.opModeUseTimer = !this.opModeUseTimer;
    }

    public void onClickButtonSelect(View view) {
        this.opModeCountDown.stop();
        String[] opModeStringArray = new String[this.opModes.size()];
        this.opModes.toArray(opModeStringArray);
        OpModeSelectionDialogFragment opModeSelectionDialogFragment = new OpModeSelectionDialogFragment();
        opModeSelectionDialogFragment.setOnSelectionDialogListener(this);
        opModeSelectionDialogFragment.setOpModes(opModeStringArray);
        opModeSelectionDialogFragment.show(getFragmentManager(), "op_mode_selection");
        setTextView(this.systemTelemetry, BuildConfig.VERSION_NAME);
        setVisibility(this.systemTelemetry, 8);
        this.opModeUseTimer = false;
        setImageResource(this.buttonStartTimed, C0099R.drawable.icon_timeroff);
        setTextView(this.textTimer, BuildConfig.VERSION_NAME);
        this.pendingCommands.add(new Command(CommandList.CMD_INIT_OP_MODE, OpModeManager.DEFAULT_OP_MODE_NAME));
    }

    public void onClickButtonStop(View view) {
        handleOpModeStop();
    }

    public void onSelectionClick(String selection) {
        handleOpModeQueued(selection);
        this.opModeCountDown.setCountdown(30);
        if (this.opModeUseTimer) {
            setTextView(this.textTimer, String.valueOf(this.opModeCountDown.getTimeRemainingInSeconds()));
        } else {
            setTextView(this.textTimer, BuildConfig.VERSION_NAME);
        }
        uiWaitingForInitEvent();
    }

    protected void shutdown() {
        if (this.recvLoopService != null) {
            this.recvLoopService.shutdownNow();
        }
        if (!(this.sendLoopFuture == null || this.sendLoopFuture.isDone())) {
            this.sendLoopFuture.cancel(true);
        }
        if (this.peerDiscoveryManager != null) {
            this.peerDiscoveryManager.stop();
        }
        if (this.socket != null) {
            this.socket.close();
        }
        this.remoteAddr = null;
        this.setupNeeded = true;
        pingStatus(BuildConfig.VERSION_NAME);
    }

    protected void peerDiscoveryEvent(RobocolDatagram packet) {
        if (!packet.getAddress().equals(this.remoteAddr)) {
            this.remoteAddr = packet.getAddress();
            DbgLog.msg("new remote peer discovered: " + this.remoteAddr.getHostAddress());
            try {
                this.socket.connect(this.remoteAddr);
            } catch (SocketException e) {
                DbgLog.error("Unable to connect to peer:" + e.toString());
            }
            if (this.sendLoopFuture == null || this.sendLoopFuture.isDone()) {
                this.sendLoopFuture = this.sendLoopService.scheduleAtFixedRate(new SendLoopRunnable(), 0, 40, TimeUnit.MILLISECONDS);
            }
            assumeClientConnect();
        }
    }

    protected void heartbeatEvent(RobocolDatagram packet) {
        try {
            this.heartbeatRecv.fromByteArray(packet.getData());
            double elapsedTime = this.heartbeatRecv.getElapsedTime();
            int sequenceNumber = this.heartbeatRecv.getSequenceNumber();
            this.robotState = RobotState.fromByte(this.heartbeatRecv.getRobotState());
            this.pingAverage.addNumber((int) (1000.0d * elapsedTime));
            if (this.enableNetworkTrafficLogging) {
                DbgLog.msg(String.format("Network - num: %5d, time: %7.4f", new Object[]{Integer.valueOf(sequenceNumber), Double.valueOf(elapsedTime)}));
            }
            if (this.lastUiUpdate.time() > 0.5d) {
                this.lastUiUpdate.reset();
                pingStatus(String.format("%3dms", new Object[]{Integer.valueOf(this.pingAverage.getAverage())}));
            }
        } catch (RobotCoreException e) {
            DbgLog.logStacktrace(e);
        }
    }

    protected void commandEvent(RobocolDatagram packet) {
        try {
            Command command = new Command(packet.getData());
            if (command.isAcknowledged()) {
                this.pendingCommands.remove(command);
                return;
            }
            DbgLog.msg(" processing command: " + command.getName());
            command.acknowledge();
            this.pendingCommands.add(command);
            String name = command.getName();
            String extra = command.getExtra();
            if (name.equals(CommandList.CMD_REQUEST_OP_MODE_LIST_RESP)) {
                handleCommandRequestOpModeListResp(extra);
            } else if (name.equals(CommandList.CMD_INIT_OP_MODE_RESP)) {
                handleCommandInitOpModeResp(extra);
            } else if (name.equals(CommandList.CMD_RUN_OP_MODE_RESP)) {
                handleCommandStartOpModeResp(extra);
            } else {
                DbgLog.msg("Unable to process command " + name);
            }
        } catch (RobotCoreException e) {
            DbgLog.logStacktrace(e);
        }
    }

    protected void telemetryEvent(RobocolDatagram packet) {
        String telemetryString = BuildConfig.VERSION_NAME;
        try {
            Telemetry telemetry = new Telemetry(packet.getData());
            Map<String, String> strings = telemetry.getDataStrings();
            for (String key : new TreeSet(strings.keySet())) {
                telemetryString = telemetryString + key + ": " + ((String) strings.get(key)) + "\n";
            }
            telemetryString = telemetryString + "\n";
            Map<String, Float> numbers = telemetry.getDataNumbers();
            for (String key2 : new TreeSet(numbers.keySet())) {
                telemetryString = telemetryString + key2 + ": " + numbers.get(key2) + "\n";
            }
            String tag = telemetry.getTag();
            if (tag.equals(EventLoopManager.SYSTEM_TELEMETRY)) {
                DbgLog.error("System Telemetry event: " + telemetryString);
                RobotLog.setGlobalErrorMsg(telemetryString);
                setVisibility(this.systemTelemetry, 0);
                setTextView(this.systemTelemetry, "Robot Status: " + this.robotState + "\n" + "To recover, please restart the robot." + "\n" + "Error: " + ((String) strings.get(tag)));
                uiRobotNeedsRestart();
            } else if (tag.equals(EventLoopManager.RC_BATTERY_LEVEL_KEY)) {
                DbgLog.msg("RC battery Telemetry event: " + ((String) strings.get(tag)));
                setTextView(this.rcBatteryTelemetry, ((String) strings.get(tag)) + "%");
                setBatteryIcon(Float.parseFloat((String) strings.get(tag)), this.rcBatteryIcon);
            } else if (tag.equals(EventLoopManager.ROBOT_BATTERY_LEVEL_KEY)) {
                String value = (String) strings.get(tag);
                DbgLog.msg("Robot Battery Telemetry event: " + value);
                setTextView(this.robotBatteryTelemetry, value);
                ImageView robotBatteryIcon = (ImageView) findViewById(C0099R.id.robot_battery_icon);
                if (value.equals(FtcEventLoopHandler.NO_VOLTAGE_SENSOR)) {
                    setVisibility(robotBatteryIcon, 8);
                } else {
                    setVisibility(robotBatteryIcon, 0);
                }
            } else {
                setTextView(this.textTelemetry, telemetryString);
            }
        } catch (RobotCoreException e) {
            DbgLog.logStacktrace(e);
        }
    }

    protected void uiRobotNeedsRestart() {
        setOpacity(this.buttonSelect, PARTLY_OPAQUE);
        setEnabled(this.buttonSelect, false);
        setEnabled(this.buttonInit, false);
        setVisibility(this.buttonInit, 0);
        setVisibility(this.buttonStart, 4);
        setVisibility(this.buttonStop, 4);
        setVisibility(this.buttonInitStop, 4);
        setVisibility(this.buttonStartTimed, 4);
        setVisibility(this.textTimer, 4);
    }

    protected void uiRobotControllerIsDisconnected() {
        setOpacity(this.controlPanelBack, PARTLY_OPAQUE);
        setOpacity(this.wifiInfo, PARTLY_OPAQUE);
        setOpacity(this.batteryInfo, PARTLY_OPAQUE);
        setEnabled(this.buttonSelect, false);
        setOpacity(this.buttonSelect, PARTLY_OPAQUE);
        setEnabled(this.buttonInit, false);
        setVisibility(this.buttonInit, 0);
        setVisibility(this.buttonStart, 4);
        setVisibility(this.buttonStop, 4);
        setVisibility(this.buttonInitStop, 4);
        setVisibility(this.buttonStartTimed, 4);
        setVisibility(this.textTimer, 4);
    }

    protected void uiRobotControllerIsConnected() {
        setOpacity(this.wifiInfo, FULLY_OPAQUE);
        setOpacity(this.batteryInfo, FULLY_OPAQUE);
        setOpacity(this.buttonSelect, FULLY_OPAQUE);
        setTextView(this.textTelemetry, BuildConfig.VERSION_NAME);
        setTextView(this.systemTelemetry, BuildConfig.VERSION_NAME);
        setVisibility(this.systemTelemetry, 8);
        setTextView(this.rcBatteryTelemetry, BuildConfig.VERSION_NAME);
        setTextView(this.robotBatteryTelemetry, BuildConfig.VERSION_NAME);
    }

    protected void uiWaitingForOpModeSelection() {
        setEnabled(this.buttonSelect, true);
        setOpacity(this.buttonSelect, FULLY_OPAQUE);
        setButtonText(this.buttonSelect, "Select Op Mode");
        setOpacity(this.controlPanelBack, PARTLY_OPAQUE);
        setEnabled(this.buttonInit, false);
        setVisibility(this.buttonInit, 0);
        setVisibility(this.buttonStart, 4);
        setVisibility(this.buttonStop, 4);
        setVisibility(this.buttonInitStop, 4);
        setVisibility(this.buttonStartTimed, 4);
        setVisibility(this.textTimer, 4);
    }

    protected void uiWaitingForInitEvent() {
        setOpacity(this.controlPanelBack, FULLY_OPAQUE);
        setButtonText(this.buttonSelect, this.queuedOpMode);
        setEnabled(this.buttonSelect, true);
        setOpacity(this.buttonSelect, FULLY_OPAQUE);
        setEnabled(this.buttonInit, true);
        setVisibility(this.buttonInit, 0);
        setVisibility(this.buttonStart, 4);
        setVisibility(this.buttonStop, 4);
        setVisibility(this.buttonInitStop, 4);
        setEnabled(this.buttonStartTimed, true);
        setVisibility(this.buttonStartTimed, 0);
        setVisibility(this.textTimer, 0);
    }

    protected void uiWaitingForStartEvent() {
        setButtonText(this.buttonSelect, this.queuedOpMode);
        setEnabled(this.buttonSelect, true);
        setOpacity(this.buttonSelect, FULLY_OPAQUE);
        setVisibility(this.buttonStart, 0);
        setVisibility(this.buttonInit, 4);
        setVisibility(this.buttonStop, 4);
        setVisibility(this.buttonInitStop, 0);
        setEnabled(this.buttonStartTimed, true);
        setVisibility(this.buttonStartTimed, 0);
        setVisibility(this.textTimer, 0);
    }

    protected void uiWaitingForStopEvent() {
        setButtonText(this.buttonSelect, this.queuedOpMode);
        setEnabled(this.buttonSelect, true);
        setOpacity(this.buttonSelect, FULLY_OPAQUE);
        setVisibility(this.buttonStop, 0);
        setVisibility(this.buttonInit, 4);
        setVisibility(this.buttonStart, 4);
        setVisibility(this.buttonInitStop, 4);
        setEnabled(this.buttonStartTimed, false);
        setVisibility(this.buttonStartTimed, 0);
        setVisibility(this.textTimer, 0);
    }

    protected void assumeClientConnect() {
        DbgLog.msg("Assuming client connected");
        this.clientConnected = true;
        uiRobotControllerIsConnected();
        this.pendingCommands.add(new Command(CommandList.CMD_REQUEST_OP_MODE_LIST));
    }

    protected void assumeClientDisconnect() {
        DbgLog.msg("Assuming client disconnected");
        this.clientConnected = false;
        this.opModeUseTimer = false;
        this.opModeCountDown.stop();
        this.opModeCountDown.setCountdown(30);
        setTextView(this.textTimer, BuildConfig.VERSION_NAME);
        setImageResource(this.buttonStartTimed, C0099R.drawable.icon_timeroff);
        this.queuedOpMode = OpModeManager.DEFAULT_OP_MODE_NAME;
        this.opModes.clear();
        pingStatus(" ");
        this.pendingCommands.clear();
        this.remoteAddr = null;
        RobotLog.clearGlobalErrorMsg();
        uiRobotControllerIsDisconnected();
    }

    protected void handleOpModeQueued(String queuedOpMode) {
        this.queuedOpMode = queuedOpMode;
        setButtonText(this.buttonSelect, queuedOpMode);
    }

    protected void handleOpModeStop() {
        this.opModeCountDown.stop();
        if (!this.textTimer.getText().toString().isEmpty()) {
            this.opModeCountDown.setCountdown(Long.parseLong(this.textTimer.getText().toString()));
        }
        uiWaitingForInitEvent();
        this.pendingCommands.add(new Command(CommandList.CMD_INIT_OP_MODE, OpModeManager.DEFAULT_OP_MODE_NAME));
    }

    protected void clearInfo() {
        setVisibility(this.systemTelemetry, 8);
        setTextView(this.textTelemetry, BuildConfig.VERSION_NAME);
    }

    protected void handleOpModeInit() {
        this.opModeCountDown.stop();
        uiWaitingForStartEvent();
        this.pendingCommands.add(new Command(CommandList.CMD_INIT_OP_MODE, this.queuedOpMode));
        clearInfo();
    }

    protected void handleOpModeStart() {
        this.opModeCountDown.stop();
        uiWaitingForStopEvent();
        this.pendingCommands.add(new Command(CommandList.CMD_RUN_OP_MODE, this.queuedOpMode));
        clearInfo();
    }

    protected void handleCommandRequestOpModeListResp(String extra) {
        this.opModes = new LinkedHashSet();
        this.opModes.addAll(Arrays.asList(extra.split(Util.ASCII_RECORD_SEPARATOR)));
        DbgLog.msg("Received the following op modes: " + this.opModes.toString());
        this.pendingCommands.add(new Command(CommandList.CMD_INIT_OP_MODE, OpModeManager.DEFAULT_OP_MODE_NAME));
        uiWaitingForOpModeSelection();
    }

    protected void handleCommandInitOpModeResp(String extra) {
        DbgLog.msg("Robot Controller initializing op mode: " + extra);
        if (!extra.equals(OpModeManager.DEFAULT_OP_MODE_NAME)) {
            uiWaitingForStartEvent();
        } else if (this.queuedOpMode.equals(OpModeManager.DEFAULT_OP_MODE_NAME)) {
            uiWaitingForOpModeSelection();
        } else {
            uiWaitingForInitEvent();
            this.pendingCommands.add(new Command(CommandList.CMD_RUN_OP_MODE, OpModeManager.DEFAULT_OP_MODE_NAME));
        }
    }

    protected void handleCommandStartOpModeResp(String extra) {
        DbgLog.msg("Robot Controller starting op mode: " + extra);
        if (!extra.equals(OpModeManager.DEFAULT_OP_MODE_NAME)) {
            uiWaitingForStopEvent();
        }
        if (this.opModeUseTimer && !extra.equals(OpModeManager.DEFAULT_OP_MODE_NAME)) {
            this.opModeCountDown.start();
        }
    }

    public void onClickRCBatteryToast(View v) {
        showToast("Robot Controller battery", 0);
    }

    public void onClickRobotBatteryToast(View v) {
        showToast("Robot battery", 0);
    }

    public void onClickDSBatteryToast(View v) {
        showToast("Driver Station battery", 0);
    }

    protected void wifiDirectStatus(String status) {
        runOnUiThread(new C00894(status));
    }

    protected void displayDeviceName(String name) {
        runOnUiThread(new C00905(name));
    }

    protected void setButtonText(Button button, String text) {
        runOnUiThread(new C00916(button, text));
    }

    protected void setTextView(TextView textView, String text) {
        runOnUiThread(new C00927(textView, text));
    }

    protected void setOpacity(View v, float opacity) {
        runOnUiThread(new C00938(v, opacity));
    }

    protected void setImageResource(ImageButton button, int resourceId) {
        runOnUiThread(new C00949(button, resourceId));
    }

    protected void setVisibility(View view, int visibility) {
        runOnUiThread(new AnonymousClass10(view, visibility));
    }

    protected void setEnabled(View view, boolean enabled) {
        runOnUiThread(new AnonymousClass11(view, enabled));
    }

    protected void pingStatus(String status) {
        setTextView(this.textPingStatus, status);
    }

    protected synchronized void handleGamepadEvent(MotionEvent event) {
        Gamepad gamepad = (Gamepad) this.gamepads.get(Integer.valueOf(event.getDeviceId()));
        if (gamepad != null) {
            gamepad.update(event);
            indicateGamepad(event);
        }
    }

    protected void indicateGamepad(InputEvent event) {
        for (Entry<Integer, Integer> entry : this.userToGamepadMap.entrySet()) {
            if (((Integer) entry.getValue()).intValue() == event.getDeviceId()) {
                Animation fadeout = AnimationUtils.loadAnimation(this.context, C0099R.anim.fadeout);
                if (((Integer) entry.getKey()).intValue() == 1) {
                    this.userIcon_1_active.setImageResource(C0099R.drawable.icon_controlleractive);
                    fadeout.setAnimationListener(this.animationListener_icon1);
                    this.userIcon_1_active.startAnimation(fadeout);
                } else if (((Integer) entry.getKey()).intValue() == 2) {
                    this.userIcon_2_active.setImageResource(C0099R.drawable.icon_controlleractive);
                    fadeout.setAnimationListener(this.animationListener_icon2);
                    this.userIcon_2_active.startAnimation(fadeout);
                }
            }
        }
    }

    protected synchronized void handleGamepadEvent(KeyEvent event) {
        if (!this.gamepads.containsKey(Integer.valueOf(event.getDeviceId()))) {
            this.gamepads.put(Integer.valueOf(event.getDeviceId()), new Gamepad());
        }
        Gamepad gamepad = (Gamepad) this.gamepads.get(Integer.valueOf(event.getDeviceId()));
        gamepad.update(event);
        indicateGamepad(event);
        if (gamepad.start && (gamepad.f684a || gamepad.f685b)) {
            int user = -1;
            if (gamepad.f684a) {
                user = 1;
                setVisibility(this.userIcon_1_base, 0);
            }
            if (gamepad.f685b) {
                user = 2;
                setVisibility(this.userIcon_2_base, 0);
            }
            assignNewGamepad(user, event.getDeviceId());
        }
    }

    protected void initGamepad(int user, int gamepadId) {
        Gamepad gamepad;
        String key = BuildConfig.VERSION_NAME;
        switch (user) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                key = getString(C0099R.string.pref_gamepad_user1_type_key);
                break;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                key = getString(C0099R.string.pref_gamepad_user2_type_key);
                break;
        }
        String gamepadType = this.preferences.getString(key, getString(C0099R.string.gamepad_default));
        if (gamepadType.equals(getString(C0099R.string.gamepad_logitech_f310))) {
            gamepad = new LogitechGamepadF310();
        } else if (gamepadType.equals(getString(C0099R.string.gamepad_microsoft_xbox_360))) {
            gamepad = new MicrosoftGamepadXbox360();
        } else {
            gamepad = new Gamepad();
        }
        gamepad.id = gamepadId;
        gamepad.timestamp = SystemClock.uptimeMillis();
        this.gamepads.put(Integer.valueOf(gamepadId), gamepad);
    }

    protected void assignNewGamepad(int user, int gamepadId) {
        Set<Integer> duplicates = new HashSet();
        for (Entry<Integer, Integer> entry : this.userToGamepadMap.entrySet()) {
            if (((Integer) entry.getValue()).intValue() == gamepadId) {
                duplicates.add(entry.getKey());
            }
        }
        for (Integer i : duplicates) {
            this.userToGamepadMap.remove(i);
        }
        this.userToGamepadMap.put(Integer.valueOf(user), Integer.valueOf(gamepadId));
        initGamepad(user, gamepadId);
        DbgLog.msg(String.format("Gamepad %d detected as %s (ID %d)", new Object[]{Integer.valueOf(user), ((Gamepad) this.gamepads.get(Integer.valueOf(gamepadId))).type(), Integer.valueOf(gamepadId)}));
    }
}
