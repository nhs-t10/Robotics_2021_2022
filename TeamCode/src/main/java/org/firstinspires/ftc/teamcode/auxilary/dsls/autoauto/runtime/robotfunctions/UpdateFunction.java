package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class UpdateFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.input.InputManager manager;

    public UpdateFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.input.InputManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) {manager.update(); return new AutoautoUndefined();}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No update with 0 args");
    }
}