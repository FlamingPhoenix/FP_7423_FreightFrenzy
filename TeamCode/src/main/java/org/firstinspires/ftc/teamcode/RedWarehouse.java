package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="RedWarehouse", group = "none")

public class RedWarehouse extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();

        int duckPos = 2;

        waitForStart();

        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + 1000) {//rik method
            duckPos = imageNavigation.getDuckies();
            telemetry.addData("ducky: %d", duckPos);
            telemetry.update();
            sleep(10);
        }

        Drive(0.5f, 12, Direction.BACKWARD);
        Strafe(0.8f, 6, Direction.RIGHT);
//        TurnUntilImage(0.5f, Direction.CLOCKWISE, 135);
        Turn(0.5f, 150, Direction.CLOCKWISE, imu);
        sleep(200);
        Turn(0.5f, 30, Direction.COUNTERCLOCKWISE, imu);

        DriveToPointHeading(0.35f, 18, -60);

//        while(imageNavigation.getRobotPosition().y < -45){
//            fl.setPower(0.3);
//            fr.setPower(0.3);
//            bl.setPower(0.3);
//            br.setPower(0.3);
//            sleep(10);
//        }
        StopAll();
        //drop freight

    }


}
