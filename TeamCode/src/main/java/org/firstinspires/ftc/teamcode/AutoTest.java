package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Test", group="none")

public class AutoTest extends AutoBase {




    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        Turn(1, 180, Direction.CLOCKWISE, imu);

        sleep(100);

        StrafeUntilHeading(0.6f, 2, startHeading + 180, 24, Direction.LEFT);


        //DriveHeading(0.5f, 24, -45, Direction.FORWARD);
        //DriveToPointHeading(0.5f, -12, -65);

//        Strafe(0.5f, 20, Direction.LEFT);
//        sleep(20000);
//        Strafe(0.5f, 20, Direction.RIGHT);



    }

}
