package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetIsOpModeRunningFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.FeatureManager manager;

    public SetIsOpModeRunningFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.FeatureManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 1) {if(args[0] instanceof AutoautoBooleanValue) {manager.setIsOpModeRunning(((AutoautoBooleanValue)args[0]).getBoolean()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No setIsOpModeRunning with 1 args");
    }
}