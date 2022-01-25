package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;

public abstract class AutoautoComparisonOperator extends AutoautoOperator {
    //cast booleans to numbers
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoPrimitive right) {
        return eval(new AutoautoNumericValue(left.value?1f:0f), right, true);
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoBooleanValue right) {
        return eval(left, new AutoautoNumericValue(right.value?1f:0f), true);
    }

    //cast tables to numbers
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoPrimitive right) {
        return eval(new AutoautoNumericValue(left.size()), right, true);
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoTable right) {
        return eval(left, new AutoautoNumericValue(right.size()), true);
    }

    //cast undefined to 0
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoPrimitive right) {
        return eval(new AutoautoNumericValue(0), right, true);
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoUndefined right) {
        return eval(left, new AutoautoNumericValue(0), true);
    }
}
