package org.firstinspires.ftc.teamcode.managers.lynxcaching.cachedHardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class CachedDcMotor implements DcMotor, CachedHardwareDevice {
    private DcMotor motor;

    private ZeroPowerBehavior zeroPowerBehavior;
    private int targetPosition;
    private int currentPosition;
    private Direction direction;
    private double power;

    private boolean zeroPowerBehaviorChanged, targetPositionChanged, directionChanged, powerChanged;

    public CachedDcMotor(DcMotor motor) {
        this.motor = motor;
        reReadAndSetupStart();
    }

    private void reReadAndSetupStart() {
        this.zeroPowerBehavior = motor.getZeroPowerBehavior();
        this.targetPosition = motor.getTargetPosition();
        this.currentPosition = motor.getCurrentPosition();
        this.direction = motor.getDirection();
        this.power = motor.getPower();
    }

    @Override
    public void invalidateCacheAndReWrite() {
        if(zeroPowerBehaviorChanged) motor.setZeroPowerBehavior(zeroPowerBehavior);
        if(targetPositionChanged) motor.setTargetPosition(targetPosition);
        if(directionChanged) motor.setDirection(direction);
        if(powerChanged) motor.setPower(power);

        this.currentPosition = motor.getCurrentPosition();

        zeroPowerBehaviorChanged = false;
        targetPositionChanged = false;
        directionChanged = false;
        powerChanged = false;
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
    public void setZeroPowerBehavior(ZeroPowerBehavior newZeroPowerBehavior) {
        if(newZeroPowerBehavior != this.zeroPowerBehavior) zeroPowerBehaviorChanged = true;

        this.zeroPowerBehavior = newZeroPowerBehavior;
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return zeroPowerBehavior;
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
    public void setTargetPosition(int newPosition) {
        if(this.targetPosition != newPosition) targetPositionChanged = true;

        this.targetPosition = newPosition;
    }

    @Override
    public int getTargetPosition() {
        return targetPosition;
    }

    @Override
    public boolean isBusy() {
        return motor.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
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
    public void setDirection(Direction newDirection) {
        if(this.direction != newDirection) this.directionChanged = true;

        this.direction = newDirection;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public void setPower(double newPower) {
        if(this.power != newPower) this.powerChanged = true;

        this.power = newPower;
    }

    @Override
    public double getPower() {
        return power;
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
}
