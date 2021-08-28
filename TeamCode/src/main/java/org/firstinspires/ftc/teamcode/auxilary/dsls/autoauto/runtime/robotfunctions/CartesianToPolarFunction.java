package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;

public class CartesianToPolarFunction extends NativeRobotFunction {
    public String name = "cartesianToPolar";
    public int argCount = 2;
    public Class<?> declaringClass = org.firstinspires.ftc.teamcode.auxilary.PaulMath.class;

    public CartesianToPolarFunction() {
        
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        AutoautoTable coords = (AutoautoTable)args[0];

        AutoautoNumericValue coord1 = (AutoautoNumericValue)coords.get(new AutoautoNumericValue(0));
        AutoautoNumericValue coord2 = (AutoautoNumericValue)coords.get(new AutoautoNumericValue(0));

        float[] v = PaulMath.cartesianToPolar(coord1.getFloat(), coord2.getFloat());

        AutoautoValue[] res = new AutoautoValue[v.length];

        for(int i = 0; i < v.length; i++) {
            res[i] = new AutoautoNumericValue(v[i]);
        }

        return new AutoautoTable(res);
    }
}