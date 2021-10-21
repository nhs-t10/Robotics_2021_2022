package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetServoPowerFunction extends NativeRobotFunction {
    private org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manager;

    public SetServoPowerFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(manager == null) throw new ManagerSetupException("No .ManipulationManager; please define one in template.notjava");
        if(args.length == 2) {if(args[0] instanceof AutoautoString&&args[1] instanceof AutoautoNumericValue) {manager.setServoPower(((AutoautoString)args[0]).getString(),(double)((AutoautoNumericValue)args[1]).getFloat()); return new AutoautoUndefined();}if(args[0] instanceof AutoautoNumericValue&&args[1] instanceof AutoautoNumericValue) {manager.setServoPower((int)((AutoautoNumericValue)args[0]).getFloat(),(double)((AutoautoNumericValue)args[1]).getFloat()); return new AutoautoUndefined();}}throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No setServoPower with 2 args");
    }
}