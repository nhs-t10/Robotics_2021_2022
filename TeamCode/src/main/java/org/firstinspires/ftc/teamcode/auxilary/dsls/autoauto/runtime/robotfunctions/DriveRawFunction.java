package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class DriveRawFunction extends NativeRobotFunction {
    public String name = "driveRaw";
    public int argCount = 4;
    public Class<?> declaringClass = MovementManager.class;

    private MovementManager manager;

    public DriveRawFunction(FeatureManager manager) {
        this.manager = (MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        manager.driveRaw(
            ((AutoautoNumericValue)args[0]).getFloat(),
            ((AutoautoNumericValue)args[1]).getFloat(),
            ((AutoautoNumericValue)args[2]).getFloat(),
            ((AutoautoNumericValue)args[3]).getFloat()
        );
        return new AutoautoUndefined();
    }
}