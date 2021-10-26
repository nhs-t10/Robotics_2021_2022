package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetAutoClearFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager manager;

    public SetAutoClearFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No TelemetryManager; please define one in template.notjava");
        if(args.length == 1) {if(args[0] instanceof AutoautoBooleanValue) {manager.setAutoClear(((AutoautoBooleanValue)args[0]).getBoolean()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No setAutoClear with 1 args");
    }
}