package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeRobotFunction;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;

public class ProportionalPIDFunction extends NativeRobotFunction {
    public String name = "proportionalPID";
    public int argCount = 3;
    public Class<?> declaringClass = org.firstinspires.ftc.teamcode.auxilary.PaulMath.class;

    private org.firstinspires.ftc.teamcode.auxilary.PaulMath manager;

    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 2) return new AutoautoNumericValue(PaulMath.proportionalPID(((AutoautoNumericValue)args[0]).getFloat(), ((AutoautoNumericValue)args[1]).getFloat()));
        else if(args.length == 5) return new AutoautoNumericValue(PaulMath.proportionalPID(((AutoautoNumericValue)args[0]).getFloat(), ((AutoautoNumericValue)args[1]).getFloat(), ((AutoautoNumericValue)args[2]).getFloat(),
                                                                                ((AutoautoNumericValue)args[3]).getFloat(), ((AutoautoNumericValue)args[4]).getFloat()));
        else if(args.length == 3) return new AutoautoNumericValue(PaulMath.proportionalPID(((AutoautoNumericValue)args[0]).getFloat(), ((AutoautoNumericValue)args[1]).getFloat(), ((AutoautoNumericValue)args[2]).getFloat()));
        else return new AutoautoUndefined();
    }
}