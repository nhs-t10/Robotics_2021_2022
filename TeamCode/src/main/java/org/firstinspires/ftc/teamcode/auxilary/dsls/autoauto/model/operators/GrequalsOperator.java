package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoString;

public class GrequalsOperator extends AutoautoComparisonOperator {
    @Override
    //if none of the other overloads have activated, just uh... give up :/ give it false
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoPrimitive right) {
        return new AutoautoBooleanValue(false);
    }

    //compare strings
    public AutoautoPrimitive eval(AutoautoString left, AutoautoString right) {
        return new AutoautoBooleanValue(left.value.compareTo(right.value) >= 0);
    }

    //compare numbers
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoNumericValue right) {
        return new AutoautoBooleanValue(left.value >= right.value);
    }

    @Override
    public String getOperatorStr() {
        return ">=";
    }
}
