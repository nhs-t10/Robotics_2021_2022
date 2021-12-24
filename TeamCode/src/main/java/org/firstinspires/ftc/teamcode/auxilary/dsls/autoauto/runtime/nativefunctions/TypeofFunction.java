package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUnitValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.ResolvedTitledArg;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;

public class TypeofFunction extends NativeFunction {
    public String name = "length";
    public int argCount = 1;

    public TypeofFunction() {
    }

    @Override
    public String[] getArgNames() {
        return new String[0];
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0 || args[0] == null) return new AutoautoUndefined();

        AutoautoPrimitive p = args[0];

        if(p instanceof AutoautoUndefined && ((AutoautoUndefined) p).source == AutoautoUndefined.NONEXISTENT_VARIABLE) return new AutoautoUndefined();

        return new AutoautoString(type(p));
    }

    private String type(AutoautoPrimitive p) {
        if(p instanceof AutoautoUnitValue) return "unit";
        if(p instanceof AutoautoNumericValue) return "numeric";
        if(p instanceof AutoautoString) return "string";
        if(p instanceof AutoautoBooleanValue) return "boolean";
        if(p instanceof AutoautoCallableValue) return "function";
        if(p instanceof AutoautoUndefined) return "undefined";
        if(p instanceof ResolvedTitledArg) return type(((ResolvedTitledArg)p).value);
        return "object";
    }
}