package org.firstinspires.ftc.teamcode.managers.CV;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
public class FancyPantsEdgeDetectionPipeline extends PipelineThatExposesSomeAnalysis {


    /*
     * An enum to define the skystone position
     */
    public enum SkystonePosition
    {
        LEFT,
        CENTER,
        RIGHT
    }

    /*
     * Some color constants
     */
    static final Scalar YCRCB_RED = new Scalar(255, 255, 0);
    static final Scalar YCRCB_ORIGIN = new Scalar(0, 128, 128);


    // Working variables. Because of memory concerns, we're not allowed to make ANY non-primitive variables within the `processFrame` method.

    Mat YCrCb = new Mat(), redPixels = new Mat(), edges = new Mat(), hierarchy = new Mat();
    MatOfPoint biggestContour;

    ArrayList<MatOfPoint> contours;

    Rect biggestContourBoundingRect;

    int largeBlobCenterX, inputWidth;

    // Volatile since accessed by OpMode thread w/o synchronization
    private volatile SkystonePosition position = SkystonePosition.LEFT;

    @Override
    public void init(Mat firstFrame) {

    }

    @Override
    public Mat processFrame(Mat input)
    {
        //convert the input to YCrCb, which is better for analysis than the default bgr.
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);

        //filter the image to ONLY redish pixels
        Core.inRange(YCrCb, YCRCB_ORIGIN, YCRCB_RED, redPixels);

        //perform edge detection to find the big blobs of reddish pixels. `threshold1` and `threshold2` should be determined through experimentation.
        Imgproc.Canny(redPixels, edges, 100, 300);

        //sometimes, Canny doesn't completely connect the blobs, so fill in by finding all "contours".
        //It likes using a list of Matrices of points instead of something more simple, but that's ok.
        contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        //only bother continuing if there were any contours found
        if(contours.size() == 0) return input;

        double biggestArea = -1;
        //look through the list and find the biggest contour
        for(MatOfPoint contour : contours) {
            double area = Imgproc.boundingRect(contour).area();
            if(area > biggestArea) {
                biggestArea = area;
                biggestContour = contour;
            }
        }

        biggestContourBoundingRect = Imgproc.boundingRect(biggestContour);

        largeBlobCenterX = biggestContourBoundingRect.x + (biggestContourBoundingRect.width / 2);
        inputWidth = input.width();

        //depending on which third the blob's center falls into, report the result position.
        if(largeBlobCenterX < inputWidth / 3) {
            position = SkystonePosition.LEFT;
        } else if(largeBlobCenterX < (inputWidth * 2) / 3) {
            position = SkystonePosition.CENTER;
        } else {
            position = SkystonePosition.RIGHT;
        }

        return input;
    }
    public int getAnalysis() {
        return position.ordinal();
    }
    public double getAnalysisPrecise() {
        //avoid divide-by-0 error
        if(inputWidth == 0) return 0;

        return largeBlobCenterX / (double)inputWidth;
    }
}
