package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoString;

public class TimesOperator extends AutoautoOperator {
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoNumericValue right) {
        return new AutoautoNumericValue(left.value * right.value);
    }

    //string multiplication support
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoString right) {
        return new AutoautoString(PaulMath.repeat(right.value, (int)left.value));
    }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoNumericValue right) {
        return new AutoautoString(PaulMath.repeat(left.value, (int)right.value));
    }

    //boolean support for a ternary-ish operator
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoPrimitive right) {
        return left.value?right:new AutoautoNumericValue(0f);
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoBooleanValue right) {
        return right.value?left:new AutoautoNumericValue(0f);
    }

    @Override
    public String getOperatorStr() {
        return "*";
    }
}
