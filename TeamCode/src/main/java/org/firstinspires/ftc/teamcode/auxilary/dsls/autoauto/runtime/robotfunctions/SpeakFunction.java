package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SpeakFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager manager;

    public SpeakFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No .TelemetryManager; please define one in template.notjava");
        if(args.length == 1) {if(args[0] instanceof AutoautoString) {manager.speak(((AutoautoString)args[0]).getString()); return new AutoautoUndefined();}}if(args.length == 3) {if(args[0] instanceof AutoautoString&&args[1] instanceof AutoautoString&&args[2] instanceof AutoautoString) {manager.speak(((AutoautoString)args[0]).getString(),((AutoautoString)args[1]).getString(),((AutoautoString)args[2]).getString()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No speak with 3 args");
    }
}