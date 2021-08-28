package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class DriveOmniExponentialFunction extends NativeRobotFunction {
    public String name = "driveOmniExponential";
    public int argCount = 1;
    public Class<?> declaringClass = MovementManager.class;

    private MovementManager manager;

    public DriveOmniExponentialFunction(FeatureManager manager) {
        this.manager = (MovementManager)manager;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 3) {
            manager.driveOmniExponential(new float[] {
                    ((AutoautoNumericValue)args[0]).getFloat(),
                    ((AutoautoNumericValue)args[1]).getFloat(),
                    ((AutoautoNumericValue)args[2]).getFloat()
            });
        } else {
            AutoautoTable table = (AutoautoTable)args[0];
            float[] floats = new float[table.arrayLength()];
            for(int i = 0; i < floats.length; i++) {
                floats[i] = ((AutoautoNumericValue)table.get(new AutoautoNumericValue(i))).getFloat();
            }
            manager.driveOmniExponential(floats);
        }
        return new AutoautoUndefined();
    }
}