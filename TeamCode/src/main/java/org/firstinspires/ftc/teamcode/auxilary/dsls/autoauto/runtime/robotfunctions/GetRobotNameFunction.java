package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class GetRobotNameFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.feature.FeatureManager manager;

    public GetRobotNameFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.feature.FeatureManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No FeatureManager; please define one in template.notjava");
        if(args.length == 0) {return new AutoautoString(manager.getRobotName());}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No getRobotName with 0 args");
    }
}