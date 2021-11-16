package org.firstinspires.ftc.teamcode.managers.telemetry.fallible.hardwaredevices;

import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.FailureType;
import org.firstinspires.ftc.teamcode.managers.telemetry.fallible.HardwareCapability;

public interface FallibleHardwareDevice {
    void setFailureType(FailureType f);
    HardwareCapability[] getCapabilities();
}
