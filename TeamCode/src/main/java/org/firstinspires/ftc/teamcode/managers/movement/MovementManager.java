package org.firstinspires.ftc.teamcode.managers.movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class MovementManager extends FeatureManager {

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    private ElapsedTime timer;

    private static float scale = 1.0f;

    /**
     * Create a MovementManager with four motors.
     * @param fl Front Left motor
     * @param fr Front Right motor
     * @param br Back Right motor
     * @param bl Back Left motor
     */
    public MovementManager(DcMotor fl, DcMotor fr, DcMotor br, DcMotor bl) {
        this.frontLeft = fl;
        this.frontRight = fr;
        this.backRight = br;
        this.backLeft = bl;

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public MovementManager(DcMotor fl, DcMotor fr, DcMotor br, DcMotor bl, ElapsedTime timer) {
        this.frontLeft = fl;
        this.frontRight = fr;
        this.backRight = br;
        this.backLeft = bl;
        this.timer = timer;

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void driveRaw(float fl, float fr, float br, float bl) {
        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backRight.setPower(br);
        backLeft.setPower(bl);

    }

    public void driveBlue(float fl, float fr, float br, float bl) {
        RobotConfiguration configuration = FeatureManager.getRobotConfiguration();
        driveRaw(fl * configuration.flCoef, fr * configuration.frCoef, br * configuration.brCoef, bl * configuration.blCoef);
    }

    public void stopDrive() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }

    public void driveOmni(float[] powers) {
        float[] sum = PaulMath.omniCalc(powers[0]*scale, powers[1]*scale, powers[2] * scale);
        driveRaw(sum[0], sum[1], sum[2], sum[3]);
    }
    public void driveOmni(float v, float h, float r) {
        driveOmni(new float[] {v, h, r});
    }

    public void driveOmniExponential(float[] powers) {
        float exponentialScalar = FeatureManager.getRobotConfiguration().exponentialScalar;
        float[] sum = PaulMath.omniCalc(
                (float) Math.pow(powers[0], exponentialScalar),
                (float) Math.pow(powers[1], exponentialScalar),
                (float) Math.pow(powers[2], exponentialScalar));
        driveRaw(sum[0], sum[1], sum[2], sum[3]);
    }
    public DcMotor[] getMotor(){
        DcMotor[] motors = {frontLeft, frontRight, backRight, backLeft};

        return motors;
    }


    public void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void runToPosition() {
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void runUsingEncoders() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void runWithOutEncoders() {
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setTargetPositions(int fl, int fr, int br, int bl) {
        frontLeft.setTargetPosition(fl);
        frontRight.setTargetPosition(fr);
        backRight.setTargetPosition(br);
        backLeft.setTargetPosition(bl);
    }

    public void driveVertical(float power, float distance) {
        int ticks = PaulMath.encoderDistanceCm(distance);
        setTargetPositions(ticks, ticks, ticks, ticks);
        runToPosition();
        while(
                Math.abs(frontLeft.getCurrentPosition()) < Math.abs(frontLeft.getTargetPosition()) &&
                        Math.abs(frontRight.getCurrentPosition()) < Math.abs(frontRight.getTargetPosition()) &&
                        Math.abs(backRight.getCurrentPosition()) < Math.abs(backRight.getTargetPosition()) &&
                        Math.abs(backLeft.getCurrentPosition()) < Math.abs(backLeft.getTargetPosition())
        ) {
            driveRaw(power, power, power, power);
            //Waiting for motor to finish
        }
        stopDrive();
    }
    public float getScale(){
        return scale;
    }
    public void upScale(float ScaleFactor){
        scale+=ScaleFactor;
    }
    public void downScale(float ScaleFactor){
        scale-=ScaleFactor;
    }

    public float[] getMotorPositions() {
        return new float[] {
          frontRight.getCurrentPosition(),
          frontLeft.getCurrentPosition(),
          backRight.getCurrentPosition(),
          backLeft.getCurrentPosition()
        };
    }

    public void driveWithVertical(float power, float distance) {
        int ticks = PaulMath.encoderDistanceCm(distance);
        setTargetPositions(ticks, ticks, ticks, ticks);
        runUsingEncoders();
        if(Math.abs(frontLeft.getCurrentPosition()) < Math.abs(frontLeft.getTargetPosition()) &&
                        Math.abs(frontRight.getCurrentPosition()) < Math.abs(frontRight.getTargetPosition()) &&
                        Math.abs(backRight.getCurrentPosition()) < Math.abs(backRight.getTargetPosition()) &&
                        Math.abs(backLeft.getCurrentPosition()) < Math.abs(backLeft.getTargetPosition())
        ) {
            driveRaw(power, power, power, power);
            //Waiting for motor to finish
        } else {
            stopDrive();
        }

    }

    public int getTicks() {
        return frontLeft.getCurrentPosition();
    }
    public int getHorizontalTicks() { return frontRight.getCurrentPosition(); }
    public int getVerticalTicks() { return  backLeft.getCurrentPosition(); }

    public float getMeters(){
        return (PaulMath.encoderDistanceCm(getTicks()) / 100f);
    }
    public float getHorizontalMeters(){
        return (PaulMath.encoderDistanceCm(getHorizontalTicks()) / 100f);
    }
    public float getVerticalMeters(){
        return (PaulMath.encoderDistanceCm(getVerticalTicks()) / 100f);
    }

}
