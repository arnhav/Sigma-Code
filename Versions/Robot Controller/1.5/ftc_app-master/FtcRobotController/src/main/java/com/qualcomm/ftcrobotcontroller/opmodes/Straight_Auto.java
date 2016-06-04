package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by IyengarArnhav on 2/15/2016.
 */
public class Straight_Auto extends LinearOpMode {

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor arm;
    DcMotor arm2;
    GyroSensor gyro;
    Servo flipperL;
    Servo flipperR;
    Servo acsL;
    Servo acsR;
    Servo dropper;

    public void myinit(){
        frontRight = hardwareMap.dcMotor.get("motor_3");
        frontLeft = hardwareMap.dcMotor.get("motor_1");
        backRight = hardwareMap.dcMotor.get("motor_4");
        backLeft = hardwareMap.dcMotor.get("motor_2");
        arm = hardwareMap.dcMotor.get("arm");
        arm2 = hardwareMap.dcMotor.get("arm2");
        flipperL = hardwareMap.servo.get("flipper_l");
        flipperR = hardwareMap.servo.get("flipper_r");
        acsL = hardwareMap.servo.get("acsL");
        acsR = hardwareMap.servo.get("acsR");
        dropper = hardwareMap.servo.get("cis1");
        gyro = hardwareMap.gyroSensor.get("gyro");
        acsL.setDirection(Servo.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        gyro.calibrate();
        telemetry.addData("Front Left", -frontLeft.getCurrentPosition());
        telemetry.addData("Front Right", -frontRight.getCurrentPosition());
        telemetry.addData("Mode", frontRight.getMode());
        telemetry.addData("Gyro", gyro.getHeading());

        flipperL.setPosition(0.15);
        flipperR.setPosition(1.0);
        dropper.setPosition(0.6);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        myinit();
        revert_encoders();
        waitForStart();
        while (gyro.isCalibrating()) {
            Thread.sleep(50);
        }
        sleep(40);
        drive_straight(7, 0, "Backward");
    }

    public void reset_encoders(){
        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    public void revert_encoders(){
        frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    public void drive_forward(double f) throws InterruptedException {
        while (!have_encoders_reached(f)) {
            display();
            backLeft.setPower(0.7);
            frontLeft.setPower(0.7);
            backRight.setPower(-0.7);
            frontRight.setPower(-0.7);
            waitOneFullHardwareCycle();
            //backLeft.setPower(0.7);
            //frontLeft.setPower(0.7);
            //backRight.setPower(-0.7);
            //frontRight.setPower(-0.7);
        }
        stop_dem();
    }

    public void drive_backward(double f) throws InterruptedException {
        while (!have_encoders_reached(f)) {
            display();
            backLeft.setPower(-0.7);
            frontLeft.setPower(-0.7);
            backRight.setPower(0.7);
            frontRight.setPower(0.7);
            waitOneFullHardwareCycle();
            //backLeft.setPower(-0.7);
            //frontLeft.setPower(-0.7);
            //backRight.setPower(0.7);
            //frontRight.setPower(0.7);
        }
        stop_dem();
    }

    public void drive_straight(double f, double d, String t) throws InterruptedException {
        if (t.equals("Backward")) {
            while (!have_encoders_reached(f)) {
                display();
                waitOneFullHardwareCycle();
                if (gyro.getHeading() - d <= -1) {
                    backLeft.setPower(-0.2);
                    frontLeft.setPower(-0.2);
                    backRight.setPower(0.7);
                    frontRight.setPower(0.7);
                    telemetry.addData("Correction:", "Left");
                } else if (gyro.getHeading() - d >= 1) {
                    backLeft.setPower(-0.7);
                    frontLeft.setPower(-0.7);
                    backRight.setPower(0.2);
                    frontRight.setPower(0.2);
                    telemetry.addData("Correction:", "Right");
                } else {
                    drive_backward(f);
                    telemetry.addData("Correction:", "None");
                }
            }
            stop_dem();
        }
        if (t.equals("Forward")) {
            while (!have_encoders_reached(f)) {
                if (gyro.getHeading() - d <= 1) {
                    backLeft.setPower(0.2);
                    frontLeft.setPower(0.2);
                    backRight.setPower(-0.7);
                    frontRight.setPower(-0.7);
                    telemetry.addData("Correction:", "Left");
                } else if (gyro.getHeading() - d >= -1) {
                    backLeft.setPower(0.7);
                    frontLeft.setPower(0.7);
                    backRight.setPower(-0.2);
                    frontRight.setPower(-0.2);
                    telemetry.addData("Correction:", "Right");
                } else {
                    drive_forward(f);
                    telemetry.addData("Correction:", "None");
                }
            }
            stop_dem();
        }
        stop_dem();
    }

    public void turn_left(double f) throws InterruptedException {
        while (!has_gyro_reached(f)) {
            backLeft.setPower(-0.5);
            frontLeft.setPower(-0.5);
            backRight.setPower(-0.5);
            frontRight.setPower(-0.5);
            waitOneFullHardwareCycle();
            //backLeft.setPower(-0.5);
            //frontLeft.setPower(-0.5);
            //backRight.setPower(-0.5);
            //frontRight.setPower(-0.5);
            display();
        }
        stop_dem();
    }

    public void turn_right(double f) throws InterruptedException {
        while (!has_gyro_reached(f)) {
            display();
            backLeft.setPower(0.5);
            frontLeft.setPower(0.5);
            backRight.setPower(0.5);
            frontRight.setPower(0.5);
            waitOneFullHardwareCycle();
            //backLeft.setPower(0.5);
            //frontLeft.setPower(0.5);
            //backRight.setPower(0.5);
            //frontRight.setPower(0.5);
        }
        stop_dem();
    }

    public void stop_dem(){
        backLeft.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time * 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    public boolean has_gyro_reached(double degree) {
        if (gyro.getHeading()<= degree + 1 && gyro.getHeading() >= degree - 1)
            return true;
        return false;
    }
    public void display(){
        telemetry.addData("Front Left", frontLeft.getCurrentPosition());
        telemetry.addData("Front Right", -frontRight.getCurrentPosition());
        telemetry.addData("Mode", frontRight.getMode());
        telemetry.addData("Gyro", gyro.getHeading());
    }
}
