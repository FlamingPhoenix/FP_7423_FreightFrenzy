package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
        //place freight
        Turn(0.25f, 90, Direction.COUNTERCLOCKWISE, imu);
        DriveHeading(0.25f, 30, startHeading + 135, 0.3f, Direction.BACKWARD);
        //spin ducky amd score
        Carousel(0.4f);
        Turn(0.25f, 80, Direction.COUNTERCLOCKWISE, imu);
        DriveHeading(0.25f, 25, startHeading - 155, 0.3f, Direction.FORWARD);
        Strafe(0.5f, 5, Direction.RIGHT);
    }

}
