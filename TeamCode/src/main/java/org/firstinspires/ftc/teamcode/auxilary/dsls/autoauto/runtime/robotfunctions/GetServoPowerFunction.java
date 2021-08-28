package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;

public class GetServoPowerFunction extends NativeRobotFunction {
    public String name = "getServoPower";
    public int argCount = 1;
    public Class<?> declaringClass = ManipulationManager.class;

    private ManipulationManager manager;

    public GetServoPowerFunction(FeatureManager manager) {
        this.manager = (ManipulationManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        return new AutoautoNumericValue((float) manager.getServoPower((int)((AutoautoNumericValue)args[0]).getFloat()));
    }
}