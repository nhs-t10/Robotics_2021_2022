package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;

public class NotEqualsOperator extends AutoautoOperator {
    @Override
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoPrimitive right) {
        return new AutoautoBooleanValue(!left.getString().equals(right.getString()));
    }

    //falsy-checking
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoPrimitive right) {
        return new AutoautoBooleanValue(left.value != AutoautoBooleanValue.isTruthy(right));
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left,  AutoautoBooleanValue right) {
        return new AutoautoBooleanValue(AutoautoBooleanValue.isTruthy(left) != right.value);
    }

    @Override
    public String getOperatorStr() {
        return "!=";
    }
}
