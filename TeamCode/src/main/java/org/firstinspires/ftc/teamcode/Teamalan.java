package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Alan", group = "none")

public class Teamalan extends LinearOpMode {

    public DcMotor fl;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor br;

    public float PPR = 1120;

    public float diameter = 4;

    public BNO055IMU imu;

    public void initialize (){
        fl = hardwareMap.dcMotor.get("frontleft");
        fr = hardwareMap.dcMotor.get("frontright");
        bl = hardwareMap.dcMotor.get("backleft");
        br = hardwareMap.dcMotor.get("backright");

        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(BNO055IMU.class, "imu1");
        BNO055IMU.Parameters p = new BNO055IMU.Parameters();
        p.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(p);

    }

    public void Drive (float power, float distance) {
        float x = (PPR * distance)/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;

        while (currentPosition < targetEncoderValue && opModeIsActive()) {
            currentPosition = Math.abs(bl.getCurrentPosition());
            fl.setPower(power);
            fr.setPower(power);
            bl.setPower(power);
            br.setPower(power);
        }

        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);

    }

    public void Turn (float power, int angle, BNO055IMU imu) {


        Orientation startOrientation = imu.getAngularOrientation();

        float targetangle;
        float currentangle;

        targetangle = startOrientation.firstAngle + angle;
        currentangle = startOrientation.firstAngle;

        while (currentangle < targetangle && opModeIsActive()) {

            currentangle = imu.getAngularOrientation().firstAngle;

            this.telemetry.addData("CurrentAngle =: %f", currentangle);
            this.telemetry.update();

            fl.setPower(power);
            bl.setPower(power);
            fr.setPower(-power);
            br.setPower(-power);


        }
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);


    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        Drive(0.5f,10);
        sleep(1000);
        Drive(-0.5f,24);
    }
}
