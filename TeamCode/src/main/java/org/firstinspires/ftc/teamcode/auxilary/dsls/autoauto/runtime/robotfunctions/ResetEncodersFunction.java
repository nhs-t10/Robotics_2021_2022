package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class ResetEncodersFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manager;

    public ResetEncodersFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No ManipulationManager; please define one in template.notjava");
        if(args.length == 0) {manager.resetEncoders(); return new AutoautoUndefined();}if(args.length == 1) {if(args[0] instanceof AutoautoString) {manager.resetEncoders(((AutoautoString)args[0]).getString()); return new AutoautoUndefined();}if(args[0] instanceof AutoautoNumericValue) {manager.resetEncoders((int)((AutoautoNumericValue)args[0]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No resetEncoders with 1 args");
    }
}