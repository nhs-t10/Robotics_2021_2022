package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetMsTransmissionIntervalFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager manager;

    public SetMsTransmissionIntervalFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 1) {if(args[0] instanceof AutoautoNumericValue) {manager.setMsTransmissionInterval((int)((AutoautoNumericValue)args[0]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No setMsTransmissionInterval with 1 args");
    }
}