package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class DriveBlueFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.movement.MovementManager manager;

    public DriveBlueFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.movement.MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No MovementManager; please define one in template.notjava");
        if(args.length == 4) {if(args[0] instanceof AutoautoNumericValue&&args[1] instanceof AutoautoNumericValue&&args[2] instanceof AutoautoNumericValue&&args[3] instanceof AutoautoNumericValue) {manager.driveBlue(((AutoautoNumericValue)args[0]).getFloat(),((AutoautoNumericValue)args[1]).getFloat(),((AutoautoNumericValue)args[2]).getFloat(),((AutoautoNumericValue)args[3]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No driveBlue with 4 args");
    }
}