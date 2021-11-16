package org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FailureType;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.HardwareCapability;

public class FallibleDcMotor implements DcMotor, FallibleHardwareDevice {


    private final DcMotor motor;
    private FailureType failureType;

    public FallibleDcMotor(DcMotor motor) {
        this.motor = motor;
        this.failureType = FailureType.NOT_FAILING;
    }

    @Override
    public MotorConfigurationType getMotorType() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return MotorConfigurationType.getUnspecifiedMotorType();
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getMotorType();
        else return motor.getMotorType();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) return;
        else motor.setMotorType(motorType);
    }

    @Override
    public DcMotorController getController() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return motor.getController();
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getController();
        else return motor.getController();
    }

    @Override
    public int getPortNumber() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getPortNumber();
        else return motor.getPortNumber();
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) return;
        else motor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return motor.getZeroPowerBehavior();
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getZeroPowerBehavior();
        else return motor.getZeroPowerBehavior();
    }

    @Override
    public void setPowerFloat() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) return;
        else motor.setPowerFloat();
    }

    @Override
    public boolean getPowerFloat() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return false;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getPowerFloat();
        else return motor.getPowerFloat();
    }

    @Override
    public void setTargetPosition(int position) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) return;
        else motor.setTargetPosition(position);
    }

    @Override
    public int getTargetPosition() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getTargetPosition();
        else return motor.getTargetPosition();
    }

    @Override
    public boolean isBusy() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return false;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.isBusy();
        else return motor.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0;
        else if(failureType == FailureType.CONTROL_FAILURE) return 0;
        else return motor.getCurrentPosition();
    }

    @Override
    public void setMode(RunMode mode) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) return;
        else motor.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return RunMode.RUN_WITHOUT_ENCODER;
        else if(failureType == FailureType.CONTROL_FAILURE) return RunMode.RUN_WITHOUT_ENCODER;
        else return motor.getMode();
    }

    @Override
    public void setDirection(Direction direction) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) ;
        else motor.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return Direction.REVERSE;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getDirection();
        else return motor.getDirection();
    }

    @Override
    public void setPower(double power) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) return;
        else motor.setPower(power);
    }

    @Override
    public double getPower() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0.0;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getPower();
        else return motor.getPower();
    }

    @Override
    public Manufacturer getManufacturer() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return Manufacturer.Other;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getManufacturer();
        else return motor.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return "";
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getDeviceName();
        else return motor.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return "";
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getConnectionInfo();
        else return motor.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0;
        else if(failureType == FailureType.CONTROL_FAILURE) return motor.getVersion();
        else return motor.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) ;
        else motor.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) ;
        else motor.close();
    }

    @Override
    public void setFailureType(FailureType f) {
        this.failureType = FailureType.NOT_FAILING;
    }

    @Override
    public HardwareCapability[] getCapabilities() {
        return new HardwareCapability[] {HardwareCapability.MOTOR};
    }
}
