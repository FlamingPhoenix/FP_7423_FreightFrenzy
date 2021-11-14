package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="RedWarehouse", group = "none")

public class RedWarehouse extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
//        Drive(0.5f, 12);
//        Strafe(0.5f, 12, Direction.LEFT);

//        Turn(0.5f, 45, Direction.COUNTERCLOCKWISE, imu);
//        driveHeading(0.5f, 50, 30);
//            if(imageNavigation != null){
//                int duckPos = imageNavigation.getDuckies();
//                telemetry.addData("ducky: %d", duckPos);
//                telemetry.update();
//                sleep(500);
//                imageNavigation.getPosition();
//        Drive(0.5f, 12, Direction.FORWARD);
//        sleep(500);
//        Turn(0.5f, 90, Direction.CLOCKWISE, imu);
//        sleep(500);
//        driveHeading(0.5f, 45, -55, Direction.BACKWARD);
//        Carousel(0.7f);

//        DriveToPoint(0.5f,12,48);

        int duckPos = imageNavigation.getDuckies(); //0 is left, 1 is middle, and 2 is right
        telemetry.addData("ducky: %d", duckPos);
        telemetry.update();

        Drive(0.5f, 12, Direction.BACKWARD);
        TurnUntilImage(0.5f, Direction.CLOCKWISE, 135);

        while(imageNavigation.getRobotPosition().y < -45){
            fl.setPower(0.3);
            fr.setPower(0.3);
            bl.setPower(0.3);
            br.setPower(0.3);
            sleep(10);
        }
        StopAll();
        //drop freight

    }

}
