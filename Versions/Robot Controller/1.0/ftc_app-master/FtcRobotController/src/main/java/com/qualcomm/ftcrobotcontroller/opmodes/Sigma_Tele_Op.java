package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by IyengarArnhav on 11/4/2015.
 */
public class Sigma_Tele_Op extends OpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;


    public Sigma_Tele_Op() {

    }

    @Override
    public void init() {
        frontRight = hardwareMap.dcMotor.get("motor_1");
        frontLeft = hardwareMap.dcMotor.get("motor_2");
        backRight = hardwareMap.dcMotor.get("motor_3");
        backLeft = hardwareMap.dcMotor.get("motor_4");
    }

    @Override
    public void loop() {

        float rightThrottle = -gamepad1.left_stick_y;
        float leftThrottle = gamepad1.right_stick_y;
        float right = rightThrottle;
        float left = leftThrottle;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        right = (float) scaleInput(right);
        left = (float) scaleInput(left);

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

    }
/*
    @Override
    public void stop() {

    }
*/
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
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
}