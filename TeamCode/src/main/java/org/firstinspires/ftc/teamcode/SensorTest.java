package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
@Autonomous(name = "Sensor Test", group = "none")
public class SensorTest extends AutoBase {
    public void runOpMode() throws InterruptedException {
        testInitialize();
        waitForStart();
        while (true) {
            double d = distanceSensor.getDistance(DistanceUnit.INCH);
            telemetry.addData("Distance from object %f", d);
            telemetry.update();
        }
    }
}
