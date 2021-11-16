package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class PositionTwoFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.nate.NateManager manager;

    public PositionTwoFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.nate.NateManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No NateManager; please define one in template.notjava");
        if(args.length == 0) {manager.positionTwo(); return new AutoautoUndefined();}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No positionTwo with 0 args");
    }
}