package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetServoPositionFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manager;

    public SetServoPositionFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 2) {if(args[0] instanceof AutoautoString&&args[1] instanceof AutoautoNumericValue) {manager.setServoPosition(((AutoautoString)args[0]).getString(),(double)((AutoautoNumericValue)args[1]).getFloat()); return new AutoautoUndefined();}if(args[0] instanceof AutoautoNumericValue&&args[1] instanceof AutoautoNumericValue) {manager.setServoPosition((int)((AutoautoNumericValue)args[0]).getFloat(),(double)((AutoautoNumericValue)args[1]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No setServoPosition with 2 args");
    }
}