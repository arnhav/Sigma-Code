package com.qualcomm.ftccommon;

import android.content.Context;
import com.qualcomm.ftccommon.UpdateUI.Callback;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.BatteryChecker.BatteryWatcher;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map.Entry;

public class FtcEventLoopHandler implements BatteryWatcher {
    public static final String NO_VOLTAGE_SENSOR = "no voltage sensor found";
    EventLoopManager f653a;
    BatteryChecker f654b;
    Context f655c;
    ElapsedTime f656d;
    double f657e;
    Callback f658f;
    HardwareFactory f659g;
    HardwareMap f660h;

    public FtcEventLoopHandler(HardwareFactory hardwareFactory, Callback callback, Context robotControllerContext) {
        this.f656d = new ElapsedTime();
        this.f657e = 0.25d;
        this.f660h = new HardwareMap();
        this.f659g = hardwareFactory;
        this.f658f = callback;
        this.f655c = robotControllerContext;
        this.f654b = new BatteryChecker(robotControllerContext, this, 180000);
        this.f654b.startBatteryMonitoring();
    }

    public void init(EventLoopManager eventLoopManager) {
        this.f653a = eventLoopManager;
    }

    public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
        this.f660h = this.f659g.createHardwareMap(this.f653a);
        return this.f660h;
    }

    public void displayGamePadInfo(String activeOpModeName) {
        this.f658f.updateUi(activeOpModeName, this.f653a.getGamepads());
    }

    public Gamepad[] getGamepads() {
        return this.f653a.getGamepads();
    }

    public void resetGamepads() {
        this.f653a.resetGamepads();
    }

    public void sendTelemetryData(Telemetry telemetry) {
        if (this.f656d.time() > this.f657e) {
            this.f656d.reset();
            if (telemetry.hasData()) {
                this.f653a.sendTelemetryData(telemetry);
            }
            telemetry.clearData();
        }
    }

    public void sendBatteryInfo() {
        m435a();
        m436b();
    }

    private void m435a() {
        sendTelemetry(EventLoopManager.RC_BATTERY_LEVEL_KEY, String.valueOf(this.f654b.getBatteryLevel()));
    }

    private void m436b() {
        String str;
        Iterator it = this.f660h.voltageSensor.iterator();
        double d = Double.MAX_VALUE;
        while (it.hasNext()) {
            double voltage;
            VoltageSensor voltageSensor = (VoltageSensor) it.next();
            if (voltageSensor.getVoltage() < d) {
                voltage = voltageSensor.getVoltage();
            } else {
                voltage = d;
            }
            d = voltage;
        }
        if (this.f660h.voltageSensor.size() == 0) {
            str = NO_VOLTAGE_SENSOR;
        } else {
            str = String.valueOf(new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        sendTelemetry(EventLoopManager.ROBOT_BATTERY_LEVEL_KEY, str);
    }

    public void sendTelemetry(String tag, String msg) {
        Telemetry telemetry = new Telemetry();
        telemetry.setTag(tag);
        telemetry.addData(tag, msg);
        if (this.f653a != null) {
            this.f653a.sendTelemetryData(telemetry);
            telemetry.clearData();
        }
    }

    public void shutdownMotorControllers() {
        for (Entry entry : this.f660h.dcMotorController.entrySet()) {
            String str = (String) entry.getKey();
            DcMotorController dcMotorController = (DcMotorController) entry.getValue();
            DbgLog.msg("Stopping DC Motor Controller " + str);
            dcMotorController.close();
        }
    }

    public void shutdownServoControllers() {
        for (Entry entry : this.f660h.servoController.entrySet()) {
            String str = (String) entry.getKey();
            ServoController servoController = (ServoController) entry.getValue();
            DbgLog.msg("Stopping Servo Controller " + str);
            servoController.close();
        }
    }

    public void shutdownLegacyModules() {
        for (Entry entry : this.f660h.legacyModule.entrySet()) {
            String str = (String) entry.getKey();
            LegacyModule legacyModule = (LegacyModule) entry.getValue();
            DbgLog.msg("Stopping Legacy Module " + str);
            legacyModule.close();
        }
    }

    public void shutdownCoreInterfaceDeviceModules() {
        for (Entry entry : this.f660h.deviceInterfaceModule.entrySet()) {
            String str = (String) entry.getKey();
            DeviceInterfaceModule deviceInterfaceModule = (DeviceInterfaceModule) entry.getValue();
            DbgLog.msg("Stopping Core Interface Device Module " + str);
            deviceInterfaceModule.close();
        }
    }

    public void restartRobot() {
        this.f654b.endBatteryMonitoring();
        this.f658f.restartRobot();
    }

    public void sendCommand(Command command) {
        this.f653a.sendCommand(command);
    }

    public String getOpMode(String extra) {
        if (this.f653a.state != RobotState.RUNNING) {
            return OpModeManager.DEFAULT_OP_MODE_NAME;
        }
        return extra;
    }

    public void updateBatteryLevel(float percent) {
        sendTelemetry(EventLoopManager.RC_BATTERY_LEVEL_KEY, String.valueOf(percent));
    }
}
