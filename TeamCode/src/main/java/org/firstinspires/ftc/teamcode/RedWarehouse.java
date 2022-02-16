package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="RedWarehouse", group = "none")

public class RedWarehouse extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        Log.i("[phoenix:init]", "Initialize");
        initialize();
        Log.i("[phoenix:afterInit]", "After Initialize");
        waitForStart();
        Log.i("[phoenix:started]", "Started");
        OnStart();

        int duckPos = 2;

        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + 1000) {
            duckPos = imageNavigation.getDuckies();
            telemetry.addData("ducky: %d", duckPos);
            telemetry.update();
            sleep(10);
        }

        Drive(0.5f, 12, Direction.BACKWARD);
        Turn(0.5f, 30, Direction.COUNTERCLOCKWISE, imu);

        Strafe(0.8f, 6, Direction.RIGHT);
        Turn(0.5f, 150, Direction.CLOCKWISE, imu);
        sleep(200);

        DriveToPointHeading(0.35f, 18, -60);
        StopAll();
        MovePulley(0.5f, 1);//stage tbd with ducky
        //drop freight

        Turn(0.5f, 90, Direction.COUNTERCLOCKWISE, imu);
        sleep(1000);
        Log.i("[phoenix:getAdAngle]", String.format("getAdjustedAngle: %f", imu.getAdjustedAngle()));
        StrafeUntilHeading(0.6f, 0.5f, startHeading + 90, 20, Direction.RIGHT);
        Log.i("[phoenix:passed]", String.format("getAdjustedAngle: %f", imu.getAdjustedAngle()));
        Strafe(0.5f, 5, Direction.LEFT);
        DriveHeading(0.5f, 40, 90, 0.3f, Direction.BACKWARD);

        //copyToTele();
    }


}
