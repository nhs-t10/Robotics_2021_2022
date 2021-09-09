package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class GetKeyFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.input.InputManager manager;

    public GetKeyFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.input.InputManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 1) {if(args[0] instanceof AutoautoString) {return new AutoautoNumericValue(manager.getKey(((AutoautoString)args[0]).getString()));}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getKey with 1 args");
    }
}