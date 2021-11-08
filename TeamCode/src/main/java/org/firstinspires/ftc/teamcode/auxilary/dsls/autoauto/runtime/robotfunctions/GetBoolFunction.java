package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class GetBoolFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.input.InputManager manager;

    public GetBoolFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.input.InputManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No InputManager; please define one in template.notjava");
        if(args.length == 1) {if(args[0] instanceof AutoautoString) {return new AutoautoBooleanValue(manager.getBool(((AutoautoString)args[0]).getString()));}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getBool with 1 args");
    }
}