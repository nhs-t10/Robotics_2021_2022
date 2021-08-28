package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;

public class ResetMotorEncoderFunction extends NativeRobotFunction {
    public String name = "resetEncoders";
    public int argCount = 1;
    public Class<?> declaringClass = ManipulationManager.class;

    private ManipulationManager manager;

    public ResetMotorEncoderFunction(FeatureManager manager) {
        this.manager = (ManipulationManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        manager.resetEncoders((int) ( ((AutoautoNumericValue)args[0]).getFloat() ));
        return new AutoautoUndefined();
    }
}