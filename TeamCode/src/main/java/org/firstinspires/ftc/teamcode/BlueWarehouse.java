package org.firstinspires.ftc.teamcode;

import android.util.Log;

public class BlueWarehouse extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        Log.i("[phoenix:InitgetAdjustedAngle]", "Initialize");
        initialize();
        Log.i("[phoenix:InitgetAdjustedAngle]", "After Initialize");
        waitForStart();
        Log.i("[phoenix:InitgetAdjustedAngle]", "Started");

        Log.i("[phoenix:InitgetAdjustedAngle]", String.format("getAdjustedAngle: %f", imu.getAdjustedAngle()));


        int duckPos = 2;

        waitForStart();

        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + 1000) {
            duckPos = imageNavigation.getDuckies();
            telemetry.addData("ducky: %d", duckPos);
            telemetry.update();
            sleep(10);
        }

        Drive(0.5f, 12, Direction.BACKWARD);
        Turn(0.5f, 135, Direction.COUNTERCLOCKWISE, imu);


        //drop freight

        Strafe(0.8f, 6, Direction.RIGHT);
        Turn(0.5f, 150, Direction.CLOCKWISE, imu);
        sleep(200);

        DriveToPointHeading(0.35f, 18, -60);//coordinates r prob wrong
        StopAll();
        //drop freight

        Turn(0.5f, 90, Direction.COUNTERCLOCKWISE, imu);
        sleep(1000);
        Log.i("[phoenix:getAdjustedAngle]", String.format("getAdjustedAngle: %f", imu.getAdjustedAngle()));
        StrafeUntilHeading(0.6f, 0.5f, startHeading + 90, 20, Direction.RIGHT);
        Log.i("[phoenix:passed]", String.format("getAdjustedAngle: %f", imu.getAdjustedAngle()));
        Strafe(0.5f, 5, Direction.LEFT);
        DriveHeading(0.5f, 40, 90, 0.3f, Direction.BACKWARD);
    }

}
