package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by IyengarArnhav on 2/27/2016.
 */
public class Acceleration extends LinearOpMode {

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor arm;
    DcMotor arm2;
    GyroSensor gyro;
    Servo flipperL;
    Servo flipperR;

    public void myinit(){
        frontRight = hardwareMap.dcMotor.get("motor_3");
        frontLeft = hardwareMap.dcMotor.get("motor_1");
        backRight = hardwareMap.dcMotor.get("motor_4");
        backLeft = hardwareMap.dcMotor.get("motor_2");
        arm = hardwareMap.dcMotor.get("arm");
        arm2 = hardwareMap.dcMotor.get("arm2");
        flipperL = hardwareMap.servo.get("flipper_l");
        flipperR = hardwareMap.servo.get("flipper_r");
        gyro = hardwareMap.gyroSensor.get("gyro");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        gyro.calibrate();
        telemetry.addData("Front Left", -frontLeft.getCurrentPosition());
        telemetry.addData("Front Right", -frontRight.getCurrentPosition());
        telemetry.addData("Mode", frontRight.getMode());
        telemetry.addData("Gyro", gyro.getHeading());

        flipperL.setPosition(0.15);
        flipperR.setPosition(1.0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        myinit();
        waitForStart();
    }
    public boolean have_encoders_reached(double encoderCount) {

        encoderCount = encoderCount * 1120;

        if (Math.abs(frontRight.getCurrentPosition()) >= encoderCount &&
                Math.abs(frontLeft.getCurrentPosition()) >= encoderCount)
            return true;
        return false;
    }
    public boolean have_encoders_reset() {

        if (Math.abs(frontRight.getCurrentPosition()) <= 10 &&
                Math.abs(frontLeft.getCurrentPosition()) <= 10) {
            return true;
        }
        else {
            return false;
        }
    }

}
