package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="BlueDuck", group = "none")

public class BlueDuck extends AutoBase {

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
        //place freight
        Turn(0.25f, 90, Direction.CLOCKWISE, imu);
        DriveHeading(0.25f, 21, startHeading - 135, 0.3f, Direction.BACKWARD);
        //spin ducky amd score
        Carousel(-0.5f);
        Turn(0.25f, 80, Direction.CLOCKWISE, imu);
        DriveHeading(0.25f, 20, startHeading + 155, 0.3f, Direction.FORWARD);
        Strafe(0.5f,5, Direction.LEFT);
    }

}
