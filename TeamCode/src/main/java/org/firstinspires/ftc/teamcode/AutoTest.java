package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Auto Test", group="none")

public class AutoTest extends AutoBase {




    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        //DriveHeading(0.5f, 24, -45, Direction.FORWARD);
        DriveToPointHeading(0.5f, 12, 60);

    }

}
