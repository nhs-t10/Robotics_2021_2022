package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class DownScaleFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.movement.MovementManager manager;

    public DownScaleFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.movement.MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No MovementManager; please define one in template.notjava");
        if(args.length == 1) {if(args[0] instanceof AutoautoNumericValue) {manager.downScale(((AutoautoNumericValue)args[0]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No downScale with 1 args");
    }
}