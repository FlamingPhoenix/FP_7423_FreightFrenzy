package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Test", group="none")

public class AutoTest extends AutoBase {

    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        Drive(60, Direction.FORWARD);


        //Turn(0.5f, 125, Direction.COUNTERCLOCKWISE, imu);

        //DriveHeading(0.25f, 35, startHeading + 135, 0.3f, Direction.BACKWARD);



        //MovePulley(0.5f, 1);


//        Turn(1, 180, Direction.CLOCKWISE, imu);

//        sleep(100);

//        StrafeUntilHeading(0.6f, 2, startHeading + 180, 24, Direction.RIGHT);


        //DriveHeading(0.5f, 24, -45, Direction.FORWARD);
        //DriveToPointHeading(0.5f, -12, -65);

//        Strafe(0.5f, 20, Direction.LEFT);
//        sleep(20000);
//        Strafe(0.5f, 20, Direction.RIGHT);



    }

}
