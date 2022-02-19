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
    float maxPower = 0.8f;

    int currentPosition, currentStage;
    int stage;
    float intakePos = 0.7f;
    float vposR = 0.8f; // 0.785 or 0.8 to pick up freight; 0.69 to pull out; 0.2 to drop freight; 0.8 initialize
    float fpos = 0.2f;
    public static int autoCurrentPosition, autoCurrentStage;
    int targetEncoderValue = 0;
    boolean isDropping = false;

    boolean isReturning = false;
    double returningTime;

    boolean isTransferring = false;
    double intakeTime;

    boolean isClamping = false;
    double clampTime;

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

        fl.setPower(frontLeft * maxPower);
        fr.setPower(frontRight * maxPower);
        bl.setPower(backLeft * maxPower);
        br.setPower(backRight * maxPower);
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
        intakeRight.setPosition(intakePos); //starting position

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
        vbarRight.setPosition(vposR);

        finger = hardwareMap.servo.get("finger");
        ServoControllerEx fingerController = (ServoControllerEx) finger.getController();
        int fingerServoPort = finger.getPortNumber();
        PwmControl.PwmRange fingerPwmRange = new PwmControl.PwmRange(600, 2400);
        fingerController.setServoPwmRange(fingerServoPort, fingerPwmRange);
        finger.setPosition(fpos);

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

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

        if (currentStage == 1 || currentStage == 2)
            maxPower = 0.25f;
        else
            maxPower = 0.8f;

        if (currentStage > stage) {
            if (currentPosition + Globals.pulleyEncoder > targetEncoderValue) {
                if (!isDropping && !isReturning) {
                    isReturning = true;
                    returningTime = System.currentTimeMillis() + 1000;
                    vposR = 0.72f;
                    fpos = 0.4f; // neutral position
                    vbarRight.setPosition(vposR);
                    finger.setPosition(fpos);
                } else if (isReturning && System.currentTimeMillis() - returningTime >= 0) {
                    currentPosition = pulley.getCurrentPosition();
                    pulley.setPower(-0.5f);
                    pulley2.setPower(-0.5f);
                    isReturning = false;
                }
                Log.i("[phoenix:pulleyInfo]", String.format("currentPulley = %d", currentPosition));
            } else {
                currentStage = stage;

                if (currentStage == 0) {
                    fpos = 0;
                    finger.setPosition(fpos);
                }
            }
        } else if (currentStage < stage) {
            if (currentPosition + Globals.pulleyEncoder < targetEncoderValue) {
                if (!isDropping) {
                    vposR = 0.69f;
                    vbarRight.setPosition(vposR);
                }
                currentPosition = pulley.getCurrentPosition();
                pulley.setPower(1);
                pulley2.setPower(1);
                Log.i("[phoenix:pulleyInfo]", String.format("currentPulley = %d;  targetEncode=%d",  currentPosition, targetEncoderValue));

                if (currentPosition + Globals.pulleyEncoder >= 180) {
                    vposR = 0.2f;
                    isDropping = true;
                    vbarRight.setPosition(vposR);
                }
            } else{
                currentStage = stage;
            }
            Log.i("[phoenix:pulleyInfo]", "Set the currentStage");

        } else if (currentStage == 1 || currentStage == 2){
            pulley.setPower(0.1f);
            pulley2.setPower(0.1f);
        } else {
            pulley.setPower(0);
            pulley2.setPower(0);
            vposR = 0.8f;
            vbarRight.setPosition(vposR);
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
            carousel.setPower(-gamepad1.left_trigger);
            telemetry.addData("Carousel Power: %f", -gamepad1.left_trigger);
            telemetry.update();
        } else if (gamepad1.left_bumper) {
            carousel.setPower(0.5);
        } else {
            carousel.setPower(0);
        }

        Drive(x1, y1 * -1, x2);

        if (isTransferring && System.currentTimeMillis() - intakeTime > 0) {
            isTransferring = false;
            sweeper.setPower(0);
            intakePos = 0.7f;

            isClamping = true;
            clampTime = System.currentTimeMillis() + 1000;
        }

        if (gamepad1.y)
            intakePos = 0;
        else if (gamepad1.x && !isTransferring) {
            intakeTime = System.currentTimeMillis() + 2000;
            intakePos = 0.4f;
            fpos = 0.1f;
            sweeper.setPower(0.8);
            isTransferring = true;
        }
        else if (gamepad1.a)
            intakePos = 0.7f;

        intakeRight.setPosition(intakePos);

        Log.i("[phoenix:servoInfo]", String.format("currentServo = %f", intakeRight.getPosition()));

        if (gamepad1.right_trigger > 0.7)
            sweeper.setPower(0.8);
        else if (gamepad1.right_trigger > 0.1)
            sweeper.setPower(gamepad1.right_trigger * 0.8);
        else if (gamepad1.right_bumper)
            sweeper.setPower(-0.8);
        else if (!isTransferring)
            sweeper.setPower(0);

        if (isClamping && System.currentTimeMillis() - clampTime > 0) {
            isClamping = false;
            vposR = 0.72f; //keep in mind to maybe lower with continued issues
            fpos = 0.5f;
            vbarRight.setPosition(vposR);
        }

        if (isDropping && gamepad2.right_trigger > 0.7) { // finger down
            fpos = 0.5f;
        }
        else if (gamepad2.right_bumper) { // finger up
            fpos = 0.1f;
            isDropping = false;
        }

        if(gamepad2.left_trigger > 0.4)
            fpos = 0.5f;
        else if(gamepad2.left_bumper)
            fpos = 0.1f;

        finger.setPosition(fpos);

        Log.i("[phoenix:servoInfo]", String.format("vbar right pos = %f", vbarRight.getPosition()));

        telemetry.addData("v bar right position: ", vbarRight.getPosition());
        telemetry.addData("pulley position ", pulley.getCurrentPosition());
        telemetry.addData("global encoder: ", Globals.pulleyEncoder);
        telemetry.update();
        Log.i("[phoenix:encoder]", String.format("vbar right pos = %d", Globals.pulleyEncoder));

    }
}
