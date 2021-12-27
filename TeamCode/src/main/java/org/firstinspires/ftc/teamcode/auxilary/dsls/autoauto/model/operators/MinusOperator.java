package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;

public class MinusOperator extends AutoautoOperator {

    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoNumericValue right) {
        return new AutoautoNumericValue(left.value - right.value);
    }

    @Override
    public String getOperatorStr() {
        return "-";
    }
}
