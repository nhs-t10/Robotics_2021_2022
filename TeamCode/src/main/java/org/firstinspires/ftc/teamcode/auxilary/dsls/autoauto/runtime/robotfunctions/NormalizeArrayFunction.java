package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;

public class NormalizeArrayFunction extends NativeRobotFunction {
    public String name = "normalizeArray";
    public int argCount = 1;
    public Class<?> declaringClass = org.firstinspires.ftc.teamcode.auxilary.PaulMath.class;

    private org.firstinspires.ftc.teamcode.auxilary.PaulMath manager;

    public NormalizeArrayFunction() {
        
    }

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        AutoautoTable arg = (AutoautoTable)args[0];
        float[] values = new float[arg.arrayLength()];

        for(int i = 0; i < values.length; i++) {
            values[i] = ((AutoautoNumericValue)arg.get(new AutoautoNumericValue(i))).getFloat();
        }

        float[] result = PaulMath.normalizeArray(values);
        AutoautoValue[] resVals = new AutoautoValue[result.length];

        for(int i = 0; i < result.length; i++) {
            resVals[i] = new AutoautoNumericValue(result[i]);
        }
        return new AutoautoTable(resVals);
    }
}