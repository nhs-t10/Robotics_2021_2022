package org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FailureType;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.HardwareCapability;

public class FallibleCRServo implements CRServo, FallibleHardwareDevice {
    private final CRServo servo;
    private FailureType failureType;

    public FallibleCRServo(CRServo servo) {
        this.servo = servo;
        this.failureType = FailureType.NOT_FAILING;
    }

    @Override
    public ServoController getController() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return servo.getController();
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getController();
        else return servo.getController();
    }

    @Override
    public int getPortNumber() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0;
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getPortNumber();
        else return servo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) ;
        else servo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return Direction.REVERSE;
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getDirection();
        else return servo.getDirection();
    }

    @Override
    public void setPower(double power) {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) return;
        else servo.setPower(power);
    }

    @Override
    public double getPower() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0.0;
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getPower();
        else return servo.getPower();
    }

    @Override
    public Manufacturer getManufacturer() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return Manufacturer.Other;
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getManufacturer();
        else return servo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return "";
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getDeviceName();
        else return servo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return "";
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getConnectionInfo();
        else return servo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return 0;
        else if(failureType == FailureType.CONTROL_FAILURE) return servo.getVersion();
        else return servo.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) ;
        else servo.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        if(failureType == FailureType.INTERMITTENT_FAILURES && Math.random() < 0.5) return;
        else if(failureType == FailureType.CONTROL_FAILURE) ;
        else servo.close();
    }

    @Override
    public void setFailureType(FailureType f) {
        this.failureType = f;
    }

    @Override
    public HardwareCapability[] getCapabilities() {
        return new HardwareCapability[] { HardwareCapability.SENSOR, HardwareCapability.MOTOR, HardwareCapability.MOTOR_PRECISE};
    }
}
