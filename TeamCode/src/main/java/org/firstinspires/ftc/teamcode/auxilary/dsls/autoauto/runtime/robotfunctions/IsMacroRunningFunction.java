package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class IsMacroRunningFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.macro.MacroManager manager;

    public IsMacroRunningFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.macro.MacroManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No .MacroManager; please define one in template.notjava");
        if(args.length == 0) {return new AutoautoBooleanValue(manager.isMacroRunning());}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No isMacroRunning with 0 args");
    }
}