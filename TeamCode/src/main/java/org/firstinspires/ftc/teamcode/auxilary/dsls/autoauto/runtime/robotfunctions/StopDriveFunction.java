package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class StopDriveFunction extends NativeRobotFunction {
    public String name = "stopDrive";
    public int argCount = 0;
    public Class<?> declaringClass = MovementManager.class;

    private MovementManager manager;

    public StopDriveFunction(FeatureManager manager) {
        this.manager = (MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        manager.stopDrive();
        return new AutoautoUndefined();
    }
}