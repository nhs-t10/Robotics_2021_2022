package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class ComputerVisionFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.sensor.SensorManager manager;

    public ComputerVisionFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.sensor.SensorManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No SensorManager; please define one in template.notjava");
        if(args.length == 0) {return new AutoautoNumericValue(manager.computerVision());}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No computerVision with 0 args");
    }
}