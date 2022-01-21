package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOp", group="none")
public class Tele extends OpMode{

    DcMotor fr;
    DcMotor fl;
    DcMotor br;
    DcMotor bl;
    DcMotor carousel;
    DcMotor pulley, pulley2;

    float x1, x2, y1, y2, tr, tl, tr2;

    int currentPosition, currentStage;
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

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        fr.setDirection(DcMotorSimple.Direction.REVERSE); //uncomment for competition robot
        pulley2.setDirection(DcMotorSimple.Direction.REVERSE);

        carousel = hardwareMap.dcMotor.get("carousel");

        pulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        pulley.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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

        if (gamepad2.left_stick_y < -0.1 && currentPosition + Globals.pulleyEncoder < maxEncoderPulley) {
            pulley.setPower(-gamepad2.left_stick_y*0.75);
            pulley2.setPower(-gamepad2.left_stick_y*0.75);
            currentPosition = pulley.getCurrentPosition();
        } else if (gamepad2.left_stick_y > 0.1 && currentPosition + Globals.pulleyEncoder > 0) {
            pulley.setPower(-gamepad2.left_stick_y*0.25);
            pulley2.setPower(-gamepad2.left_stick_y*0.25);
            currentPosition = pulley.getCurrentPosition();
        } else {
            pulley.setPower(0);
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

        if (tr2 > 0.1) {
            carousel.setPower(-tr2);
            telemetry.addData("Carousel Power: %f", -tr2);
            telemetry.update();
        } else if (gamepad2.right_bumper) {
            carousel.setPower(0.5);
        } else {
            carousel.setPower(0);
        }

        Drive(x1, y1 * -1, x2);
    }
}
