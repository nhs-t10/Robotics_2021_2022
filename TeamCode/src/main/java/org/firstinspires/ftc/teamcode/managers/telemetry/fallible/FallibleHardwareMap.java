package org.firstinspires.ftc.teamcode.managers.telemetry.fallible;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices.FallibleCRServo;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices.FallibleDcMotor;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices.FallibleHardwareDevice;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices.FallibleIMU;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices.FallibleServo;

import java.util.HashMap;

public class FallibleHardwareMap extends HardwareMap {
    private HardwareMap internalHardwareMap;
    public HashMap<String, FallibleHardwareDevice> deviceHashMap;

    public FallibleHardwareMap(HardwareMap hardwareMap) {
        super(hardwareMap.appContext);
        this.deviceHashMap = new HashMap<>();
        this.internalHardwareMap = hardwareMap;
    }

    @Override
    public <T> T get(Class<? extends T> classOrInterface, String deviceName) {
        FallibleHardwareDevice fhd = getFallibleDevice(classOrInterface,deviceName);

        //if there's no fallible class for this hardware device, it'll be null. In that case, just go directly to the real map.
        if(fhd == null) return internalHardwareMap.get(classOrInterface, deviceName);

        deviceHashMap.put(deviceName,fhd);
        return (T)fhd;
    }

    private <T> FallibleHardwareDevice getFallibleDevice(Class<? extends T> h, String deviceName) {
        T actualDevice = internalHardwareMap.get(h, deviceName);
        if (DcMotor.class.isAssignableFrom(actualDevice.getClass())) return (new FallibleDcMotor((DcMotor) actualDevice));
        else if(CRServo.class.isAssignableFrom(actualDevice.getClass())) return (new FallibleCRServo((CRServo) actualDevice));
        else if(Servo.class.isAssignableFrom(actualDevice.getClass())) return (new FallibleServo((Servo) actualDevice));
        else if(BNO055IMU.class.isAssignableFrom(actualDevice.getClass())) return (new FallibleIMU((BNO055IMU) actualDevice));
        else return null;
    }
}
