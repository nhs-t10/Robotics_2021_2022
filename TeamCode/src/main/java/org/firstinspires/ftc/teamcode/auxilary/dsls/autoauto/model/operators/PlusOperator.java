package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;

public class PlusOperator extends AutoautoOperator {
    @Override
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoPrimitive right) {
        return new AutoautoString(left.getString() + right.getString());
    }

    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoNumericValue right) {
        return new AutoautoNumericValue(left.value + right.value);
    }

    //casting booleans to 0/1
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoBooleanValue right) {
        return new AutoautoNumericValue(left.value + (right.value?1:0));
    }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoNumericValue right) {
        return new AutoautoNumericValue(right.value + (left.value?1:0));
    }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoBooleanValue right) {
        return new AutoautoNumericValue((right.value?1f:0f) + (left.value?1:0));
    }

    @Override
    public String getOperatorStr() {
        return "+";
    }
}
