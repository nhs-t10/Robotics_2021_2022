package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class GetThirdAngleOrientationFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.imu.ImuManager manager;

    public GetThirdAngleOrientationFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.imu.ImuManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No .ImuManager; please define one in template.notjava");
        if(args.length == 0) {return new AutoautoNumericValue(manager.getThirdAngleOrientation());}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getThirdAngleOrientation with 0 args");
    }
}