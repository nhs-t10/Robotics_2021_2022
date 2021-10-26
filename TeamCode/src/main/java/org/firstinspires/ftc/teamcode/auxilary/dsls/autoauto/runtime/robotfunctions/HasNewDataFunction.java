package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class HasNewDataFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager manager;

    public HasNewDataFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No TelemetryManager; please define one in template.notjava");
        if(args.length == 0) {return new AutoautoBooleanValue(manager.hasNewData());}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No hasNewData with 0 args");
    }
}