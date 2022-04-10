package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
@TeleOp(name = "Sensor Test", group = "none")
public class SensorTest extends OpMode {

    DistanceSensor distanceSensor;


    @Override
    public void init() {
        distanceSensor = hardwareMap.get(DistanceSensor.class, "distanceSensor");
    }

    @Override
    public void loop() {
        double d = distanceSensor.getDistance(DistanceUnit.INCH);
        telemetry.addData("Distance from object %f", d);
        telemetry.update();
    }
}
