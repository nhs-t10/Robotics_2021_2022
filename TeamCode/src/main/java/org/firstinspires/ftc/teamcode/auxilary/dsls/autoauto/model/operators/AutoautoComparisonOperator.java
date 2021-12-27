package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public abstract class AutoautoComparisonOperator extends AutoautoOperator {
    //cast booleans to numbers
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoPrimitive right) {
        return eval(new AutoautoNumericValue(left.value?1f:0f), right);
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoBooleanValue right) {
        return eval(left, new AutoautoNumericValue(right.value?1f:0f));
    }

    //cast tables to numbers
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoPrimitive right) {
        return eval(new AutoautoNumericValue(left.size()), right);
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoTable right) {
        return eval(left, new AutoautoNumericValue(right.size()));
    }

    //cast undefined to 0
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoPrimitive right) {
        return eval(new AutoautoNumericValue(0), right);
    }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoUndefined right) {
        return eval(left, new AutoautoNumericValue(0));
    }
}
