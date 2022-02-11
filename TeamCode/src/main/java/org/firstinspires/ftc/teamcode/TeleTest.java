package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleTest", group="none")
public class TeleTest extends OpMode{

    DcMotor fr;
    DcMotor fl;
    DcMotor br;
    DcMotor bl;
    DcMotor carousel;
    DcMotor pulley, pulley2;
    DcMotor sweeper;

    Servo intakeLeft, intakeRight; //intakeLeft is not used because one servo is enough
    Servo vbarLeft, vbarRight;

    float x1, x2, y1, y2, tr, tl, tr2;

    int currentPosition, currentStage;
    int stage;
    float pos = 0.5f;
    float vposR = 0.58f;
    float vposL = 0.7f;
    public static int autoCurrentPosition, autoCurrentStage;

//    boolean bpr;

    float maxEncoderPulley = 420f; //448 is true max

    public void Drive(float x1, float y1, float x2) {

        float frontLeft = y1 + x1 + x2; //new wheel orientation
        float frontRight = y1 - x1 - x2;
        float backLeft = y1 - x1 + x2;
        float backRight = y1 + x1 - x2;

        frontLeft = Range.clip(frontLeft, -1, 1);
        frontRight = Range.clip(frontRight, -1, 1);
        backLeft = Range.clip(backLeft, -1, 1);
        backRight = Range.clip(backRight, -1, 1);

        fl.setPower(frontLeft);
        fr.setPower(frontRight);
        bl.setPower(backLeft);
        br.setPower(backRight);
    }

    @Override
    public void init() {
        currentPosition = autoCurrentPosition;
        currentStage = autoCurrentStage;

        fr = hardwareMap.dcMotor.get("frontright");
        fl = hardwareMap.dcMotor.get("frontleft");
        br = hardwareMap.dcMotor.get("backright");
        bl = hardwareMap.dcMotor.get("backleft");
        pulley = hardwareMap.dcMotor.get("pulley");
        pulley2 = hardwareMap.dcMotor.get("pulley2");
        sweeper = hardwareMap.dcMotor.get("sweeper");

        intakeRight = hardwareMap.servo.get("intakeright");
        ServoControllerEx intakeRightController = (ServoControllerEx) intakeRight.getController();
        int intakeRightServoPort = intakeRight.getPortNumber();
        PwmControl.PwmRange intakeRightPwmRange = new PwmControl.PwmRange(600, 2400);
        intakeRightController.setServoPwmRange(intakeRightServoPort, intakeRightPwmRange);
        intakeRight.setPosition(0); //starting position

        vbarLeft = hardwareMap.servo.get("vbarleft");
        ServoControllerEx vbarLeftController = (ServoControllerEx) vbarLeft.getController();
        int vbarLeftServoPort = vbarLeft.getPortNumber();
        PwmControl.PwmRange vbarLeftPwmRange = new PwmControl.PwmRange(600, 2400);
        vbarLeftController.setServoPwmRange(vbarLeftServoPort, vbarLeftPwmRange);

        vbarRight = hardwareMap.servo.get("vbarright");
        ServoControllerEx vbarRightController = (ServoControllerEx) vbarRight.getController();
        int vbarRightServoPort = vbarRight.getPortNumber();
        PwmControl.PwmRange vbarRightPwmRange = new PwmControl.PwmRange(600, 2400);
        vbarRightController.setServoPwmRange(vbarRightServoPort, vbarRightPwmRange);
        vbarRight.setPosition(0.58f);

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        fr.setDirection(DcMotorSimple.Direction.REVERSE); //uncomment for competition robot
        pulley2.setDirection(DcMotorSimple.Direction.REVERSE);

        carousel = hardwareMap.dcMotor.get("carousel");

        pulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pulley2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pulley.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pulley2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Ready for start %f", 0);
        telemetry.update();

    }

    @Override
    public void loop() {
        x1 = gamepad1.left_stick_x;
        y1 = gamepad1.left_stick_y;
        x2 = gamepad1.right_stick_x;
        y2 = gamepad1.right_stick_y;
        tr = gamepad1.right_trigger;
//        bpr = gamepad1.right_bumper;
        tl = gamepad1.left_trigger;
        tr2 = gamepad2.right_trigger;

        if (gamepad2.dpad_left) {
            vposR += 0.001f;//zero to 0.78
            vposL -= 0.001f;
        }
        else if (gamepad2.dpad_right) {
            vposR -= 0.001f;
            vposL += 0.001f;
        }

        //vbarLeft.setPosition(vposL);
        vbarRight.setPosition(vposR);

        Log.i("[pheonix:servoInfo]", String.format("vbar right pos = %f", vbarRight.getPosition()));
        telemetry.addData("v bar right position: ", vbarRight.getPosition());
        telemetry.update();
    }
}
