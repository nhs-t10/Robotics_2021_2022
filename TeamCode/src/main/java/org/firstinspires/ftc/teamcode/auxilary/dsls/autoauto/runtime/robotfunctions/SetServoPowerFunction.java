package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;

public class SetServoPowerFunction extends NativeRobotFunction {
    public String name = "setServoPower";
    public int argCount = 2;
    public Class<?> declaringClass = ManipulationManager.class;

    private ManipulationManager manager;

    public SetServoPowerFunction(FeatureManager manager) {
        this.manager = (ManipulationManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        manager.setServoPower((int)((AutoautoNumericValue)args[0]).getFloat(), ((AutoautoNumericValue)args[1]).getFloat());
        return new AutoautoUndefined();
    }
}