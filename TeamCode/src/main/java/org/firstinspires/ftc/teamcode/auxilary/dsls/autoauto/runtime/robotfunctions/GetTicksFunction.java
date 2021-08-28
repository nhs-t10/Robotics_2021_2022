package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class GetTicksFunction extends NativeRobotFunction {
    public String name = "getTicks";
    public int argCount = 1;
    public Class<?> declaringClass = MovementManager.class;

    private MovementManager manager;

    public GetTicksFunction(FeatureManager manager) {
        this.manager = (MovementManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        return new AutoautoNumericValue(manager.getTicks());
    }
}
