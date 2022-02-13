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

@TeleOp(name="TeleOp", group="none")
public class Tele extends OpMode{

    DcMotor fr;
    DcMotor fl;
    DcMotor br;
    DcMotor bl;
    DcMotor carousel;
    DcMotor pulley, pulley2;
    DcMotor sweeper;

    Servo intakeLeft, intakeRight; //intakeLeft is not used because one servo is enough
    Servo vbarLeft, vbarRight;
    Servo finger;

    float x1, x2, y1, y2, tr, tl, tr2;

    int currentPosition, currentStage;
    int stage;
    float pos = 0;
    float vposR = 0.8f;
    float vposL = 0.7f;
    float fpos = 0.2f;
    public static int autoCurrentPosition, autoCurrentStage;
    int targetEncoderValue = 0;

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
        vbarRight.setPosition(0.8f);

        finger = hardwareMap.servo.get("finger");
        ServoControllerEx fingerController = (ServoControllerEx) finger.getController();
        int fingerServoPort = finger.getPortNumber();
        PwmControl.PwmRange fingerPwmRange = new PwmControl.PwmRange(600, 2400);
        fingerController.setServoPwmRange(fingerServoPort, fingerPwmRange);
        finger.setPosition(0.2f);

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        fr.setDirection(DcMotorSimple.Direction.REVERSE);
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

        if (gamepad2.dpad_up) {
            stage = 2; // upper shipping hub
            targetEncoderValue = 420;
        } else if (gamepad2.dpad_right) {
            stage = 1; // middle shipping hub
            targetEncoderValue = 200;
        } else if (gamepad2.dpad_down) {
            targetEncoderValue = 0;
            stage = 0; // lower shipping hub
        }

        if (currentStage > stage) {
            if (currentPosition + Globals.pulleyEncoder > targetEncoderValue) {
                currentPosition = pulley.getCurrentPosition();
                pulley.setPower(-0.5f);
                pulley2.setPower(-0.5f);
                Log.i("[phoenix:pulleyInfo]", String.format("currentPulley = %d", currentPosition));
            } else {
                currentStage = stage;
            }
        } else if (currentStage < stage) {
            if (currentPosition + Globals.pulleyEncoder < targetEncoderValue) {
                currentPosition = pulley.getCurrentPosition();
                pulley.setPower(1);
                pulley2.setPower(1.0);
                Log.i("[phoenix:pulleyInfo]", String.format("currentPulley = %d;  targetEncode=%d",  currentPosition, targetEncoderValue));
            } else{
                currentStage = stage;
            }
            Log.i("[phoenix:pulleyInfo]", "Set the currentStage");

        } else if (currentStage == 1 || currentStage == 2){
            pulley.setPower(0.1f);
            pulley2.setPower(0.1f);
        } else{
            pulley.setPower(0);
            pulley2.setPower(0);
        }

        Log.i("[phoenix:pulleyPos]", String.format("power = %f", pulley.getPower()));

        double joystickLeftDistance = Math.pow(x1, 2) + Math.pow(y1, 2);
        if (joystickLeftDistance < 0.9) {
            x1 = x1 / 2;
            y1 = y1 / 2;
        }
        double joystickRightDistance = Math.pow(x2, 2) + Math.pow(y2, 2);
        if (joystickRightDistance < 0.9) {
            x2 = x2 / 2;
        }

        if (gamepad1.left_trigger > 0.1) {
            carousel.setPower(-gamepad2.right_trigger);
            telemetry.addData("Carousel Power: %f", -gamepad2.right_trigger);
            telemetry.update();
        } else if (gamepad2.left_bumper) {
            carousel.setPower(0.5);
        } else {
            carousel.setPower(0);
        }

        Drive(x1, y1 * -1, x2);

        if (gamepad1.y)
            pos = 0;
        else if (gamepad1.x)
            pos = 0.25f;
        else if (gamepad1.a)
            pos = 0.7f;

        intakeRight.setPosition(pos);

        Log.i("[phoenix:servoInfo]", String.format("currentServo = %f", intakeRight.getPosition()));

        if (gamepad1.right_trigger > 0.7)
            sweeper.setPower(1);
        else if (gamepad1.right_trigger > 0.1)
            sweeper.setPower(gamepad1.right_trigger);
        else if (gamepad1.right_bumper)
            sweeper.setPower(-1);
        else
            sweeper.setPower(0);

        if (gamepad2.dpad_left)
            vposR = 0.785f;
        else if (gamepad2.x) {
            vposR = 0.69f;
            fpos = 0.3f;
        }
        else if (gamepad2.b)
            vposR = 0.2f;

        vbarRight.setPosition(vposR);

        if (gamepad2.right_trigger > 0.7)
            fpos = 0.5f;
        else if (gamepad2.right_bumper)
            fpos = 0.1f;

        finger.setPosition(fpos);

        Log.i("[phoenix:servoInfo]", String.format("vbar right pos = %f", vbarRight.getPosition()));

        telemetry.addData("v bar right position: ", vbarRight.getPosition());
        telemetry.update();
    }
}
