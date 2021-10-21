package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;

public interface AutoautoCallableValue {
    AutoautoValue returnValue = null;

    AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException;
}
