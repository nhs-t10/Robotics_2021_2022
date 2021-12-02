package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;

public class absNativeFunction extends NativeFunction {
    @Override
    public String[] getArgNames() {
        return new String[] { "value" };
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
        if(args.length < 1) return new AutoautoNumericValue(0);
        if(args[0] instanceof AutoautoNumericValue) {
            return new AutoautoNumericValue(Math.abs(((AutoautoNumericValue)args[0]).getFloat()));
        }
        return new AutoautoUndefined();
    }
}
