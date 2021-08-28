package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class SetTargetPositionsFunction extends NativeRobotFunction {
    public String name = "setTargetPositions";
    public int argCount = 4;
    public Class<?> declaringClass = org.firstinspires.ftc.teamcode.managers.MovementManager.class;

    private org.firstinspires.ftc.teamcode.managers.MovementManager manager;

    public SetTargetPositionsFunction(FeatureManager manager) {
        this.manager = (org.firstinspires.ftc.teamcode.managers.MovementManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        manager.setTargetPositions((int)((AutoautoNumericValue)args[0]).getFloat(), (int)((AutoautoNumericValue)args[1]).getFloat(), (int)((AutoautoNumericValue)args[2]).getFloat(),
                (int)((AutoautoNumericValue)args[3]).getFloat());
        return new AutoautoUndefined();
    }
}