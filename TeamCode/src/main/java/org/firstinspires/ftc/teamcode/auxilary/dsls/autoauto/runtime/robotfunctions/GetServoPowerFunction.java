package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class GetServoPowerFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manager;

    public GetServoPowerFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 1) {if(args[0] instanceof AutoautoString) {return new AutoautoNumericValue(manager.getServoPower(((AutoautoString)args[0]).getString()));}if(args[0] instanceof AutoautoNumericValue) {return new AutoautoNumericValue(manager.getServoPower((int)((AutoautoNumericValue)args[0]).getFloat()));}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getServoPower with 1 args");
    }
}