package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.ImageNavigation;

@Autonomous(name="Kick Me", group = "none")

public class Teamalan extends LinearOpMode {

    public DcMotor fl;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor br;
    DcMotor carousel;

    public float PPR = 1120;

    public float diameter = 4;

    public MyIMU imu;

    public ImageNavigation imageNavigation;

    public void initialize (){
        fl = hardwareMap.dcMotor.get("frontleft");
        fr = hardwareMap.dcMotor.get("frontright");
        bl = hardwareMap.dcMotor.get("backleft");
        br = hardwareMap.dcMotor.get("backright");
        carousel = hardwareMap.dcMotor.get("carousel");


        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = new MyIMU(hardwareMap);
        BNO055IMU.Parameters p = new BNO055IMU.Parameters();
        imu.initialize(p);

        imageNavigation = new ImageNavigation(hardwareMap, this);
        imageNavigation.init();
    }

    public void StopAll() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    public void Drive (float power, float distance, Direction d) {
        float x = (PPR * distance)/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;

        while (currentPosition < targetEncoderValue && opModeIsActive()) {
            currentPosition = Math.abs(bl.getCurrentPosition());
            if (d == Direction.FORWARD) {
                fl.setPower(power);
                fr.setPower(power);
                bl.setPower(power);
                br.setPower(power);
            }
            if (d == Direction.BACKWARD) {
                fl.setPower(-power);
                fr.setPower(-power);
                bl.setPower(-power);
                br.setPower(-power);
            }
        }

        StopAll();

    }

    public void Turn (float power, int angle, Direction turnDirection, MyIMU imu) {


        Orientation startOrientation = imu.getAngularOrientation();

        float targetangle;
        float currentangle;

        imu.reset(turnDirection);
        if (turnDirection == Direction.COUNTERCLOCKWISE) {

            targetangle = startOrientation.firstAngle + angle;
            currentangle = startOrientation.firstAngle;

            while (currentangle < targetangle && opModeIsActive()) {

                currentangle = imu.getAdjustedAngle();

                Log.i("[pheonix:angleInfo]", String.format("startingAngle = %f, targetAngl = %f, currentAngle = %f", startOrientation.firstAngle, targetangle, currentangle));

                fl.setPower(-power);
                bl.setPower(-power);
                fr.setPower(power);
                br.setPower(power);

            }
        }

        else {

            targetangle = startOrientation.firstAngle - angle;
            currentangle = startOrientation.firstAngle;

            while (currentangle > targetangle && opModeIsActive()) {

                currentangle = imu.getAdjustedAngle();

                this.telemetry.addData("CurrentAngle =: %f", currentangle);
                this.telemetry.update();

                fl.setPower(power);
                bl.setPower(power);
                fr.setPower(-power);
                br.setPower(-power);


            }
        }

        StopAll();

    }

    public void Strafe(float power, float distance, Direction d) {
        float x = (PPR * (2 * distance))/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;

        if (d == Direction.LEFT) {
            while (currentPosition < targetEncoderValue && opModeIsActive()) {
                currentPosition = Math.abs(bl.getCurrentPosition());
                fl.setPower(power);
                fr.setPower(-power);
                bl.setPower(-power);
                br.setPower(power);
            }
        } else {
            while (currentPosition < targetEncoderValue && opModeIsActive()) {
                currentPosition = Math.abs(bl.getCurrentPosition());
                fl.setPower(-power);
                fr.setPower(power);
                bl.setPower(power);
                br.setPower(-power);
            }
        }

        StopAll();
    }

    public float Max(float f1, float f2, float f3, float f4) {
        f1 = Math.abs(f1);
        f2 = Math.abs(f2);
        f3 = Math.abs(f3);
        f4 = Math.abs(f4);


        if(f1>=f2 && f1>=f3 && f1>=f4) return f1;
        if(f2>=f1 && f2>=f3 && f2>=f4) return f2;
        if(f3>=f1 && f1>=f2 && f1>=f4) return f3;
        return f4;



    }

    public void driveHeading(float power, float distance, float heading, Direction turnDirection){

        power = Math.abs(power);

        imu.reset(Direction.CLOCKWISE);

        float currentHeading = imu.getAngularOrientation().firstAngle;

        float x = (PPR * distance)/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);

        Log.i("[phoenix:startValues]", String.format("Heading: %f; Target Encoder: %d", heading, targetEncoderValue));


        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;

        while (currentPosition < targetEncoderValue && opModeIsActive()) {
            Log.i("[phoenix:currentHeading]", String.format("Current Heading: %f", currentHeading));
            float adjustmentPower = 0;
            currentHeading = imu.getAngularOrientation().firstAngle;

            if (currentHeading > 0 && heading > 0) {
                if (currentHeading - heading > 1) {
                    adjustmentPower = 0.5f;
                } else if (currentHeading - heading < -1) {
                    adjustmentPower = -0.5f;
                }
            } else if (currentHeading < 0 && heading < 0) {
                if (currentHeading - heading > 1) {
                    adjustmentPower = 0.5f;
                } else if (currentHeading - heading < -1) {
                    adjustmentPower = -0.5f;
                }
            } else if (currentHeading < 0 && heading > 0) {
                if (currentHeading - heading < -1) {
                    adjustmentPower = -0.5f;
                }
            } else {
                if (currentHeading - heading > 1) {
                    adjustmentPower = 0.5f;
                } else if (currentHeading - heading < -1) {
                    adjustmentPower = -0.5f;
                }
            }

            currentPosition = Math.abs(bl.getCurrentPosition());

            float frontRight = power;
            float frontLeft = power;
            float backRight = power;
            float backLeft = power;

            if (adjustmentPower > 0) {
                frontLeft = (power + adjustmentPower);
                backLeft = (power + adjustmentPower);
            }
            else if (adjustmentPower < 0) {
                frontRight = (power - adjustmentPower);
                backRight = (power - adjustmentPower);
            }

            float theMax = Max(frontLeft, frontRight, backLeft, backRight);

            if (turnDirection == Direction.FORWARD) {
                frontLeft = frontLeft/theMax;
                frontRight = frontRight/theMax;
                backLeft = backLeft/theMax;
                backRight = backRight/theMax;
            } else if (turnDirection == Direction.BACKWARD) {
                frontLeft = -frontLeft/theMax;
                frontRight = -frontRight/theMax;
                backLeft = -backLeft/theMax;
                backRight = -backRight/theMax;
            }


            Log.i("[phoenix:wheelPowers]", String.format("frontLeft: %f; frontRight: %f; backLeft: %f; backRight: %f", frontLeft, frontRight, backLeft, backRight));

            fr.setPower(frontRight);
            fl.setPower(frontLeft);
            bl.setPower(backLeft);
            br.setPower(backRight);
        }

        StopAll();

    }
    public void Carousel(float power){
        float x = (280 * 50)/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);

        carousel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        carousel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;

        while (currentPosition < targetEncoderValue && opModeIsActive()) {
            currentPosition = Math.abs(carousel.getCurrentPosition());

            carousel.setPower(0.5);
        }
        carousel.setPower(0);
    }
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
        Drive(0.5f, 12, Direction.FORWARD);
        sleep(500);
        Turn(0.5f, 90, Direction.CLOCKWISE, imu);
        sleep(500);
        driveHeading(0.5f, 50, -100, Direction.BACKWARD);
    }
}
