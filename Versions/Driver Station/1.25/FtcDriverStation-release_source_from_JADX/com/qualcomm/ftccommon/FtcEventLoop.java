package com.qualcomm.ftccommon;

import android.content.Context;
import com.qualcomm.ftccommon.UpdateUI.Callback;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.Util;

public class FtcEventLoop implements EventLoop {
    FtcEventLoopHandler f650a;
    OpModeManager f651b;
    OpModeRegister f652c;

    public FtcEventLoop(HardwareFactory hardwareFactory, OpModeRegister register, Callback callback, Context robotControllerContext) {
        this.f651b = new OpModeManager(new HardwareMap());
        this.f650a = new FtcEventLoopHandler(hardwareFactory, callback, robotControllerContext);
        this.f652c = register;
    }

    public OpModeManager getOpModeManager() {
        return this.f651b;
    }

    public void init(EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        DbgLog.msg("======= INIT START =======");
        this.f651b.registerOpModes(this.f652c);
        this.f650a.init(eventLoopManager);
        HardwareMap hardwareMap = this.f650a.getHardwareMap();
        ModernRoboticsUsbUtil.init(hardwareMap.appContext, hardwareMap);
        this.f651b.setHardwareMap(hardwareMap);
        hardwareMap.logDevices();
        DbgLog.msg("======= INIT FINISH =======");
    }

    public void loop() throws RobotCoreException {
        this.f650a.displayGamePadInfo(this.f651b.getActiveOpModeName());
        this.f651b.runActiveOpMode(this.f650a.getGamepads());
        this.f650a.sendTelemetryData(this.f651b.getActiveOpMode().telemetry);
    }

    public void teardown() throws RobotCoreException {
        DbgLog.msg("======= TEARDOWN =======");
        this.f651b.stopActiveOpMode();
        this.f650a.shutdownMotorControllers();
        this.f650a.shutdownServoControllers();
        this.f650a.shutdownLegacyModules();
        this.f650a.shutdownCoreInterfaceDeviceModules();
        DbgLog.msg("======= TEARDOWN COMPLETE =======");
    }

    public void processCommand(Command command) {
        DbgLog.msg("Processing Command: " + command.getName() + " " + command.getExtra());
        this.f650a.sendBatteryInfo();
        String name = command.getName();
        String extra = command.getExtra();
        if (name.equals(CommandList.CMD_RESTART_ROBOT)) {
            m431a();
        } else if (name.equals(CommandList.CMD_REQUEST_OP_MODE_LIST)) {
            m433b();
        } else if (name.equals(CommandList.CMD_INIT_OP_MODE)) {
            m432a(extra);
        } else if (name.equals(CommandList.CMD_RUN_OP_MODE)) {
            m434c();
        } else {
            DbgLog.msg("Unknown command: " + name);
        }
    }

    private void m431a() {
        this.f650a.restartRobot();
    }

    private void m433b() {
        String str = BuildConfig.VERSION_NAME;
        for (String str2 : this.f651b.getOpModes()) {
            if (!str2.equals(OpModeManager.DEFAULT_OP_MODE_NAME)) {
                if (!str.isEmpty()) {
                    str = str + Util.ASCII_RECORD_SEPARATOR;
                }
                str = str + str2;
            }
        }
        this.f650a.sendCommand(new Command(CommandList.CMD_REQUEST_OP_MODE_LIST_RESP, str));
    }

    private void m432a(String str) {
        String opMode = this.f650a.getOpMode(str);
        this.f650a.resetGamepads();
        this.f651b.initActiveOpMode(opMode);
        this.f650a.sendCommand(new Command(CommandList.CMD_INIT_OP_MODE_RESP, opMode));
    }

    private void m434c() {
        this.f651b.startActiveOpMode();
        this.f650a.sendCommand(new Command(CommandList.CMD_RUN_OP_MODE_RESP, this.f651b.getActiveOpModeName()));
    }
}
