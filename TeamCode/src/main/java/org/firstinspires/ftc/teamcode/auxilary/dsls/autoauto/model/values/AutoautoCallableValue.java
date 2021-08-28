package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

public interface AutoautoCallableValue {
    AutoautoValue returnValue = null;

    AutoautoPrimitive call(AutoautoPrimitive[] args);
}
