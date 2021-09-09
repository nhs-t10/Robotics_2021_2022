package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetTargetPositionsFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.movement.MovementManager manager;

    public SetTargetPositionsFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.movement.MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 4) {if(args[0] instanceof AutoautoNumericValue&&args[1] instanceof AutoautoNumericValue&&args[2] instanceof AutoautoNumericValue&&args[3] instanceof AutoautoNumericValue) {manager.setTargetPositions((int)((AutoautoNumericValue)args[0]).getFloat(),(int)((AutoautoNumericValue)args[1]).getFloat(),(int)((AutoautoNumericValue)args[2]).getFloat(),(int)((AutoautoNumericValue)args[3]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No setTargetPositions with 4 args");
    }
}