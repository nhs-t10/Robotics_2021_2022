package org.firstinspires.ftc.teamcode.auxilary;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class EncodedMotor implements DcMotor {

    public static final float DEFAULT_TICKS_PER_ROT = 100f;

    private float ticksPerRotation;

    private DcMotor motor;

    private boolean runLoop;

    private double rawPower;

    private float lastMotorPosition;
    private long lastMotorPositionRecordingTime;

    private Thread updateLoop;

    public EncodedMotor(DcMotor _motor, float _ticksPerSecond) {
        this.motor = _motor;
        this.rawPower = motor.getPower();
        this.lastMotorPosition = motor.getCurrentPosition();
        this.lastMotorPositionRecordingTime = System.currentTimeMillis();
        this.ticksPerRotation = _ticksPerSecond;

        this.motor.setMode(RunMode.RUN_WITHOUT_ENCODER);

        this.runLoop = true;

        addListener();

        this.updateLoop = new Thread(new MotorUpdateLooper());
        updateLoop.start();
    }

    private void addListener() {
        FeatureManager.setIsOpModeRunning(true);
    }

    public EncodedMotor(DcMotor _motor) {
        this.motor = _motor;
        this.rawPower = motor.getPower();
        this.lastMotorPosition = motor.getCurrentPosition();
        this.lastMotorPositionRecordingTime = System.currentTimeMillis();
        this.ticksPerRotation = DEFAULT_TICKS_PER_ROT;

        this.motor.setMode(RunMode.RUN_WITHOUT_ENCODER);

        this.runLoop = true;

        addListener();

        this.updateLoop = new Thread(new MotorUpdateLooper());
        updateLoop.start();
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return motor.getMotorType();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        motor.setMotorType(motorType);
    }

    @Override
    public DcMotorController getController() {
        return motor.getController();
    }

    @Override
    public int getPortNumber() {
        return motor.getPortNumber();
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        motor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return motor.getZeroPowerBehavior();
    }

    @Override
    public void setPowerFloat() {
        motor.setPowerFloat();
    }

    @Override
    public boolean getPowerFloat() {
        return motor.getPowerFloat();
    }

    @Override
    public void setTargetPosition(int position) {
        motor.setTargetPosition(position);
    }

    @Override
    public int getTargetPosition() {
        return motor.getTargetPosition();
    }

    @Override
    public boolean isBusy() {
        return motor.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    @Override
    public void setMode(RunMode mode) {
        motor.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        return motor.getMode();
    }

    @Override
    public void setDirection(Direction direction) {
        motor.getDirection();
    }

    @Override
    public Direction getDirection() {
        return motor.getDirection();
    }

    @Override
    public void setPower(double power) {
        this.rawPower = power;
    }

    @Override
    public double getPower() {
        return motor.getPower();
    }

    @Override
    public Manufacturer getManufacturer() {
        return motor.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return motor.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return motor.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return motor.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        motor.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        motor.close();
    }

    private class MotorUpdateLooper implements Runnable {
        @Override
        public void run() {
            while(FeatureManager.isOpModeRunning) {
                int currentMotorPosition = motor.getCurrentPosition();
                long currentMotorPositionRecordingTime = System.currentTimeMillis();
                float velocityPerMillisecond = (currentMotorPosition - lastMotorPosition) / (currentMotorPositionRecordingTime - lastMotorPositionRecordingTime);

                float currentPercentageOfMaxVelocity = velocityPerMillisecond / (ticksPerRotation);

                float changePower = ((float)rawPower - currentPercentageOfMaxVelocity) * 1f;

                double oldPower = motor.getPower();
                double newPower = oldPower + changePower;

                motor.setPower(newPower);

                lastMotorPosition = currentMotorPosition;
                lastMotorPositionRecordingTime = currentMotorPositionRecordingTime;
            }
        }
    }
}
