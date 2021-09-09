package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class GetTicksFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.movement.MovementManager manager;

    public GetTicksFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.movement.MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) {return new AutoautoNumericValue(manager.getTicks());}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getTicks with 0 args");
    }
}