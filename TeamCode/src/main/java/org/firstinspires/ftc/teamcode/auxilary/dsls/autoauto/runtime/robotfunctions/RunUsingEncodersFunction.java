package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class RunUsingEncodersFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manager;

    public RunUsingEncodersFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) {manager.runUsingEncoders(); return new AutoautoUndefined();}if(args.length == 1) {if(args[0] instanceof AutoautoString) {manager.runUsingEncoders(((AutoautoString)args[0]).getString()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No runUsingEncoders with 1 args");
    }
}