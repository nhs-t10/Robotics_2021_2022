package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class GetMotorPowerFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manager;

    public GetMotorPowerFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No .ManipulationManager; please define one in template.notjava");
        if(args.length == 1) {if(args[0] instanceof AutoautoString) {return new AutoautoNumericValue(manager.getMotorPower(((AutoautoString)args[0]).getString()));}if(args[0] instanceof AutoautoNumericValue) {return new AutoautoNumericValue(manager.getMotorPower((int)((AutoautoNumericValue)args[0]).getFloat()));}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getMotorPower with 1 args");
    }
}