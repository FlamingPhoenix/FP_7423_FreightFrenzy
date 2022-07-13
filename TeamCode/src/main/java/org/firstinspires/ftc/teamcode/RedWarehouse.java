package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name="RedWarehouse", group = "none")

public class RedWarehouse extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        Log.i("[phoenix:init]", "Initialize");
        initialize();
        int duckPos;
        Log.i("[phoenix:afterInit]", "After Initialize");
        duckPos = useCamera();
        Log.i("[phoenix:started]", "Started");

        OnStart(duckPos);

        Turn(0.5f, 125, Direction.CLOCKWISE, imu);

//        Drive(0.3f, 5, Direction.FORWARD);

        MovePulley(0.9f, duckPos);
        sleep(750);
        finger.setPosition(0.1);
        sleep(750);
        finger.setPosition(0.3);
        MovePulley(0.7f, 0);
        sleep(100);
        vbarRight.setPosition(0.79);

        Turn(0.5f, 30, Direction.COUNTERCLOCKWISE, imu);
        Strafe(0.6f, 11, Direction.LEFT);
        Drive(0.8f, 40, Direction.BACKWARD);

        //Strafe(0.4f, 2.5f, Direction.RIGHT);

        Intake(0.8f);
        Strafe(0.5f, 5, Direction.RIGHT);
        Turn(0.5f, 45, Direction.CLOCKWISE, imu);

        finger.setPosition(0.6f);
        sleep(500);

        MovePulley(0.9f, duckPos);
        sleep(750);
        finger.setPosition(0.1);
        sleep(750);
        finger.setPosition(0.3);
        MovePulley(0.7f, 0);
        sleep(100);
        vbarRight.setPosition(0.79);

        Turn(0.5f, 30, Direction.COUNTERCLOCKWISE, imu);
        Strafe(0.6f, 10, Direction.LEFT);
        Drive(0.8f, 40, Direction.BACKWARD);

//        Drive(0.5f, 12, Direction.BACKWARD);
//        Turn(0.5f, 30, Direction.COUNTERCLOCKWISE, imu);
//
//        Strafe(0.8f, 6, Direction.RIGHT);
//        Turn(0.5f, 150, Direction.CLOCKWISE, imu);
//        sleep(200);
//
//        DriveToPointHeading(0.35f, 18, -60);
//        StopAll();
//        MovePulley(0.5f, 1);//stage tbd with ducky
//        //drop freight
//
//        Turn(0.5f, 90, Direction.COUNTERCLOCKWISE, imu);
//        sleep(1000);
//        Log.i("[phoenix:getAdAngle]", String.format("getAdjustedAngle: %f", imu.getAdjustedAngle()));
//        StrafeUntilHeading(0.6f, 0.5f, startHeading + 90, 20, Direction.RIGHT);
//        Log.i("[phoenix:passed]", String.format("getAdjustedAngle: %f", imu.getAdjustedAngle()));
//        Strafe(0.5f, 5, Direction.LEFT);
//        DriveHeading(0.5f, 40, 90, 0.3f, Direction.BACKWARD);
    }


}
