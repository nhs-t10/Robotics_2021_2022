package org.firstinspires.ftc.teamcode.managers.feature.robotconfiguration;


import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class RobotConfiguration {
    public final static String fileName = "configuration";
    public final static String bigBoyFileContent = "bigBoy";
    public final static String littleBoyFileContent = "littleBoy";
    public final static String tankBoyFileContent = "tankBoy";

    public WheelCoefficients motorCoefficients;
    public WheelCoefficients autoMotorCoefficients;
    public WheelCoefficients teleOpMotorCoefficients;

    public OmniCalcComponents omniComponents;



    /**
     * How many "ticks" quantify a rotation of the motor.
     */
    public double encoderTicksPerRotation;
    /**
     * The gear ratio of the main drive motors.
     */
    public double gearRatio;
    /**
     * The diameter of the main drive wheels, in centimeters
     */
    public double wheelDiameterCm;
    /**
     * A coefficient indicating how much sliding we can expect of the wheels. 1 is perfect traction; 0 is no traction at all.
     */
    public double slip;
    /**
     * The circumference of the main drive wheels, in centimeters
     */
    public double wheelCircumference;


    public float exponentialScalar;


    public RobotConfiguration(WheelCoefficients teleOpMotorCoefficients, WheelCoefficients autoMotorCoefficients, OmniCalcComponents omniComponents, float pidPCoefficient, double encoderTicksPerRotation, double gearRatio, double wheelDiameterCm, double slip, float exponentialScalar) {
        this.motorCoefficients = teleOpMotorCoefficients;
        this.teleOpMotorCoefficients = teleOpMotorCoefficients;
        this.autoMotorCoefficients = autoMotorCoefficients;
        this.omniComponents = omniComponents;
        this.encoderTicksPerRotation = encoderTicksPerRotation;
        this.gearRatio = gearRatio;
        this.wheelDiameterCm = wheelDiameterCm;
        this.slip = slip;
        this.wheelCircumference = Math.PI * wheelDiameterCm;
        this.exponentialScalar = exponentialScalar;
    }

}