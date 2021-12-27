package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;

public class ExponentiationOperator extends AutoautoOperator {

    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoNumericValue right) {
        return new AutoautoNumericValue(Math.pow(left.value, right.value));
    }

    //boolean support for a ternary-ish operator
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoBooleanValue right) {
        return right.value ? left : new AutoautoNumericValue(1f);
    }

    @Override
    public String getOperatorStr() {
        return "**";
    }
}
