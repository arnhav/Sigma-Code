package com.qualcomm.robotcore.eventloop;

import com.qualcomm.ftcdriverstation.FtcDriverStationActivity;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.Heartbeat.Token;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.PeerDiscovery.PeerType;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.robocol.RobocolParsable.MsgType;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventLoopManager {
    public static final String RC_BATTERY_LEVEL_KEY = "RobotController Battery Level";
    public static final String ROBOT_BATTERY_LEVEL_KEY = "Robot Battery Level";
    public static final String SYSTEM_TELEMETRY = "SYSTEM_TELEMETRY";
    private static final EventLoop f398a;
    private Thread f399b;
    private Thread f400c;
    private final RobocolDatagramSocket f401d;
    private boolean f402e;
    private ElapsedTime f403f;
    private EventLoop f404g;
    private final Gamepad[] f405h;
    private Heartbeat f406i;
    private EventLoopMonitor f407j;
    private final Set<SyncdDevice> f408k;
    private final Command[] f409l;
    private int f410m;
    private final Set<Command> f411n;
    private InetAddress f412o;
    public RobotState state;

    /* renamed from: com.qualcomm.robotcore.eventloop.EventLoopManager.1 */
    static /* synthetic */ class C01011 {
        static final /* synthetic */ int[] f391a;

        static {
            f391a = new int[MsgType.values().length];
            try {
                f391a[MsgType.GAMEPAD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f391a[MsgType.HEARTBEAT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f391a[MsgType.PEER_DISCOVERY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f391a[MsgType.COMMAND.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f391a[MsgType.EMPTY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public interface EventLoopMonitor {
        void onStateChange(RobotState robotState);
    }

    public enum State {
        NOT_STARTED,
        INIT,
        RUNNING,
        STOPPED,
        EMERGENCY_STOP,
        DROPPED_CONNECTION
    }

    /* renamed from: com.qualcomm.robotcore.eventloop.EventLoopManager.b */
    private class C0102b implements Runnable {
        final /* synthetic */ EventLoopManager f393a;

        private C0102b(EventLoopManager eventLoopManager) {
            this.f393a = eventLoopManager;
        }

        public void run() {
            RobotLog.m330v("EventLoopRunnable has started");
            try {
                ElapsedTime elapsedTime = new ElapsedTime();
                while (!Thread.interrupted()) {
                    while (elapsedTime.time() < 0.001d) {
                        Thread.sleep(5);
                    }
                    elapsedTime.reset();
                    if (RobotLog.hasGlobalErrorMsg()) {
                        this.f393a.buildAndSendTelemetry(EventLoopManager.SYSTEM_TELEMETRY, RobotLog.getGlobalErrorMsg());
                    }
                    if (this.f393a.f403f.startTime() == 0.0d) {
                        Thread.sleep(500);
                    } else if (this.f393a.f403f.time() > FtcDriverStationActivity.ASSUME_DISCONNECT_TIMER) {
                        this.f393a.handleDroppedConnection();
                        this.f393a.f412o = null;
                        this.f393a.f403f = new ElapsedTime(0);
                    }
                    for (SyncdDevice blockUntilReady : this.f393a.f408k) {
                        blockUntilReady.blockUntilReady();
                    }
                    try {
                        this.f393a.f404g.loop();
                        for (SyncdDevice blockUntilReady2 : this.f393a.f408k) {
                            blockUntilReady2.startBlockingWork();
                        }
                    } catch (Exception e) {
                        RobotLog.m328e("Event loop threw an exception");
                        RobotLog.logStacktrace(e);
                        RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + (e.getClass().getSimpleName() + (e.getMessage() != null ? " - " + e.getMessage() : BuildConfig.VERSION_NAME)));
                        this.f393a.buildAndSendTelemetry(EventLoopManager.SYSTEM_TELEMETRY, RobotLog.getGlobalErrorMsg());
                        throw new RobotCoreException("EventLoop Exception in loop()");
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        for (SyncdDevice blockUntilReady22 : this.f393a.f408k) {
                            blockUntilReady22.startBlockingWork();
                        }
                    }
                }
            } catch (InterruptedException e2) {
                RobotLog.m330v("EventLoopRunnable interrupted");
                this.f393a.m268a(RobotState.STOPPED);
            } catch (RobotCoreException e3) {
                RobotLog.m330v("RobotCoreException in EventLoopManager: " + e3.getMessage());
                this.f393a.m268a(RobotState.EMERGENCY_STOP);
                this.f393a.buildAndSendTelemetry(EventLoopManager.SYSTEM_TELEMETRY, RobotLog.getGlobalErrorMsg());
            }
            try {
                this.f393a.f404g.teardown();
            } catch (Exception e4) {
                RobotLog.m331w("Caught exception during looper teardown: " + e4.toString());
                RobotLog.logStacktrace(e4);
                if (RobotLog.hasGlobalErrorMsg()) {
                    this.f393a.buildAndSendTelemetry(EventLoopManager.SYSTEM_TELEMETRY, RobotLog.getGlobalErrorMsg());
                }
            }
            RobotLog.m330v("EventLoopRunnable has exited");
        }
    }

    /* renamed from: com.qualcomm.robotcore.eventloop.EventLoopManager.c */
    private class C0103c implements Runnable {
        ElapsedTime f394a;
        final /* synthetic */ EventLoopManager f395b;

        private C0103c(EventLoopManager eventLoopManager) {
            this.f395b = eventLoopManager;
            this.f394a = new ElapsedTime();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r4 = this;
        L_0x0000:
            r0 = r4.f395b;
            r0 = r0.f401d;
            r0 = r0.recv();
            r1 = r4.f395b;
            r1 = r1.f402e;
            if (r1 != 0) goto L_0x001e;
        L_0x0012:
            r1 = r4.f395b;
            r1 = r1.f401d;
            r1 = r1.isClosed();
            if (r1 == 0) goto L_0x001f;
        L_0x001e:
            return;
        L_0x001f:
            if (r0 != 0) goto L_0x0025;
        L_0x0021:
            java.lang.Thread.yield();
            goto L_0x0000;
        L_0x0025:
            r1 = com.qualcomm.robotcore.util.RobotLog.hasGlobalErrorMsg();
            if (r1 == 0) goto L_0x0036;
        L_0x002b:
            r1 = r4.f395b;
            r2 = "SYSTEM_TELEMETRY";
            r3 = com.qualcomm.robotcore.util.RobotLog.getGlobalErrorMsg();
            r1.buildAndSendTelemetry(r2, r3);
        L_0x0036:
            r1 = com.qualcomm.robotcore.eventloop.EventLoopManager.C01011.f391a;	 Catch:{ RobotCoreException -> 0x004b }
            r2 = r0.getMsgType();	 Catch:{ RobotCoreException -> 0x004b }
            r2 = r2.ordinal();	 Catch:{ RobotCoreException -> 0x004b }
            r1 = r1[r2];	 Catch:{ RobotCoreException -> 0x004b }
            switch(r1) {
                case 1: goto L_0x0067;
                case 2: goto L_0x006d;
                case 3: goto L_0x0073;
                case 4: goto L_0x0079;
                case 5: goto L_0x007f;
                default: goto L_0x0045;
            };	 Catch:{ RobotCoreException -> 0x004b }
        L_0x0045:
            r1 = r4.f395b;	 Catch:{ RobotCoreException -> 0x004b }
            r1.m282e(r0);	 Catch:{ RobotCoreException -> 0x004b }
            goto L_0x0000;
        L_0x004b:
            r0 = move-exception;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "RobotCore event loop cannot process event: ";
            r1 = r1.append(r2);
            r0 = r0.toString();
            r0 = r1.append(r0);
            r0 = r0.toString();
            com.qualcomm.robotcore.util.RobotLog.m331w(r0);
            goto L_0x0000;
        L_0x0067:
            r1 = r4.f395b;	 Catch:{ RobotCoreException -> 0x004b }
            r1.m267a(r0);	 Catch:{ RobotCoreException -> 0x004b }
            goto L_0x0000;
        L_0x006d:
            r1 = r4.f395b;	 Catch:{ RobotCoreException -> 0x004b }
            r1.m272b(r0);	 Catch:{ RobotCoreException -> 0x004b }
            goto L_0x0000;
        L_0x0073:
            r1 = r4.f395b;	 Catch:{ RobotCoreException -> 0x004b }
            r1.m275c(r0);	 Catch:{ RobotCoreException -> 0x004b }
            goto L_0x0000;
        L_0x0079:
            r1 = r4.f395b;	 Catch:{ RobotCoreException -> 0x004b }
            r1.m279d(r0);	 Catch:{ RobotCoreException -> 0x004b }
            goto L_0x0000;
        L_0x007f:
            r0 = r4.f395b;	 Catch:{ RobotCoreException -> 0x004b }
            r0.m273c();	 Catch:{ RobotCoreException -> 0x004b }
            goto L_0x0000;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.qualcomm.robotcore.eventloop.EventLoopManager.c.run():void");
        }
    }

    /* renamed from: com.qualcomm.robotcore.eventloop.EventLoopManager.d */
    private class C0104d implements Runnable {
        final /* synthetic */ EventLoopManager f396a;
        private Set<Command> f397b;

        private C0104d(EventLoopManager eventLoopManager) {
            this.f396a = eventLoopManager;
            this.f397b = new HashSet();
        }

        public void run() {
            while (!Thread.interrupted()) {
                for (Command command : this.f396a.f411n) {
                    if (command.getAttempts() > 10) {
                        RobotLog.m331w("Failed to send command, too many attempts: " + command.toString());
                        this.f397b.add(command);
                    } else if (command.isAcknowledged()) {
                        RobotLog.m330v("Command " + command.getName() + " has been acknowledged by remote device");
                        this.f397b.add(command);
                    } else {
                        try {
                            RobotLog.m330v("Sending command: " + command.getName() + ", attempt " + command.getAttempts());
                            this.f396a.f401d.send(new RobocolDatagram(command.toByteArray()));
                        } catch (RobotCoreException e) {
                            RobotLog.m331w("Failed to send command " + command.getName());
                            RobotLog.logStacktrace(e);
                        }
                    }
                }
                this.f396a.f411n.removeAll(this.f397b);
                this.f397b.clear();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e2) {
                    return;
                }
            }
        }
    }

    /* renamed from: com.qualcomm.robotcore.eventloop.EventLoopManager.a */
    private static class C0145a implements EventLoop {
        private C0145a() {
        }

        public void init(EventLoopManager eventProcessor) {
        }

        public void loop() {
        }

        public void teardown() {
        }

        public void processCommand(Command command) {
            RobotLog.m331w("Dropping command " + command.getName() + ", no active event loop");
        }

        public OpModeManager getOpModeManager() {
            return null;
        }
    }

    static {
        f398a = new C0145a();
    }

    public void handleDroppedConnection() {
        OpModeManager opModeManager = this.f404g.getOpModeManager();
        String str = "Lost connection while running op mode: " + opModeManager.getActiveOpModeName();
        resetGamepads();
        opModeManager.initActiveOpMode(OpModeManager.DEFAULT_OP_MODE_NAME);
        m268a(RobotState.DROPPED_CONNECTION);
        RobotLog.m329i(str);
    }

    public EventLoopManager(RobocolDatagramSocket socket) {
        this.state = RobotState.NOT_STARTED;
        this.f399b = new Thread();
        this.f400c = new Thread();
        this.f402e = false;
        this.f403f = new ElapsedTime();
        this.f404g = f398a;
        this.f405h = new Gamepad[]{new Gamepad(), new Gamepad()};
        this.f406i = new Heartbeat(Token.EMPTY);
        this.f407j = null;
        this.f408k = new CopyOnWriteArraySet();
        this.f409l = new Command[8];
        this.f410m = 0;
        this.f411n = new CopyOnWriteArraySet();
        this.f401d = socket;
        m268a(RobotState.NOT_STARTED);
    }

    public void setMonitor(EventLoopMonitor monitor) {
        this.f407j = monitor;
    }

    public void start(EventLoop eventLoop) throws RobotCoreException {
        this.f402e = false;
        setEventLoop(eventLoop);
        this.f400c = new Thread(new C0104d(), "Scheduled Sends");
        this.f400c.start();
        new Thread(new C0103c()).start();
    }

    public void shutdown() {
        this.f401d.close();
        this.f400c.interrupt();
        this.f402e = true;
        m270b();
    }

    public void registerSyncdDevice(SyncdDevice device) {
        this.f408k.add(device);
    }

    public void unregisterSyncdDevice(SyncdDevice device) {
        this.f408k.remove(device);
    }

    public void setEventLoop(EventLoop eventLoop) throws RobotCoreException {
        if (eventLoop == null) {
            eventLoop = f398a;
            RobotLog.m327d("Event loop cannot be null, using empty event loop");
        }
        m270b();
        this.f404g = eventLoop;
        m264a();
    }

    public EventLoop getEventLoop() {
        return this.f404g;
    }

    public Gamepad getGamepad() {
        return getGamepad(0);
    }

    public Gamepad getGamepad(int port) {
        Range.throwIfRangeIsInvalid((double) port, 0.0d, Servo.MAX_POSITION);
        return this.f405h[port];
    }

    public Gamepad[] getGamepads() {
        return this.f405h;
    }

    public void resetGamepads() {
        for (Gamepad reset : this.f405h) {
            reset.reset();
        }
    }

    public Heartbeat getHeartbeat() {
        return this.f406i;
    }

    public void sendTelemetryData(Telemetry telemetry) {
        try {
            this.f401d.send(new RobocolDatagram(telemetry.toByteArray()));
        } catch (RobotCoreException e) {
            RobotLog.m331w("Failed to send telemetry data");
            RobotLog.logStacktrace(e);
        }
        telemetry.clearData();
    }

    public void sendCommand(Command command) {
        this.f411n.add(command);
    }

    private void m264a() throws RobotCoreException {
        try {
            m268a(RobotState.INIT);
            this.f404g.init(this);
            for (SyncdDevice startBlockingWork : this.f408k) {
                startBlockingWork.startBlockingWork();
            }
            this.f403f = new ElapsedTime(0);
            m268a(RobotState.RUNNING);
            this.f399b = new Thread(new C0102b(), "Event Loop");
            this.f399b.start();
        } catch (Exception e) {
            RobotLog.m331w("Caught exception during looper init: " + e.toString());
            RobotLog.logStacktrace(e);
            m268a(RobotState.EMERGENCY_STOP);
            if (RobotLog.hasGlobalErrorMsg()) {
                buildAndSendTelemetry(SYSTEM_TELEMETRY, RobotLog.getGlobalErrorMsg());
            }
            throw new RobotCoreException("Robot failed to start: " + e.getMessage());
        }
    }

    private void m270b() {
        this.f399b.interrupt();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        m268a(RobotState.STOPPED);
        this.f404g = f398a;
        this.f408k.clear();
    }

    private void m268a(RobotState robotState) {
        this.state = robotState;
        RobotLog.m330v("EventLoopManager state is " + robotState.toString());
        if (this.f407j != null) {
            this.f407j.onStateChange(robotState);
        }
    }

    private void m267a(RobocolDatagram robocolDatagram) throws RobotCoreException {
        Gamepad gamepad = new Gamepad();
        gamepad.fromByteArray(robocolDatagram.getData());
        if (gamepad.user < (byte) 1 || gamepad.user > 2) {
            RobotLog.m327d("Gamepad with user %d received. Only users 1 and 2 are valid");
            return;
        }
        int i = gamepad.user - 1;
        this.f405h[i].copy(gamepad);
        if (this.f405h[0].id == this.f405h[1].id) {
            RobotLog.m330v("Gamepad moved position, removing stale gamepad");
            if (i == 0) {
                this.f405h[1].copy(new Gamepad());
            }
            if (i == 1) {
                this.f405h[0].copy(new Gamepad());
            }
        }
    }

    private void m272b(RobocolDatagram robocolDatagram) throws RobotCoreException {
        Heartbeat heartbeat = new Heartbeat(Token.EMPTY);
        heartbeat.fromByteArray(robocolDatagram.getData());
        heartbeat.setRobotState(this.state);
        robocolDatagram.setData(heartbeat.toByteArray());
        this.f401d.send(robocolDatagram);
        this.f403f.reset();
        this.f406i = heartbeat;
    }

    private void m275c(RobocolDatagram robocolDatagram) throws RobotCoreException {
        if (!robocolDatagram.getAddress().equals(this.f412o)) {
            if (this.state == RobotState.DROPPED_CONNECTION) {
                m268a(RobotState.RUNNING);
            }
            if (this.f404g != f398a) {
                this.f412o = robocolDatagram.getAddress();
                RobotLog.m329i("new remote peer discovered: " + this.f412o.getHostAddress());
                try {
                    this.f401d.connect(this.f412o);
                } catch (SocketException e) {
                    RobotLog.m328e("Unable to connect to peer:" + e.toString());
                }
                RobocolParsable peerDiscovery = new PeerDiscovery(PeerType.PEER);
                RobotLog.m330v("Sending peer discovery packet");
                RobocolDatagram robocolDatagram2 = new RobocolDatagram(peerDiscovery);
                if (this.f401d.getInetAddress() == null) {
                    robocolDatagram2.setAddress(this.f412o);
                }
                this.f401d.send(robocolDatagram2);
            }
        }
    }

    private void m279d(RobocolDatagram robocolDatagram) throws RobotCoreException {
        RobocolParsable command = new Command(robocolDatagram.getData());
        if (command.isAcknowledged()) {
            this.f411n.remove(command);
            return;
        }
        command.acknowledge();
        this.f401d.send(new RobocolDatagram(command));
        Command[] commandArr = this.f409l;
        int length = commandArr.length;
        int i = 0;
        while (i < length) {
            Command command2 = commandArr[i];
            if (command2 == null || !command2.equals(command)) {
                i++;
            } else {
                return;
            }
        }
        Command[] commandArr2 = this.f409l;
        int i2 = this.f410m;
        this.f410m = i2 + 1;
        commandArr2[i2 % this.f409l.length] = command;
        try {
            this.f404g.processCommand(command);
        } catch (Exception e) {
            RobotLog.m328e("Event loop threw an exception while processing a command");
            RobotLog.logStacktrace(e);
        }
    }

    private void m273c() {
    }

    private void m282e(RobocolDatagram robocolDatagram) {
        RobotLog.m331w("RobotCore event loop received unknown event type: " + robocolDatagram.getMsgType().name());
    }

    public void buildAndSendTelemetry(String tag, String msg) {
        Telemetry telemetry = new Telemetry();
        telemetry.setTag(tag);
        telemetry.addData(tag, msg);
        sendTelemetryData(telemetry);
    }
}
