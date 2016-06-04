package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by IyengarArnhav on 11/4/2015.
 */
public class Sigma_Auto extends Sigma_Tele_Op {

    public Sigma_Auto() {
    }

    @Override
    public void loop() {
    }

    public void start() {

        Log.d("debug-auto", "in autonomous now");
        backLeft.setPower(1);
        backRight.setPower(1);
        frontRight.setPower(1);
        frontLeft.setPower(1);
        if (frontRight.getCurrentPosition()== 2880 || frontLeft.getCurrentPosition()== -2880 || backLeft.getCurrentPosition()== 2880 || backRight.getCurrentPosition()== 2880){
            Log.d("debug-auto", "two revs done");
            backLeft.setPower(0);
            backRight.setPower(0);
            frontRight.setPower(0);
            frontLeft.setPower(0);
            try {
                Log.d("debug-auto", "in wait loop");
                wait(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backLeft.setPower(1);
            frontLeft.setPower(1);
        }
    }

    private boolean have_encoders_reached(int i, int i1) {

        return false;
    }

    public void stop(){
    }

}
