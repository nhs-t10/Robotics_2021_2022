package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.table;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.ManagerSetupException;

public class PushFunctionGetter extends NativeFunction {
    @Override
    public String[] getArgNames() {
        return new String[]{"this"};
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
        return new PushFunction((AutoautoTable) args[0]);
    }

    static class PushFunction extends NativeFunction {

        private final AutoautoTable thisArg;

        public PushFunction(AutoautoTable arg) {
            this.thisArg = arg;
        }

        @Override
        public String[] getArgNames() {
            return new String[0];
        }

        @Override
        public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
            int len = thisArg.arrayLength();
            for(int i = 0; i < args.length; i++) {
                thisArg.setProperty(new AutoautoNumericValue(len + i), args[i]);
            }
            return new AutoautoNumericValue(len + args.length);
        }
    }
}
