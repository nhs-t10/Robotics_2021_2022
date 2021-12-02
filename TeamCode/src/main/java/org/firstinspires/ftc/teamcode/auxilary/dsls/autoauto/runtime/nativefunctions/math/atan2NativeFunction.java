package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;

public class atan2NativeFunction extends NativeFunction {
    @Override
    public String[] getArgNames() {
        return new String[] { "y", "x" };
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
        if(args.length < 2) return new AutoautoUndefined();

        if(args[0] instanceof AutoautoNumericValue && args[1] instanceof AutoautoNumericValue) {
            return new AutoautoNumericValue(Math.atan2(
                    ((AutoautoNumericValue)args[0]).getFloat(),
                    ((AutoautoNumericValue)args[1]).getFloat()
                    ));
        }
        return new AutoautoUndefined();
    }
}
