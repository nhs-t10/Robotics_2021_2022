package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class DriveWithVerticalFunction extends NativeRobotFunction {
    public String name = "driveWithVertical";
    public int argCount = 2;
    public Class<?> declaringClass = MovementManager.class;

    private MovementManager manager;

    public DriveWithVerticalFunction(FeatureManager manager) {
        this.manager = (MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        manager.driveWithVertical(((AutoautoNumericValue)args[0]).getFloat(), ((AutoautoNumericValue)args[1]).getFloat());
        return new AutoautoUndefined();
    }
}