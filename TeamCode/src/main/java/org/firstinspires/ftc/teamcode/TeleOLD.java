package org.firstinspires.ftc.teamcode;

import android.os.PowerManager;
import android.os.WorkSource;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.checkerframework.common.util.report.qual.ReportWrite;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

//@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="none")
public class TeleOLD extends OpMode{

    DcMotor fr;
    DcMotor fl;
    DcMotor br;
    DcMotor bl;
    DcMotor carousel;

    float x1, x2, y1, y2, tr, tl;
//    boolean bpr;

    public void Drive(float x1, float y1, float x2) {

        float frontLeft = y1 - x1 + x2; //previous wheel orientation
        float frontRight = y1 + x1 - x2;
        float backLeft = y1 + x1 + x2;
        float backRight = y1 - x1 - x2;

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
        fr = hardwareMap.dcMotor.get("frontright");
        fl = hardwareMap.dcMotor.get("frontleft");
        br = hardwareMap.dcMotor.get("backright");
        bl = hardwareMap.dcMotor.get("backleft");

        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        carousel = hardwareMap.dcMotor.get("carousel");
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

        double joystickLeftDistance = Math.pow(x1, 2) + Math.pow(y1, 2);
        if (joystickLeftDistance < 0.9) {
            x1 = x1 / 2;
            y1 = y1 / 2;
        }
        double joystickRightDistance = Math.pow(x2, 2) + Math.pow(y2, 2);
        if (joystickRightDistance < 0.9) {
            x2 = x2 / 2;
        }

        if (tr > 0.1) {
            carousel.setPower(tr);
            telemetry.addData("Carousel Power: %f", tr);
            telemetry.update();
        } else if (tl > 0.1) {
            carousel.setPower(tl);
            telemetry.addData("Carousel Power: %f", tl);
            telemetry.update();
        } else {
            carousel.setPower(0);
        }

        Drive(x1, y1 * -1, x2);
    }
}
