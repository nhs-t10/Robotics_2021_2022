package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.ManagerSetupException;

public interface AutoautoCallableValue {
    AutoautoValue returnValue = null;

    String[] getArgNames();
    AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException;
}
