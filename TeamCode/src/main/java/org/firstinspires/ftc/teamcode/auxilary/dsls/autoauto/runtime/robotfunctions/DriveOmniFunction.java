package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class DriveOmniFunction extends NativeRobotFunction {
    public String name = "driveOmni";
    public int argCount = 1;
    public Class<?> declaringClass = MovementManager.class;

    private MovementManager manager;

    public DriveOmniFunction(FeatureManager manager) {
        this.manager = (MovementManager)manager;
    }

    public float[] call(float[][] args) {
        if(args.length > 1) manager.driveOmni(new float[] { args[0][0], args[1][0], args[2][0] });
        else manager.driveOmni(args[0]);
        return new float[0];
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 3) {
            manager.driveOmni(new float[] {
                    ((AutoautoNumericValue)args[0]).getFloat(),
                    ((AutoautoNumericValue)args[1]).getFloat(),
                    ((AutoautoNumericValue)args[2]).getFloat()
            });
        } else {
            AutoautoTable arr = (AutoautoTable)args[0];
            float[] floats = new float[arr.arrayLength()];
            for(int i = 0; i < floats.length; i++) {
                floats[i] = ((AutoautoNumericValue)arr.get(new AutoautoNumericValue(i))).getFloat();
            }
            manager.driveOmni(floats);
        }
        return new AutoautoUndefined();
    }
}