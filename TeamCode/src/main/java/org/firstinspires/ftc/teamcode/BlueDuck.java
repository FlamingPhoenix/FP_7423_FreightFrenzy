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
        Drive(0.5f, 12, Direction.BACKWARD);
        //place freight
        Turn(0.5f, 90, Direction.CLOCKWISE, imu);
        DriveHeading(0.5f, 30, startHeading - 135, 0.3f, Direction.BACKWARD);
        //spin ducky amd score
        //Carousel(0.4f);
        Turn(0.5f, 80, Direction.CLOCKWISE, imu);
        DriveHeading(0.5f, 23, startHeading + 155, 0.3f, Direction.FORWARD);
    }

}
