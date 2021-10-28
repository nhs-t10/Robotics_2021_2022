package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class RunMacroFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.macro.MacroManager manager;

    public RunMacroFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.macro.MacroManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No MacroManager; please define one in template.notjava");
        if(args.length == 1) {if(args[0] instanceof AutoautoString) {manager.runMacro(((AutoautoString)args[0]).getString()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No runMacro with 1 args");
    }
}