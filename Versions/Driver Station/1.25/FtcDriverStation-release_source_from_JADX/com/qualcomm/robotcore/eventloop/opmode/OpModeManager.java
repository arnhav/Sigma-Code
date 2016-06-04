package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorController.DeviceMode;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OpModeManager {
    public static final OpMode DEFAULT_OP_MODE;
    public static final String DEFAULT_OP_MODE_NAME = "Stop Robot";
    private Map<String, Class<?>> f420a;
    private Map<String, OpMode> f421b;
    private String f422c;
    private OpMode f423d;
    private String f424e;
    private HardwareMap f425f;
    private HardwareMap f426g;
    private C0106b f427h;
    private boolean f428i;
    private boolean f429j;
    private boolean f430k;

    /* renamed from: com.qualcomm.robotcore.eventloop.opmode.OpModeManager.b */
    private enum C0106b {
        INIT,
        LOOPING
    }

    /* renamed from: com.qualcomm.robotcore.eventloop.opmode.OpModeManager.a */
    private static class C0146a extends OpMode {
        public void init() {
            m448a();
        }

        public void init_loop() {
            m448a();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void loop() {
            m448a();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void stop() {
        }

        private void m448a() {
            Iterator it = this.hardwareMap.servoController.iterator();
            while (it.hasNext()) {
                ((ServoController) it.next()).pwmDisable();
            }
            it = this.hardwareMap.dcMotorController.iterator();
            while (it.hasNext()) {
                ((DcMotorController) it.next()).setMotorControllerDeviceMode(DeviceMode.WRITE_ONLY);
            }
            it = this.hardwareMap.dcMotor.iterator();
            while (it.hasNext()) {
                DcMotor dcMotor = (DcMotor) it.next();
                dcMotor.setPower(0.0d);
                dcMotor.setMode(RunMode.RUN_WITHOUT_ENCODERS);
            }
            it = this.hardwareMap.lightSensor.iterator();
            while (it.hasNext()) {
                ((LightSensor) it.next()).enableLed(false);
            }
        }
    }

    static {
        DEFAULT_OP_MODE = new C0146a();
    }

    public OpModeManager(HardwareMap hardwareMap) {
        this.f420a = new LinkedHashMap();
        this.f421b = new LinkedHashMap();
        this.f422c = DEFAULT_OP_MODE_NAME;
        this.f423d = DEFAULT_OP_MODE;
        this.f424e = DEFAULT_OP_MODE_NAME;
        this.f425f = new HardwareMap();
        this.f426g = new HardwareMap();
        this.f427h = C0106b.INIT;
        this.f428i = false;
        this.f429j = false;
        this.f430k = false;
        this.f425f = hardwareMap;
        register(DEFAULT_OP_MODE_NAME, C0146a.class);
        initActiveOpMode(DEFAULT_OP_MODE_NAME);
    }

    public void registerOpModes(OpModeRegister register) {
        register.register(this);
    }

    public void setHardwareMap(HardwareMap hardwareMap) {
        this.f425f = hardwareMap;
    }

    public HardwareMap getHardwareMap() {
        return this.f425f;
    }

    public Set<String> getOpModes() {
        Set<String> linkedHashSet = new LinkedHashSet();
        linkedHashSet.addAll(this.f420a.keySet());
        linkedHashSet.addAll(this.f421b.keySet());
        return linkedHashSet;
    }

    public String getActiveOpModeName() {
        return this.f422c;
    }

    public OpMode getActiveOpMode() {
        return this.f423d;
    }

    public void initActiveOpMode(String name) {
        this.f424e = name;
        this.f428i = true;
        this.f429j = true;
        this.f427h = C0106b.INIT;
    }

    public void startActiveOpMode() {
        this.f427h = C0106b.LOOPING;
        this.f430k = true;
    }

    public void stopActiveOpMode() {
        this.f423d.stop();
        initActiveOpMode(DEFAULT_OP_MODE_NAME);
    }

    public void runActiveOpMode(Gamepad[] gamepads) {
        this.f423d.time = this.f423d.getRuntime();
        this.f423d.gamepad1 = gamepads[0];
        this.f423d.gamepad2 = gamepads[1];
        if (this.f428i) {
            this.f423d.stop();
            m288a();
            this.f427h = C0106b.INIT;
            this.f429j = true;
        }
        if (this.f427h == C0106b.INIT) {
            if (this.f429j) {
                this.f423d.hardwareMap = this.f425f;
                this.f423d.resetStartTime();
                this.f423d.init();
                this.f429j = false;
            }
            this.f423d.init_loop();
            return;
        }
        if (this.f430k) {
            this.f423d.start();
            this.f430k = false;
        }
        this.f423d.loop();
    }

    public void logOpModes() {
        RobotLog.m329i("There are " + (this.f420a.size() + this.f421b.size()) + " Op Modes");
        for (Entry key : this.f420a.entrySet()) {
            RobotLog.m329i("   Op Mode: " + ((String) key.getKey()));
        }
        for (Entry key2 : this.f421b.entrySet()) {
            RobotLog.m329i("   Op Mode: " + ((String) key2.getKey()));
        }
    }

    public void register(String name, Class opMode) {
        if (m290a(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        }
        this.f420a.put(name, opMode);
    }

    public void register(String name, OpMode opMode) {
        if (m290a(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        }
        this.f421b.put(name, opMode);
    }

    private void m288a() {
        RobotLog.m329i("Attempting to switch to op mode " + this.f424e);
        try {
            if (this.f421b.containsKey(this.f424e)) {
                this.f423d = (OpMode) this.f421b.get(this.f424e);
            } else {
                this.f423d = (OpMode) ((Class) this.f420a.get(this.f424e)).newInstance();
            }
            this.f422c = this.f424e;
        } catch (Exception e) {
            m289a(e);
        } catch (Exception e2) {
            m289a(e2);
        }
        this.f428i = false;
    }

    private boolean m290a(String str) {
        return getOpModes().contains(str);
    }

    private void m289a(Exception exception) {
        RobotLog.m328e("Unable to start op mode " + this.f422c);
        RobotLog.logStacktrace(exception);
        this.f422c = DEFAULT_OP_MODE_NAME;
        this.f423d = DEFAULT_OP_MODE;
    }
}
