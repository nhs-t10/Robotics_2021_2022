package org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FailureType;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.HardwareCapability;

public class FallibleIMU implements BNO055IMU, FallibleHardwareDevice {
    public BNO055IMU imu;
    private FailureType failureType;

    public FallibleIMU(BNO055IMU imu) {
        this.imu = imu;
        this.failureType = FailureType.NOT_FAILING;
    }
//TODO: Make this Fallible
    @Override
    public boolean initialize(@NonNull Parameters parameters) { return imu.initialize(parameters); }

    @NonNull
    @Override
    public Parameters getParameters() { return imu.getParameters(); }

    @Override
    public void close() { imu.close(); }

    @Override
    public Orientation getAngularOrientation() { return imu.getAngularOrientation(); }

    @Override
    public Orientation getAngularOrientation(AxesReference reference, AxesOrder order, org.firstinspires.ftc.robotcore.external.navigation.AngleUnit angleUnit) { return imu.getAngularOrientation( reference, order, angleUnit); }

    @Override
    public Acceleration getOverallAcceleration() { return imu.getOverallAcceleration(); }

    @Override
    public AngularVelocity getAngularVelocity() { return imu.getAngularVelocity(); }

    @Override
    public Acceleration getLinearAcceleration() { return imu.getLinearAcceleration(); }

    @Override
    public Acceleration getGravity() {  return imu.getGravity(); }

    @Override
    public Temperature getTemperature() { return imu.getTemperature(); }

    @Override
    public MagneticFlux getMagneticFieldStrength() { return imu.getMagneticFieldStrength(); }

    @Override
    public Quaternion getQuaternionOrientation() { return imu.getQuaternionOrientation(); }

    @Override
    public Position getPosition() { return imu.getPosition(); }

    @Override
    public Velocity getVelocity() { return imu.getVelocity(); }

    @Override
    public Acceleration getAcceleration() { return imu.getAcceleration(); }

    @Override
    public void startAccelerationIntegration(Position initialPosition, Velocity initialVelocity, int msPollInterval) { imu.startAccelerationIntegration( initialPosition, initialVelocity, msPollInterval); }

    @Override
    public void stopAccelerationIntegration() { imu.stopAccelerationIntegration(); }

    @Override
    public SystemStatus getSystemStatus() { return imu.getSystemStatus(); }

    @Override
    public SystemError getSystemError() { return imu.getSystemError(); }

    @Override
    public CalibrationStatus getCalibrationStatus() { return imu.getCalibrationStatus(); }

    @Override
    public boolean isSystemCalibrated() { return imu.isSystemCalibrated(); }

    @Override
    public boolean isGyroCalibrated() { return imu.isGyroCalibrated(); }

    @Override
    public boolean isAccelerometerCalibrated() { return imu.isAccelerometerCalibrated(); }

    @Override
    public boolean isMagnetometerCalibrated() { return imu.isMagnetometerCalibrated(); }

    @Override
    public CalibrationData readCalibrationData() { return imu.readCalibrationData(); }

    @Override
    public void writeCalibrationData(CalibrationData data) { imu.writeCalibrationData( data); }

    @Override
    public byte read8(Register register) { return imu.read8( register); }

    @Override
    public byte[] read(Register register, int cb) { return imu.read( register, cb); }

    @Override
    public void write8(Register register, int bVal) { imu.write8( register, bVal); }

    @Override
    public void write(Register register, byte[] data) { imu.write( register, data); }

    @Override
    public void setFailureType(FailureType f) {
        this.failureType = f;
    }

    @Override
    public HardwareCapability[] getCapabilities() {
        return new HardwareCapability[] {HardwareCapability.MOTOR};
    }
}
