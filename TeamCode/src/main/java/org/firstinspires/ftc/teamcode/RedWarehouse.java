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
