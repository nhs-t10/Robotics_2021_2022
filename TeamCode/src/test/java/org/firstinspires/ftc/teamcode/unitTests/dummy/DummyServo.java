package org.firstinspires.ftc.teamcode.unitTests.dummy;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class DummyServo implements Servo {
    @Override
    public ServoController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public void setPosition(double position) {

    }

    @Override
    public double getPosition() {
        return 0;
    }

    @Override
    public void scaleRange(double min, double max) {

    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
