package org.firstinspires.ftc.teamcode.managers.sensor;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;

public class ColorSensor extends Sensor {
    /** The colorSensor field will contain a reference to our color sensor hardware object */
    NormalizedColorSensor colorSensor;
    boolean weShouldRead, weveInitiated;
    int colorReturned;
    Thread updateLoopThread;
    float[] hsvValues = new float[3];
    public int runCount = 0;

    public ColorSensor(NormalizedColorSensor sensor) {
        //   this.hardwareMap = _hardwareMap; //since we don't get the hardwaremap by default-- this isn't an OpMode-- we have to set it manually
        this.weveInitiated = true; //We have initiated the code
        this.colorReturned = 0;
        this.weShouldRead = true;
        this.colorSensor = sensor;
    }
    public ColorSensor(HardwareMap _hardwareMap) {
        this.weveInitiated = true; //We have initiated the code
        this.colorReturned = 0;
        this.weShouldRead = false;
        this.colorSensor = _hardwareMap.get(NormalizedColorSensor.class, "sensor"); // set the colorSensor to the actual hardware color sensor
    }

    public void startAsyncLoop() {
        updateLoopThread = new Thread(new UpdateLoopThread());
        updateLoopThread.start();
    }

    //Switch the sample loop on/off
    public void switchSampling(boolean start_or_dont) {
        //save a boolean for the loop's state before changes
        boolean weShouldReadold = this.weShouldRead;
        this.weShouldRead = start_or_dont;
        //if it's switching on from being previously off, start the loop again
    }

    //return our color integer
    public int getColorInt () {
        return this.colorReturned;
    }

    public String getHexCode() {
        return Color.red(this.colorReturned) + " | "+ Color.green(this.colorReturned) + " | " + Color.blue(this.colorReturned) + " | " +Color.alpha(this.colorReturned);
    }

    public float[] getHsv() {
        return this.hsvValues;
    }



    public boolean getReadState() {

        return this.weShouldRead;
       // Color.alpha(this.colorReturned);
    }
    public int getAlpha(){
       return Color.alpha(this.colorReturned);
    }

    public boolean isSpecial() {
        return Color.alpha(this.colorReturned) >= 200;

    }

    @Override
    public float getNumberValue() {
        return 0;
    }

    @Override
    public float[] getFloatArray() {
        return new float[0];
    }

    @Override
    public boolean getBool() {
        return false;
    }

    //Test if we're seeing gold
    public boolean isSpecial2() {
        return PaulMath.delta(46 / 360f, hsvValues[0]) < 0.075f && PaulMath.delta(0.27f, hsvValues[1]) < 0.075f && PaulMath.delta(0.23f, hsvValues[2]) < 0.075f;
    }

    @Override
    public void update() {
        this.runSample();
    }

    public void runSample() {

        this.runCount++;


        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (this.colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)this.colorSensor).enableLight(true);
        }

        // Loop until we are asked to stop
        // Read the sensor
        NormalizedRGBA colors = this.colorSensor.getNormalizedColors();

        //Convert the color to HSV
        Color.colorToHSV(colors.toColor(), this.hsvValues);

        //normalize the colors-- make it so brightness won't affect our readout (much)
        float max = Math.max(Math.max(colors.red, colors.green), Math.max(colors.blue, colors.alpha));
        colors.red /= max;
        colors.green /= max;
        colors.blue /= max;


        //set the colorReturned variable so it can be used by the other methods
        this.colorReturned = colors.toColor();
    }

    public NormalizedRGBA getNormalizedColors() {
        return colorSensor.getNormalizedColors();
    }

    public class UpdateLoopThread implements Runnable {
        @Override
        public void run() {
            while(weShouldRead) {
                runSample();
            }
        }


    }
}
