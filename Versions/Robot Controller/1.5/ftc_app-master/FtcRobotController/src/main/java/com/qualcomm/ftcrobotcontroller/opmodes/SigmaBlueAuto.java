package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by IyengarArnhav on 12/2/2015.
 */
public class SigmaBlueAuto extends LinearOpMode {

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;

    public void myinit(){
        frontRight = hardwareMap.dcMotor.get("motor_3");
        frontLeft = hardwareMap.dcMotor.get("motor_1");
        backRight = hardwareMap.dcMotor.get("motor_4");
        backLeft = hardwareMap.dcMotor.get("motor_2");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        reset_encoders();
        telemetry.addData("Time", this.time);
        telemetry.addData("Front Left", -frontLeft.getCurrentPosition());
        telemetry.addData("Back Left", -backLeft.getCurrentPosition());
        telemetry.addData("Front Right", frontRight.getCurrentPosition());
        telemetry.addData("Back Right", backRight.getCurrentPosition());
        telemetry.addData("Mode", backLeft.getMode());
    }

    @Override
    public void runOpMode() throws InterruptedException {
        myinit();
        revert_encoders();
        waitForStart();
        drive_forward(0.25);
        reset_encoders();
        sleep(1);
        revert_encoders();
        turn_right(0.8);
        reset_encoders();
        sleep(1);
        revert_encoders();
        drive_forward(6);
        reset_encoders();
    }

    public void reset_encoders(){
        if (!have_encoders_reset()) {
            backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }

    public void revert_encoders(){
        if (have_encoders_reset()) {
            frontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            frontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            backLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            backRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }

    public void drive_forward(double f){
        {
            backLeft.setPower(-1);
            frontLeft.setPower(-1);
            backRight.setPower(1);
            frontRight.setPower(1);
        }
        while (!have_encoders_reached(f)) {
            stop_dem();
            display();
            try {
                waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
    }

    public void turn_left(double f){
        {
            backLeft.setPower(-1);
            frontLeft.setPower(-1);
            backRight.setPower(-1);
            frontRight.setPower(-1);
        }
        while (!have_encoders_reached(f)) {
            stop_dem();
            display();
            try {
                waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
    }

    public void turn_right(double f){
        {
            backLeft.setPower(1);
            frontLeft.setPower(1);
            backRight.setPower(1);
            frontRight.setPower(1);
        }
        while (!have_encoders_reached(f)) {
            stop_dem();
            display();
            try {
                waitOneFullHardwareCycle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
    }

    public void stop_dem(){
        backLeft.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean have_encoders_reached(double encoderCount) {

        encoderCount = encoderCount * 1120;

        if (frontRight.getCurrentPosition() >= encoderCount ||
                frontLeft.getCurrentPosition() <= -encoderCount ||
                backLeft.getCurrentPosition() <= -encoderCount ||
                backRight.getCurrentPosition() >= encoderCount)
            return true;
        return false;
    }
    public boolean have_encoders_reset() {

        if (Math.abs(frontRight.getCurrentPosition()) <= 10 ||
                Math.abs(frontLeft.getCurrentPosition()) <= 10 ||
                Math.abs(backLeft.getCurrentPosition()) <= 10 ||
                Math.abs(backRight.getCurrentPosition()) <= 10)
            return true;
        return false;
    }
    public void display(){
        telemetry.addData("Time", this.time);
        telemetry.addData("Front Left", -frontLeft.getCurrentPosition());
        telemetry.addData("Back Left", -backLeft.getCurrentPosition());
        telemetry.addData("Front Right", frontRight.getCurrentPosition());
        telemetry.addData("Back Right", backRight.getCurrentPosition());
        telemetry.addData("Mode", backLeft.getMode());
    }
}
