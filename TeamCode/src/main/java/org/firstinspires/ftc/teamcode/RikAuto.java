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

@Autonomous(name="rikAuto", group="none")

public class RikAuto extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        DcMotor fl;
        DcMotor fr;
        DcMotor bl;
        DcMotor br;

        fl = hardwareMap.dcMotor.get("frontleft");
        fr = hardwareMap.dcMotor.get("frontright");
        bl = hardwareMap.dcMotor.get("backleft");
        br = hardwareMap.dcMotor.get("backright");

        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

//        int distance = 24;
//        float PPR = 3895.9F;
//        float power = 0.5F;
//
//        float x = (PPR * distance)/(4F * (float)Math.PI);
//        int targetEncoderValue = Math.round(x);
//
//        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        int currentPosition = 0;
//
//        while (Math.abs(currentPosition) < targetEncoderValue && this.opModeIsActive()) {
//
//            currentPosition = (Math.abs(br.getCurrentPosition()));
//            fl.setPower(power);
//            fr.setPower(power);
//            bl.setPower(power);
//            br.setPower(power);
//        }
//
//        fl.setPower(0);
//        fr.setPower(0);
//        bl.setPower(0);
//        br.setPower(0);
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(imu.getParameters());

        Orientation startOrientation = imu.getAngularOrientation();

        float targetAngle;
        float currentAngle;
        float power = 0.5f;
        power *= -1;

        targetAngle = startOrientation.firstAngle - 90;
        currentAngle = startOrientation.firstAngle;

        while (currentAngle > targetAngle && opModeIsActive()) {

            currentAngle = imu.getAngularOrientation().firstAngle;
            fl.setPower(-power);
            fr.setPower(power);
            bl.setPower(-power);
            br.setPower(power);
        }

        fl.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        fr.setPower(0);




    }
}
