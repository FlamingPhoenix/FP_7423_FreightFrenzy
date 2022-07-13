package org.firstinspires.ftc.teamcode;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="BlueDuck", group = "none")

public class BlueDuck extends AutoBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        int duckPos;
        duckPos = useCamera();

        OnStart(duckPos);

        Turn(0.5f, 120, Direction.CLOCKWISE, imu);

        MovePulley(0.9f, duckPos);

        sleep(750);

        finger.setPosition(0.1);

        sleep(750);

        finger.setPosition(0.3);
        MovePulley(0.7f, 0);

        sleep(100);

        vbarRight.setPosition(0.79);

        Strafe(0.8f, 2, Direction.RIGHT);

        //place freight
        DriveHeading(0.25f, 23, startHeading - 130, 0.3f, Direction.BACKWARD);
        //spin ducky amd score
        Carousel(0.8f);
        Turn(0.5f, 80, Direction.CLOCKWISE, imu);
        DriveHeading(0.25f, 18f,  startHeading + 175, 0.3f, Direction.FORWARD);
        StrafeUntilHeading(0.5f, 2, startHeading - 180, 4, Direction.RIGHT);
    }

}
