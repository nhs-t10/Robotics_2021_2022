package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class IsSpecialFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.sensor.SensorManager manager;

    public IsSpecialFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.sensor.SensorManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No SensorManager; please define one in template.notjava");
        if(args.length == 1) {if(args[0] instanceof AutoautoNumericValue) {return new AutoautoBooleanValue(manager.isSpecial((int)((AutoautoNumericValue)args[0]).getFloat()));}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No isSpecial with 1 args");
    }
}