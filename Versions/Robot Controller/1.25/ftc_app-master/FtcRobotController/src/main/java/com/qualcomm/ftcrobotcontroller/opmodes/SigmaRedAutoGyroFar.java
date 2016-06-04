package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by aggie on 1/28/2016.
 */
public class SigmaRedAutoGyroFar extends LinearOpMode {

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
        reset_encoders();
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
        revert_encoders();
        gyro.calibrate();
        waitForStart();
        while (gyro.isCalibrating()) {
            Thread.sleep(1);
        }
        drive_backward(.25);
        reset_encoders();
        sleep(2);
        revert_encoders();
        waitForStart();
        drive_backward(.25);
        reset_encoders();
        sleep(1);
        revert_encoders();
        turn_right(0.8);
        reset_encoders();
        sleep(1);
        revert_encoders();
        drive_backward(.25);
        reset_encoders();
        sleep(1);
        revert_encoders();
        waitForStart();
        turn_back_right(.25);
        reset_encoders();
        sleep(1);
        revert_encoders();
        waitForStart();
        drive_backward(70000000);
        reset_encoders();
    }

    public void reset_encoders(){
        if (!have_encoders_reset()) {
            frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }

    public void revert_encoders(){
        if (have_encoders_reset()) {
            frontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            frontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }

    public void drive_forward(double f) throws InterruptedException {
        while (!have_encoders_reached(f)) {
            display();
            backLeft.setPower(1);
            frontLeft.setPower(1);
            backRight.setPower(-1);
            frontRight.setPower(-1);
            waitOneFullHardwareCycle();
        }
        stop_dem();
    }

    public void drive_backward(double f) throws InterruptedException {
        while (!have_encoders_reached(f)) {
            display();
            backLeft.setPower(-1);
            frontLeft.setPower(-1);
            backRight.setPower(1);
            frontRight.setPower(1);
            waitOneFullHardwareCycle();
        }
        stop_dem();
    }

    public void drive_straight(double f, double d, String t) throws InterruptedException {
        while (!have_encoders_reached(f)) {
            display();
            waitOneFullHardwareCycle();
            if (t.equals("Backward")){
                if (gyro.getHeading() > 270){
                    backLeft.setPower(-0.4);
                    frontLeft.setPower(-0.4);
                    backRight.setPower(0.8);
                    frontRight.setPower(0.8);
                }
                else if (gyro.getHeading() < 90){
                    backLeft.setPower(-0.8);
                    frontLeft.setPower(-0.8);
                    backRight.setPower(0.4);
                    frontRight.setPower(0.4);
                }
                else {
                    backLeft.setPower(-0.8);
                    frontLeft.setPower(-0.8);
                    backRight.setPower(0.8);
                    frontRight.setPower(0.8);
                }
            }
            if (t.equals("Forward")) {
                if (gyro.getHeading() > d){
                    backLeft.setPower(0.6);
                    frontLeft.setPower(0.6);
                    backRight.setPower(-1);
                    frontRight.setPower(-1);
                }
                else if (gyro.getHeading() > d){
                    backLeft.setPower(1);
                    frontLeft.setPower(1);
                    backRight.setPower(-0.6);
                    frontRight.setPower(-0.6);
                }
                else {
                    backLeft.setPower(1);
                    frontLeft.setPower(1);
                    backRight.setPower(-1);
                    frontRight.setPower(-1);
                }
            }
        }
        stop_dem();
    }

    public void turn_left(double f) throws InterruptedException {
        while (!has_gyro_reached(f)) {
            backLeft.setPower(-1);
            frontLeft.setPower(-1);
            backRight.setPower(-1);
            frontRight.setPower(-1);
            display();
            waitOneFullHardwareCycle();
        }
        stop_dem();
    }

    public void turn_right(double f) throws InterruptedException {
        while (!has_gyro_reached(f)) {
            backLeft.setPower(1);
            frontLeft.setPower(1);
            backRight.setPower(1);
            frontRight.setPower(1);
            display();
            waitOneFullHardwareCycle();
        }
        stop_dem();
    }

    public void turn_back_right(double f) throws InterruptedException{
        while (!has_gyro_reached(f)){
            backRight.setPower(-1);
            frontRight.setPower(-1);
            display();
            waitOneFullHardwareCycle();
        }
        stop_dem();
    }

    public void turn_back_left(double f) throws InterruptedException{
        while (!has_gyro_reached(f)){
            frontLeft.setPower(1);
            backLeft.setPower(1);
            display();
            waitForNextHardwareCycle();
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

        if (frontRight.getCurrentPosition() <= -encoderCount ||
                frontLeft.getCurrentPosition() >= encoderCount)
            return true;
        return false;
    }
    public boolean have_encoders_reset() {

        if (Math.abs(frontRight.getCurrentPosition()) <= 10 ||
                Math.abs(frontLeft.getCurrentPosition()) <= 10) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean has_gyro_reached(double degree) {
        if (gyro.getHeading()== degree)
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
