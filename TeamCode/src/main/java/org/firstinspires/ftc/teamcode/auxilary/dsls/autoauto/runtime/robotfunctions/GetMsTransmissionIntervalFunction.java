package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class GetMsTransmissionIntervalFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager manager;

    public GetMsTransmissionIntervalFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) {return new AutoautoNumericValue(manager.getMsTransmissionInterval());}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getMsTransmissionInterval with 0 args");
    }
}