package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name="RunCamera", group = "none")
public class RunCamera extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        OpenCvWebcam webcam;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "specialCamera"), cameraMonitorViewId);

        ConceptCV myPipeline = new ConceptCV();
        webcam.setPipeline(myPipeline);

        OpenCvWebcam finalWebcam = webcam;
        finalWebcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                finalWebcam.startStreaming(1920 ,1080, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("Initialization passed ", 1);
                telemetry.update();
            }
            @Override
            public void onError(int errorCode) {
                telemetry.addData("Init Failed ", errorCode);
                telemetry.update();
            }
        });

        //replaces init-loop

        while (!isStarted() && !isStopRequested())
        {
            if (myPipeline.position == ConceptCV.ShippingElementPosition.LEFT)
                telemetry.addData("Realtime analysis: Left ", 0);
            if (myPipeline.position == ConceptCV.ShippingElementPosition.CENTER)
                telemetry.addData("Realtime analysis: Center ", 0);
            if (myPipeline.position == ConceptCV.ShippingElementPosition.RIGHT)
                telemetry.addData("Realtime analysis: Right ", 0);
            telemetry.update();
            sleep(50);
        }





    }
}