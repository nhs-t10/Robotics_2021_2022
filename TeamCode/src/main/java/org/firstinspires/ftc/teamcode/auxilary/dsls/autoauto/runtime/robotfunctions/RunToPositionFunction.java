package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class RunToPositionFunction extends NativeRobotFunction {
    public String name = "runToPosition";
    public int argCount = 0;
    public Class<?> declaringClass = MovementManager.class;

    private MovementManager manager;

    public RunToPositionFunction(FeatureManager manager) {
        this.manager = (MovementManager)manager;
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        manager.runToPosition();
        return new AutoautoUndefined();
    }
}