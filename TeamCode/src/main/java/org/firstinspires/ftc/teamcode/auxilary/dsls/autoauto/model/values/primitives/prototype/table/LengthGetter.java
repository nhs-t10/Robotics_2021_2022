package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.table;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.ManagerSetupException;

public class LengthGetter extends NativeFunction {
    @Override
    public String[] getArgNames() {
        return new String[] {"this"};
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
        if(args[0] instanceof AutoautoTable) return new AutoautoNumericValue(((AutoautoTable) args[0]).arrayLength());
        return new AutoautoUndefined();
    }
}
