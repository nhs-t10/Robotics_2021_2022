package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;

public class LengthFunction extends NativeFunction {
    public String name = "length";
    public int argCount = 1;

    public LengthFunction() {
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) throw new AutoautoArgumentException("0 arguments provided to length(); 1 argument required" + AutoautoProgram.formatStack(getLocation()));

        AutoautoPrimitive arg = args[0];

        if(arg instanceof AutoautoTable) return new AutoautoNumericValue(( (AutoautoTable) arg).arrayLength() );
        else if(arg instanceof AutoautoString) return new AutoautoNumericValue(arg.getString().length());
        else return new AutoautoNumericValue(args.length);
    }
}