package org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FailureType;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.HardwareCapability;

public class FallibleServo implements Servo, FallibleHardwareDevice {
    private final Servo servo;
    private FailureType failureType;

    public FallibleServo(Servo servo) {
        this.servo = servo;
        this.failureType = FailureType.NOT_FAILING;
    }

    @Override
    public ServoController getController() {
        return servo.getController();
    }

    @Override
    public int getPortNumber() {
        return servo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        servo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return servo.getDirection();
    }

    @Override
    public void setPosition(double position) {
        servo.setPosition(position);
    }

    @Override
    public double getPosition() {
        return servo.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {
        servo.scaleRange(min, max);
    }

    @Override
    public Manufacturer getManufacturer() {
        return servo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return servo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return servo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return servo.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        servo.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        servo.close();
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
