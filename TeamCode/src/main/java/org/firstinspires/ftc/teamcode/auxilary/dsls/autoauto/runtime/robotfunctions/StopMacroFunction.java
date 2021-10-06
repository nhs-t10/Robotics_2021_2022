package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class StopMacroFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.macro.MacroManager manager;

    public StopMacroFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.macro.MacroManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) {manager.stopMacro(); return new AutoautoUndefined();}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No stopMacro with 0 args");
    }
}