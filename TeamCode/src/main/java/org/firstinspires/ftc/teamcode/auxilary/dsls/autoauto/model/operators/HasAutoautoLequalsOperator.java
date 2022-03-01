package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;

public interface HasAutoautoLequalsOperator extends HasAutoautoOperatorInterface {
    AutoautoPrimitive opLequals(AutoautoPrimitive other, boolean otherIsLeft);
}
