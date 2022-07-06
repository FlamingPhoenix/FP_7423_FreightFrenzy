package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Disabled
public class MainPipeline extends OpenCvPipeline {
    ShippingElementPosition position = ShippingElementPosition.LEFT;//assumed shipping element position
    public enum ShippingElementPosition
    {
        LEFT,
        CENTER,
        RIGHT
    }

    @Override
    public Mat processFrame(Mat input) {
        Scalar RED = new Scalar(0, 0, 255);
        Scalar GREEN = new Scalar(0, 0, 255);
        int avg_redLeft;
        int avg_redMiddle;
        int avg_redRight;
        int x1[] = {0, 0, 0};
        int y1[] = {0, 0, 0};
        int x2[] = {0, 0, 0};
        int y2[] = {0, 0, 0};

        Mat YCrCb = new Mat();
        Mat Cr = new Mat();

        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cr, 2);

        Mat region1_Cb, region2_Cb, region3_Cb;//3 regions to test for RED shipping element
        Point region1_pointA = new Point(
                x1[0],
                y1[0]);
        Point region1_pointB = new Point(
                x2[0],
                y2[0]);
        Point region2_pointA = new Point(
                x1[1],
                y1[1]);
        Point region2_pointB = new Point(
                x2[1],
                y2[1]);
        Point region3_pointA = new Point(
                x1[2],
                y1[2]);
        Point region3_pointB = new Point(
                x2[2],
                y2[2]);

        region1_Cb = Cr.submat(new Rect(region1_pointA, region1_pointB));
        region2_Cb = Cr.submat(new Rect(region2_pointA, region2_pointB));
        region3_Cb = Cr.submat(new Rect(region3_pointA, region3_pointB));
        /* (x1,y1) and (x2,y2) are 2 points in a coordinate plane (in this case with the units being pixels)
        they bound the areas the program looks at
        i made an array of these coordinates
        also this is done in the YCrCb color code cuz that is better cuz its not brightness dependent unlike RGB
        YCbCr looks at blueness and redness (and brightness)
        https://tvone.com/tech-support/faqs/120-ycrcb-values-for-various-colors godsend
         */
        avg_redLeft = (int) Core.mean(region1_Cb).val[0];
        avg_redMiddle = (int) Core.mean(region2_Cb).val[0];
        avg_redRight = (int) Core.mean(region3_Cb).val[0];


        //cosmetic boxes around areas
        Imgproc.rectangle(
                input, // Buffer to draw on
                region1_pointA, // First point which defines the rectangle
                region1_pointB, // Second point which defines the rectangle
                RED, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines
        Imgproc.rectangle(
                input, // Buffer to draw on
                region2_pointA, // First point which defines the rectangle
                region2_pointB, // Second point which defines the rectangle
                RED, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines
        Imgproc.rectangle(
                input, // Buffer to draw on
                region3_pointA, // First point which defines the rectangle
                region3_pointB, // Second point which defines the rectangle
                RED, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines

        int minOneTwo = Math.min(avg_redLeft, avg_redMiddle);
        int min = Math.min(minOneTwo, avg_redRight);

        if (min == avg_redLeft) {
            position = ShippingElementPosition.LEFT;
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

        } else if (min == avg_redMiddle) {
            position = ShippingElementPosition.CENTER;
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        } else if (min == avg_redRight) {
            position = ShippingElementPosition.RIGHT;
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region3_pointA, // First point which defines the rectangle
                    region3_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        }

        return input;
    }
}
