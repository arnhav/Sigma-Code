package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by IyengarArnhav on 2/15/2016.
 */
public class Linear_Tele_Op extends LinearOpMode{

    final static double FLIPPERL_MIN_RANGE  = 0.15;
    final static double FLIPPERL_MAX_RANGE  = 0.6;
    final static double FLIPPERR_MIN_RANGE  = 0.16;
    final static double FLIPPERR_MAX_RANGE  = 1.0;

    double flipperLPosition;
    double flipperRPosition;

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor arm;
    DcMotor arm2;
    Servo flipperL;
    Servo flipperR;
    Servo dropper;
    Servo dropper1;
    GyroSensor gyro;

    int increment;
    boolean isPressed;
    boolean isPresseda;
    boolean startIsPressed;
    int count;
    int counta;

    @Override
    public void runOpMode() throws InterruptedException {
        myinit();
        waitForStart();
        while (opModeIsActive()){
            waitOneFullHardwareCycle();
            /**
             * Puts the arm back into the run using encoders mode
             */
            arm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

            /**
             * Instantiation of the Joystick variable
             */

            float leftThrottle;
            float rightThrottle;
            if (startIsPressed){
                leftThrottle = gamepad1.right_stick_y;
                rightThrottle = -gamepad1.left_stick_y;
            }
            else{
                leftThrottle = -gamepad1.left_stick_y;
                rightThrottle = gamepad1.right_stick_y;
            }
            float right = rightThrottle;
            float left = leftThrottle;
            float arm_ = gamepad2.right_stick_y;
            float arm_2 = gamepad2.left_stick_y;

            /**
             * This is the incrementation code. Essentially, the max power for the motors is set to a specific value.
             */

            if (increment == 11){
                frontLeft.setPower(left);
                backLeft.setPower(left);
                frontRight.setPower(right);
                backRight.setPower(right);
            }

            if (increment == 10){
                frontLeft.setPower(left);
                backLeft.setPower(left);
                frontRight.setPower(right);
                backRight.setPower(right);
            }

            if (increment == 9){
                frontLeft.setPower(left * 0.9);
                backLeft.setPower(left * 0.9);
                frontRight.setPower(right * 0.9);
                backRight.setPower(right * 0.9);
            }

            if (increment == 8){
                frontLeft.setPower(left * 0.8);
                backLeft.setPower(left * 0.8);
                frontRight.setPower(right * 0.8);
                backRight.setPower(right * 0.8);
            }

            if (increment == 7){
                frontLeft.setPower(left * 0.7);
                backLeft.setPower(left * 0.7);
                frontRight.setPower(right * 0.7);
                backRight.setPower(right * 0.7);
            }

            if (increment == 6){
                frontLeft.setPower(left * 0.6);
                backLeft.setPower(left * 0.6);
                frontRight.setPower(right * 0.6);
                backRight.setPower(right * 0.6);
            }

            if (increment == 5){
                frontLeft.setPower(left * 0.5);
                backLeft.setPower(left * 0.5);
                frontRight.setPower(right * 0.5);
                backRight.setPower(right * 0.5);
            }

            if (increment == 4){
                frontLeft.setPower(left * 0.4);
                backLeft.setPower(left * 0.4);
                frontRight.setPower(right * 0.4);
                backRight.setPower(right * 0.4);
            }

            if (increment == 3){
                frontLeft.setPower(left * 0.3);
                backLeft.setPower(left * 0.3);
                frontRight.setPower(right * 0.3);
                backRight.setPower(right * 0.3);
            }

            if (increment == 2){
                frontLeft.setPower(left * 0.3);
                backLeft.setPower(left * 0.3);
                frontRight.setPower(right * 0.3);
                backRight.setPower(right * 0.3);
            }

            /**
             * The tolerance for the variable
             */

            if (increment < 3){
                increment = 3;
            }

            if (increment > 10){
                increment = 10;
            }

            /**
             * Range Clips and Position / Power setting
             */

            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);
            arm_ = Range.clip(arm_, -1, 1);
            arm_2 = Range.clip(arm_2, -1, 1);

            flipperLPosition = Range.clip(flipperLPosition, FLIPPERL_MIN_RANGE, FLIPPERL_MAX_RANGE);
            flipperRPosition = Range.clip(flipperRPosition, FLIPPERR_MIN_RANGE, FLIPPERR_MAX_RANGE);

            right = (float) scaleInput(right);
            left = (float) scaleInput(left);
            arm_ = (float) scaleInput(arm_);
            arm_2 = (float) scaleInput(arm_2);

            flipperL.setPosition(flipperLPosition);
            flipperR.setPosition(flipperRPosition);
            arm.setPower(arm_);
            arm2.setPower(arm_2 * 0.25);

            /**
             * This next section is for making the arm automated / without driver skill
             */

            if (gamepad2.right_bumper){
                while (!have_encoders_reached(10)) {
                    arm.setPower(-1);
                    arm2.setPower(-0.1);
                    waitOneFullHardwareCycle();
                }
                arm.setPower(0);
                sleep(5);
                reset_encoders();
            }
            if (gamepad2.left_bumper) {
                arm.setPower(1);
            }

            /**
             * The max speed can be changed by toggling the right & left bumpers, and the right & left triggers
             */

            if(gamepad1.right_trigger == 1) {
                increment -= 1;
                sleep(2);
            }

            if (gamepad1.left_trigger == 1){
                increment += 1;
                sleep(2);
            }

            if (gamepad1.left_bumper){
                increment  = 10;
            }

            if (gamepad1.right_bumper){
                increment  = 5;
            }
            /**
             * Switches orientation of the robot
             */
            if (gamepad1.a){
                startIsPressed = true;
            }
            if (gamepad1.dpad_down){
                startIsPressed = false;
            }

            /**
             * Opens and Closes the climber dropper
             */
            if (gamepad2.dpad_up){
                dropper.setPosition(0.0);
            }
            if (gamepad2.dpad_down){
                dropper.setPosition(1.0);
            }
            if (gamepad2.a){
                dropper.setPosition(0.0);
            }
            if (gamepad2.y){
                dropper.setPosition(1.0);
            }
            /**
             * The next section is for the servo lever pushers
             */

            if (gamepad1.x){ //close
                flipperRPosition = FLIPPERR_MAX_RANGE;
                count = 2;
            }

            if (gamepad1.b){
                if (!isPressed) {
                    flipperRPosition = 0.3;
                    sleep(3);
                    count = 1;
                }
                if (isPressed) {
                    flipperRPosition = FLIPPERR_MIN_RANGE;
                    sleep(3);
                    count = 2;
                }
            }

            if (gamepad1.dpad_left){
                if (!isPresseda){
                    flipperLPosition = 0.525;
                    sleep(3);
                    counta = 1;
                }
                if (isPresseda){
                    flipperLPosition = FLIPPERL_MAX_RANGE;
                    sleep(3);
                    counta = 2;
                }
            }

            if (gamepad1.dpad_right){ //close
                flipperLPosition = FLIPPERL_MIN_RANGE;
                counta = 2;
            }

            if (count == 1){
                isPressed = true;
            }
            if (counta == 1){
                isPresseda = true;
            }
            if (count == 2){
                isPressed = false;
            }
            if (counta == 2){
                isPresseda = false;
            }


            /**
             * Telemetry for debugging
             */

            telemetry.addData("Text", "*** Robot Data***");
            telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
            telemetry.addData("Mode", frontLeft.getMode());
            telemetry.addData("Arm", arm.getCurrentPosition());
            telemetry.addData("ServoR", flipperR.getPosition());
            telemetry.addData("ServoL", flipperL.getPosition());
            telemetry.addData("Dropper", dropper.getPosition());
            telemetry.addData("Increment", increment);
            telemetry.addData("Start", startIsPressed);
            telemetry.addData("Joystick Y", gamepad2.right_stick_y);
            telemetry.addData("Joystick X", gamepad2.right_stick_x);
        }
    }

    public void myinit() {
        frontRight = hardwareMap.dcMotor.get("motor_3");
        frontLeft = hardwareMap.dcMotor.get("motor_1");
        backRight = hardwareMap.dcMotor.get("motor_4");
        backLeft = hardwareMap.dcMotor.get("motor_2");
        arm = hardwareMap.dcMotor.get("arm");
        arm2 = hardwareMap.dcMotor.get("arm2");
        flipperL = hardwareMap.servo.get("flipper_l");
        flipperR = hardwareMap.servo.get("flipper_r");
        dropper = hardwareMap.servo.get("cis1");
        dropper1 = hardwareMap.servo.get("cis2");
        gyro = hardwareMap.gyroSensor.get("gyro");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        arm2.setDirection(DcMotor.Direction.REVERSE);
        backRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        arm.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        flipperLPosition = 0.15;
        flipperRPosition = 1.0;
        flipperL.setPosition(0.15);
        flipperR.setPosition(1.0);
        dropper.setPosition(1.0);
        dropper1.setPosition(0.0);

        increment = 8;
        count = 1;
        isPressed = false;
        isPresseda = false;
        startIsPressed = true;
    }

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.1, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }
    public boolean have_encoders_reached(double encoderCount) {

        encoderCount = encoderCount * 1120;

        if (arm.getCurrentPosition() <= -encoderCount)
            return true;
        return false;
    }
    public void reset_encoders(){
        backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
