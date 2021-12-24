package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class AutoBase extends LinearOpMode {

    public DcMotor fl;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor br;
    DcMotor carousel;

    public float PPR = 537.7f; //537.7 for actual robot; 1120 for programming bot

    public float diameter = 4;

    public MyIMU imu;

    public ImageNavigation imageNavigation;

    public float startHeading;

    public void initialize (){
        fl = hardwareMap.dcMotor.get("frontleft");
        fr = hardwareMap.dcMotor.get("frontright");
        bl = hardwareMap.dcMotor.get("backleft");
        br = hardwareMap.dcMotor.get("backright");
        //carousel = hardwareMap.dcMotor.get("carousel");


        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

//        fr.setDirection(DcMotorSimple.Direction.REVERSE); //for the actual robot

        imu = new MyIMU(hardwareMap);
        BNO055IMU.Parameters p = new BNO055IMU.Parameters();
        imu.initialize(p);

        imageNavigation = new ImageNavigation(hardwareMap, this);
        imageNavigation.init();

        startHeading = imu.getAdjustedAngle();
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
                fl.setPower(-power);
                fr.setPower(power);
                bl.setPower(power);
                br.setPower(-power);
            }
        } else {
            while (currentPosition < targetEncoderValue && opModeIsActive()) {
                currentPosition = Math.abs(bl.getCurrentPosition());
                fl.setPower(power);
                fr.setPower(-power);
                bl.setPower(-power);
                br.setPower(power);
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

    public void DriveHeading(float power, float distance, float heading, float adjust, Direction turnDirection){

        power = Math.abs(power);

        imu.reset(Direction.NONE);

        float currentHeading = imu.getAdjustedAngle();

        float x = (PPR * distance)/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);

        Log.i("[phoenix:startValues]", String.format("Heading: %f; Target Encoder: %d", heading, targetEncoderValue));


        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;

        while (currentPosition < targetEncoderValue && opModeIsActive()) {
            Log.i("[phoenix:currentHead]", String.format("Current Heading: %f; heading: %f", currentHeading, heading));
            float adjustmentPower = 0;
            currentHeading = imu.getAdjustedAngle();

            if (currentHeading > 0 && heading > 0) {
                if (currentHeading - heading > 1) {
                    adjustmentPower = adjust;
                } else if (currentHeading - heading < -1) {
                    adjustmentPower = -adjust;
                }
            } else if (currentHeading < 0 && heading < 0) {
                if (currentHeading - heading > 1) {
                    adjustmentPower = adjust;
                } else if (currentHeading - heading < -1) {
                    adjustmentPower = -adjust;
                }
            } else if (currentHeading < 0 && heading > 0) {
                if (currentHeading - heading < -1) {
                    adjustmentPower = -adjust;
                }
            } else {
                if (currentHeading - heading > 1) {
                    adjustmentPower = adjust;
                } else if (currentHeading - heading < -1) {
                    adjustmentPower = -adjust;
                }
            }

            if (turnDirection == Direction.BACKWARD)
                adjustmentPower = adjustmentPower * -1;

            currentPosition = Math.abs(bl.getCurrentPosition());

            float frontRight = power;
            float frontLeft = power;
            float backRight = power;
            float backLeft = power;

            if (adjustmentPower > 0) {  //positive means give more power to the left
                frontLeft = (power + adjustmentPower);
                backLeft = (power + adjustmentPower);
            }
            else if (adjustmentPower < 0) {
                frontRight = (power - adjustmentPower);
                backRight = (power - adjustmentPower);
            }

            if (turnDirection == Direction.FORWARD) {
                setMaxPower(frontLeft, frontRight, backLeft, backRight);
            } else if (turnDirection == Direction.BACKWARD) {
                setMaxPower(-frontLeft, -frontRight, -backLeft, -backRight);
            }


            Log.i("[phoenix:wheelPowers]", String.format("frontLeft: %f; frontRight: %f; backLeft: %f; backRight: %f", frontLeft, frontRight, backLeft, backRight));
        }

        StopAll();

    }

    public void setMaxPower(float flp, float frp, float blp, float brp) {
        float max = Max(Math.abs(flp), Math.abs(frp), Math.abs(blp), Math.abs(brp));

        if (max > 1) {
            fl.setPower(flp/max);
            fr.setPower(frp/max);
            bl.setPower(blp/max);
            br.setPower(brp/max);
        } else {
            fl.setPower(flp);
            fr.setPower(frp);
            bl.setPower(blp);
            br.setPower(brp);
        }
    }


    public void Carousel(float power){
        // technically 910 but adding more to get the ducky off the carousel
        float x = (PPR * 3.25f)/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);
        int targetTime = 3000;

        carousel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        carousel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;
        int currentTime = 0;

        carousel.setPower(-power);
        sleep(targetTime);
//        while (currentPosition < targetEncoderValue && opModeIsActive()) {
//            currentPosition = Math.abs(carousel.getCurrentPosition());
//        }
        carousel.setPower(0);
    }

    public void DriveToPoint (float power, int endX, int endY) {
        power = Math.abs(power);
        RobotPosition startPosition = imageNavigation.getRobotPosition();
        Log.i("[Phoenix:startPosition]", "got position");

        if (startPosition == null)  {
            return;
        }

        float startX = startPosition.x;
        float startY = startPosition.y;
        imu.reset(Direction.NONE);
        float startAngle = imu.getAdjustedAngle();

        boolean movingX;
        boolean movingY;

        float x = endX - startX;
        float y = endY - startY;
        float epsilon = 0.1f;

        Log.i("[Phoenix:DriveToPoint]", String.format("x: %f, y: %f", x, y));

        while (x > 0 || y > 0 && opModeIsActive()) {

            RobotPosition currentPosition = imageNavigation.getRobotPosition();

            float currentX = currentPosition.x;
            float currentY = currentPosition.y;
            float currentAngle = imu.getAdjustedAngle();

            x = endX - currentX;
            y = endY - currentY;

            if (x <= 0) {
                x = 0;
            }
            if (y <= 0) {
                y = 0;
            }

            float p = 2 * Math.abs(x / (endX - startX)) * power;
            float q = Math.abs(y / (endY - startY)) * power;

            if (p > q)
                p = q;

            if (p < 0.2 && x > 0)
                p = 0.2f;
            if (q < 0.1 && y > 0)
                q = 0.1f;

            p *= -1;
            q *= -1;

            setMaxPower(-p+q, p+q, (p+q)/2, (-p+q)/2);

            Log.i("[phoenix:wheelPowers]", String.format("currentX: %f; currentY: %f; startX: %f; startY: %f; p: %f; q: %f", currentX, currentY, startX, startY, p, q));
            this.sleep(2);
        }
        StopAll();

    }

    public void DriveToPointHeading (float power, int endX, int endY) {

        power = Math.abs(power);
        RobotPosition startPosition = imageNavigation.getRobotPosition();
        Log.i("[Phoenix:startPosition]", "got position");

        if (startPosition == null)  {
            return;
        }

        float startX = startPosition.x;
        float startY = startPosition.y;
        imu.reset(Direction.NONE);
        float startAngle = imu.getAdjustedAngle();

        float x = endX - startX;
        float y = endY - startY;

        float distance = (float) Math.sqrt(x*x + y*y);

        float angle = (float) (180/Math.PI *  Math.atan(x/y));
        float heading = -angle;

        Log.i("[phoenix:DTPH]", String.format("x: %f; y: %f; distance: %f; angle: %f; heading: %f", x, y, distance, angle, heading));

        DriveHeading(power, distance, heading, 0.5f, Direction.BACKWARD);
    }

    public void TurnUntilImage(float power, Direction d, int angle) {

        imu.reset(d);

        if (d == Direction.COUNTERCLOCKWISE) {
            while (!imageNavigation.seesImage() && opModeIsActive() && imu.getAdjustedAngle() < angle) {
                fl.setPower(-power);
                bl.setPower(-power);
                fr.setPower(power);
                br.setPower(power);
            }
        } else {
            while (!imageNavigation.seesImage() && opModeIsActive() && imu.getAdjustedAngle() > angle) {
                fl.setPower(power);
                bl.setPower(power);
                fr.setPower(-power);
                br.setPower(-power);
            }
        }

        StopAll();

    }

    public void StrafeUntilHeading(float power, float multiplier, float heading, float distance, Direction d) {

        float x = (PPR * (2 * distance))/(diameter * (float)Math.PI);

        int targetEncoderValue = Math.round(x);
        float powerAdjustRatio = 1 + Math.abs(multiplier);

        float flp, frp, blp, brp;

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;

        if (d == Direction.LEFT) {
            while (currentPosition < targetEncoderValue && opModeIsActive()) {
                currentPosition = Math.abs(bl.getCurrentPosition());
                float currentHeading = imu.getAdjustedAngle();
                float angleDiff = Math.abs(currentPosition-heading);
                if (angleDiff > 20) {
                    powerAdjustRatio = 1+Math.abs(multiplier);
                } else if (angleDiff > 5) {
                    powerAdjustRatio = 1+Math.abs(multiplier*0.5f);
                } else {
                    powerAdjustRatio = 1+Math.abs(multiplier*0.1f);
                }

                flp = -power;
                frp = power;
                blp = power;
                brp = -power;

                if (currentHeading * heading < 0 && currentHeading > 0) {
                    heading += 360;
                } if (currentHeading * heading < 0  && currentHeading < 0) {
                    heading -= 360;
                }

                if (currentHeading < heading) {
                    blp /= powerAdjustRatio;
                    brp /= powerAdjustRatio;
                }
                else if (currentHeading > heading) {
                    flp /= powerAdjustRatio;
                    frp /= powerAdjustRatio;

                }

                setMaxPower(flp, frp, blp, brp);
                Log.i("[phoenix:strafeHeading]", String.format("currentHeading: %f, heading: %f, flp: %f, frp: %f, blp: %f, brp: %f", currentHeading, heading, flp, frp, blp, brp));
            }
        } else {
            while (currentPosition < targetEncoderValue && opModeIsActive()) {
                currentPosition = Math.abs(bl.getCurrentPosition());
                float currentHeading = imu.getAdjustedAngle();

                flp = power;
                frp = -power;
                blp = -power;
                brp = power;

                if (currentHeading * heading < 0 && currentHeading > 0) {
                    heading += 360;
                } if (currentHeading * heading < 0  && currentHeading < 0) {
                    heading -= 360;
                }

                if (currentHeading < heading) {
                    flp /= powerAdjustRatio;
                    frp /= powerAdjustRatio;
                }
                else if (currentHeading > heading) {
                    blp /= powerAdjustRatio;
                    brp /= powerAdjustRatio;
                }

                setMaxPower(flp, frp, blp, brp);
                Log.i("[phoenix:strafeHeading]", String.format("currentHeading: %f, heading: %f, flp: %f, frp: %f, blp: %f, brp: %f", currentHeading, heading, flp, frp, blp, brp));
            }
        }

        StopAll();
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }
}
