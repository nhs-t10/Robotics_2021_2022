package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;

public class HighestValueFunction extends NativeRobotFunction {
    public String name = "highestValue";
    public int argCount = 1;
    public Class<?> declaringClass = org.firstinspires.ftc.teamcode.auxilary.PaulMath.class;

    private org.firstinspires.ftc.teamcode.auxilary.PaulMath manager;

    public HighestValueFunction() {
        
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        AutoautoTable arg = (AutoautoTable)args[0];
        float[] values = new float[arg.arrayLength()];

        for(int i = 0; i < values.length; i++) {
            values[i] = ((AutoautoNumericValue)arg.get(new AutoautoNumericValue(i))).getFloat();
        }
        return new AutoautoNumericValue(PaulMath.highestValue(values));
    }
}