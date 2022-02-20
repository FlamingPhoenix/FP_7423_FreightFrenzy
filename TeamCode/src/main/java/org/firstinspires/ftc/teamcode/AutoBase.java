package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class AutoBase extends LinearOpMode {

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

    float pos = 0.5f;
    float vposR = 0.69f;
    float vposL = 0.7f;
    float fpos = 0.2f;

    public Servo pivotLeft;
    public Servo pivotRight;

    public float PPR = 537.7f; //537.7 for actual robot; 1120 for programming bot

    public float maxEncoderPulley = 420f;

    public float diameter = 4;

    public MyIMU imu;

    public ImageNavigation imageNavigation;

    public float startHeading;

    public int currentStage, currentPosition;

    public void initialize (){
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

        imu = new MyIMU(hardwareMap);
        BNO055IMU.Parameters p = new BNO055IMU.Parameters();
        imu.initialize(p);

        imageNavigation = new ImageNavigation(hardwareMap, this);
        imageNavigation.init();

        startHeading = imu.getAdjustedAngle();

        telemetry.addData("Ready for start %f", 0);
        telemetry.update();
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
        int targetTime = 3500;

        carousel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        carousel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        int currentPosition = 0;
        int currentTime = 0;

        carousel.setPower(-power);
        fr.setPower(-0.1);
        fl.setPower(-0.1);
        br.setPower(-0.1);
        bl.setPower(-0.1);

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

    public void MovePulley (float power, int stage) {

        // stage can be 0, 1, 2 depending on the shipping hub level

        int targetEncoderValue = 0;

        if (stage == 1) {
            targetEncoderValue = 200;
        } else if (stage == 2) {
            targetEncoderValue = 340;
        }

        pulley.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (currentStage > stage) {
            while (currentPosition > targetEncoderValue && opModeIsActive()) {
                vposR = 0.70f;
                vbarRight.setPosition(vposR);
                sleep(1000);
                currentPosition = pulley.getCurrentPosition();
                Globals.pulleyEncoder = currentPosition;
                pulley.setPower(-power);
                pulley2.setPower(-power);
                Log.i("[pheonix:pulleyInfo]", String.format("currentPulley = %d", currentPosition));
            }
        } else if (currentStage < stage) {
            while (currentPosition < targetEncoderValue && opModeIsActive()) {
                currentPosition = pulley.getCurrentPosition();
                Globals.pulleyEncoder = currentPosition;
                pulley.setPower(power);
                pulley2.setPower(power);
                Log.i("[pheonix:pulleyInfo]", String.format("currentPulley = %d", currentPosition));
            }
            vposR = 0.2f;
            vbarRight.setPosition(vposR);
        }

        float backgroundPower = 0;

        currentStage = stage;

        // w/o lift attachment
        if (currentStage == 1) {
            backgroundPower = 0.1f;
        } else if (currentStage == 2) {
            backgroundPower = 0.1f;
        }

        pulley.setPower(backgroundPower);
        pulley2.setPower(backgroundPower);

    }

    public void OnStart() {
        Drive(0.5f, 6, Direction.BACKWARD);

        finger.setPosition(0.3f);
        vbarRight.setPosition(0.78f);
        while (pulley.getCurrentPosition() < 85) {
            pulley.setPower(0.8f);
            pulley2.setPower(0.8f);
        }
        pulley.setPower(0);
        pulley2.setPower(0);
        intakeRight.setPosition(0.71f);
        sweeper.setPower(0.7f);
        sleep(1000);
        while (pulley.getCurrentPosition() > 0) {
            pulley.setPower(-0.5f);
            pulley2.setPower(-0.5f);
        }
        pulley.setPower(0);
        pulley2.setPower(0);
        sweeper.setPower(0);
        vbarRight.setPosition(0.79f);
        sleep(100);
        finger.setPosition(0.60);
    }

    public void Intake(float power){
        double intakeTime;
        double clampTime;

        int targetTime = 3000;

        sweeper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sweeper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        sweeper.setPower(power);
//        fr.setPower(-0.1);
//        fl.setPower(-0.1);
//        br.setPower(-0.1);
//        bl.setPower(-0.1);

        Shake(0.5f, 10);

//        sleep(targetTime);
        sweeper.setPower(0);

        DriveHeading(0.5f, 40, startHeading - 90, 0.3f, Direction.FORWARD);//can maybe seperate into two different functions here

        intakeRight.setPosition(0.4);
//        vbarRight.setPosition(0.69);
        finger.setPosition(0.1);
        sweeper.setPower(0.8);

        sleep(2000);

        sweeper.setPower(0);
        intakeRight.setPosition(0.7);

        sleep(1000);

        vbarRight.setPosition(0.72);
        finger.setPosition(0.5);
    }
    public void Shake(float power, int shakes){
        int neg = 1;
        for (int i = 0; i < shakes; i++){
            fl.setPower(power * neg);
            bl.setPower(power * neg);
            fr.setPower(-power * neg);
            br.setPower(-power * neg);

            sleep(100);
            neg *= -1;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }

//    public void copyToTele() {
//        Tele.autoCurrentPosition = currentPosition;
//        Tele.autoCurrentStage = currentStage;
//    }
}
