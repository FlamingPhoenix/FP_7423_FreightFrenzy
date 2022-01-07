package org.firstinspires.ftc.teamcode;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="RedDuck", group = "none")

public class RedDuck extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        int duckPos = 2;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + 1000) {
            duckPos = imageNavigation.getDuckies();
            telemetry.addData("ducky: %d", duckPos);
            telemetry.update();
            sleep(10);
        }
        Drive(0.3f, 12, Direction.BACKWARD);
        sleep(100);
        MovePulley(0.5f, 1);//stage tbd with ducky
        //place freight
        Turn(0.25f, 90, Direction.COUNTERCLOCKWISE, imu);
        DriveHeading(0.25f, 35, startHeading + 135, 0.3f, Direction.BACKWARD);
        //spin ducky amd score
        Carousel(0.4f);
        Turn(0.25f, 80, Direction.COUNTERCLOCKWISE, imu);
        DriveHeading(0.25f, 54,  startHeading + 155, 0.3f, Direction.FORWARD);
        StrafeUntilHeading(0.5f, 2, startHeading + 180, 10, Direction.LEFT);

        copyToTele();
    }

}
