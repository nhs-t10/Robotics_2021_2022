package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.universal;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.ManagerSetupException;

public class ToStringFunctionGetter extends NativeFunction {
    @Override
    public String[] getArgNames() {
        return new String[] {"this"};
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
        return new ToStringFunction(args[0]);
    }
    static class ToStringFunction extends NativeFunction {

        private final AutoautoPrimitive thisArg;

        public ToStringFunction(AutoautoPrimitive arg) {
            this.thisArg = arg;
        }

        @Override
        public String[] getArgNames() {
            return new String[0];
        }

        @Override
        public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
            return new AutoautoString(thisArg.getString());
        }
    }
}
