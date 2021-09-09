package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class DriveWithVerticalFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.movement.MovementManager manager;

    public DriveWithVerticalFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.movement.MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 2) {if(args[0] instanceof AutoautoNumericValue&&args[1] instanceof AutoautoNumericValue) {manager.driveWithVertical(((AutoautoNumericValue)args[0]).getFloat(),((AutoautoNumericValue)args[1]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No driveWithVertical with 2 args");
    }
}