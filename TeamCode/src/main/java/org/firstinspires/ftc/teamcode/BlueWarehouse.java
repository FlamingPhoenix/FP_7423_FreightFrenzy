package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="BlueWarehouse", group = "none")

public class BlueWarehouse extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        Log.i("[phoenix:init]", "Initialize");
        initialize();
        int duckPos = 2;
        Log.i("[phoenix:afterInit]", "After Initialize");
        while (!isStarted()) {
            /*do stuff while waiting for start
            waitForStart method remains to prevent errors from crashing the phone and control hub
            */
            duckPos = imageNavigation.getDuckies();
            telemetry.addData("ducky: %d", duckPos);
            telemetry.update();
            sleep(10);
        }//test first before generalizing loop to other subclasses
        waitForStart();
        Log.i("[phoenix:started]", "Started");

/*
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + 1000) {
            duckPos = imageNavigation.getDuckies();
            telemetry.addData("ducky: %d", duckPos);
            telemetry.update();
            sleep(10);
        }
*/
        OnStart();

        Turn(0.5f, 125, Direction.COUNTERCLOCKWISE, imu);

//        Drive(0.3f, 5, Direction.FORWARD);

        MovePulley(0.9f, duckPos);
        sleep(750);
        finger.setPosition(0.1);
        sleep(750);
        finger.setPosition(0.3);
        MovePulley(0.7f, 0);
        sleep(100);
        vbarRight.setPosition(0.79);

        Turn(0.5f, 30, Direction.CLOCKWISE, imu);
        Strafe(0.6f, 11, Direction.RIGHT);
        Drive(0.8f, 40, Direction.BACKWARD);

//        Strafe(0.4f, 2.5f, Direction.LEFT);

        Intake(0.8f);
        Strafe(0.5f, 5, Direction.LEFT);
        Turn(0.5f, 45, Direction.COUNTERCLOCKWISE, imu);

        MovePulley(0.9f, duckPos);
        sleep(750);
        finger.setPosition(0.1);
        sleep(750);
        finger.setPosition(0.3);
        MovePulley(0.7f, 0);
        sleep(100);
        vbarRight.setPosition(0.79);

        Turn(0.5f, 30, Direction.CLOCKWISE, imu);
        Strafe(0.6f, 10, Direction.RIGHT);
        Drive(0.8f, 40, Direction.BACKWARD);
        Strafe(0.6f, 24, Direction.RIGHT);
    }


}
