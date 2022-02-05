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

    Servo intakeLeft, intakeRight;
    Servo vbarLeft, vbarRight;

    float x1, x2, y1, y2, tr, tl, tr2;

    int currentPosition, currentStage;
    int stage;
    float pos = 0.5f;
    float vpos = 0f;
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

        intakeLeft = hardwareMap.servo.get("intakeleft");
        ServoControllerEx intakeLeftController = (ServoControllerEx) intakeLeft.getController();
        int intakeLeftServoPort = intakeLeft.getPortNumber();
        PwmControl.PwmRange intakeLeftPwmRange = new PwmControl.PwmRange(600, 2400);
        intakeLeftController.setServoPwmRange(intakeLeftServoPort, intakeLeftPwmRange);
        //intakeLeft.setPosition(1); //starting position

        intakeRight = hardwareMap.servo.get("intakeright");
        ServoControllerEx intakeRightController = (ServoControllerEx) intakeRight.getController();
        int intakeRightServoPort = intakeRight.getPortNumber();
        PwmControl.PwmRange intakeRightPwmRange = new PwmControl.PwmRange(600, 2400);
        intakeRightController.setServoPwmRange(intakeRightServoPort, intakeRightPwmRange);
//        intakeRight.setPosition(0); //starting position

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

        Log.i("[phoenix:pulleyPosition]", String.format("currentPosition = %d, adjustedCurrentPosition = %d", currentPosition, currentPosition + Globals.pulleyEncoder));

        if (gamepad2.left_stick_y < -0.1) {
            float pulleyPower = -gamepad2.left_stick_y * 0.9f;
            if (pulleyPower > 1)
                pulleyPower = 1;
            Log.i("[phoenix:pulleyPosition]", String.format("power = %f", pulleyPower));

            if (currentPosition + Globals.pulleyEncoder >= maxEncoderPulley) {
                pulleyPower = 0.1f;
            }
            pulley.setPower(pulleyPower);
            pulley2.setPower(pulleyPower);
            currentPosition = pulley.getCurrentPosition();
            Log.i("[phoenix:pulleyPosition]", String.format("power = %f", pulleyPower));
        } else if (gamepad2.left_stick_y > 0.1 && currentPosition + Globals.pulleyEncoder > 0) {
            pulley.setPower(-gamepad2.left_stick_y*0.5);
            pulley2.setPower(-gamepad2.left_stick_y*0.5);
            currentPosition = pulley.getCurrentPosition();
        } else {
            pulley.setPower(0);
            pulley2.setPower(0);
            currentPosition = pulley.getCurrentPosition();
        }

        Log.i("[phoenix:pulleyPosition]", String.format("power = %f", pulley.getPower()));

        int targetEncoderValue = 0;



        if (gamepad2.dpad_up) {
            stage = 2;
            targetEncoderValue = 420;
        } else if (gamepad2.dpad_right) {
            stage = 1;
            targetEncoderValue = 200;
        } else if (gamepad2.dpad_down) {
            targetEncoderValue = 0;
            stage = 0;
        }

        int power = 1;
        if (currentStage > stage) {
            while (currentPosition > targetEncoderValue) {
                currentPosition = pulley.getCurrentPosition();
                Globals.pulleyEncoder = currentPosition;
                pulley.setPower(-0.5*power);
                pulley2.setPower(-0.5*power);
                Log.i("[pheonix:pulleyInfo]", String.format("currentPulley = %d", currentPosition));
            }
            currentStage = stage;
        } else if (currentStage < stage) {
            while (currentPosition < targetEncoderValue) {
                currentPosition = pulley.getCurrentPosition();
                Globals.pulleyEncoder = currentPosition;
                pulley.setPower(power);
                pulley2.setPower(power);
                Log.i("[pheonix:pulleyInfo]", String.format("currentPulley = %d", currentPosition));
            }
            currentStage = stage;
        }

        double joystickLeftDistance = Math.pow(x1, 2) + Math.pow(y1, 2);
        if (joystickLeftDistance < 0.9) {
            x1 = x1 / 2;
            y1 = y1 / 2;
        }
        double joystickRightDistance = Math.pow(x2, 2) + Math.pow(y2, 2);
        if (joystickRightDistance < 0.9) {
            x2 = x2 / 2;
        }

//        if (tr2 > 0.1) {
//            carousel.setPower(-tr2);
//            telemetry.addData("Carousel Power: %f", -tr2);
//            telemetry.update();
//        } else if (gamepad2.right_bumper) {
//            carousel.setPower(0.5);
//        } else {
//            carousel.setPower(0);
//        }

        Drive(x1, y1 * -1, x2);


        intakeRight.setPosition(pos);
//        intakeLeft.setPosition(pos); //servo all the way back is 1, down is 0.3
        if (gamepad2.a)
            pos -= 0.01;
        else if (gamepad2.y)
            pos += 0.01;
        Log.i("[pheonix:servoInfo]", String.format("currentServo = %f", intakeRight.getPosition()));
        telemetry.addData("pulleyPos: ", intakeRight.getPosition());
        telemetry.update();

        if (tr2 > 0.7)
            sweeper.setPower(1);
        else if (tr2 > 0.1)
            sweeper.setPower(tr2);
        else if (gamepad2.right_bumper)
            sweeper.setPower(-1);
        else
            sweeper.setPower(0);


        vbarLeft.setPosition(1);
        vbarRight.setPosition(vpos);
        if (gamepad2.x)
            vpos -= 0.01;
        else if (gamepad2.b)
            vpos += 0.01;
        Log.i("[pheonix:servoInfo]", String.format("vbar right pos = %f", vbarRight.getPosition()));
        Log.i("[pheonix:servoInfo]", String.format("v bar left pos = %f", vbarLeft.getPosition())); // limit: 0.05 bottom; 0.65 top
//        telemetry.addData("vbarright: ", vbarLeft.getPosition());
//        telemetry.addData("vbarleft: ", vbarLeft.getPosition());
        telemetry.update();

        if (tr2 > 0.7)
            sweeper.setPower(1);
        else if (tr2 > 0.1)
            sweeper.setPower(tr2);
        else if (gamepad2.right_bumper)
            sweeper.setPower(-1);
        else
            sweeper.setPower(0);


    }
}
