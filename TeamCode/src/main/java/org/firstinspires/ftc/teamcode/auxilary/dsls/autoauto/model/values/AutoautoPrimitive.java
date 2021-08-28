package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.jetbrains.annotations.NotNull;

public abstract class AutoautoPrimitive extends AutoautoValue {
    @NotNull
    @Override
    public AutoautoPrimitive getResolvedValue() {
        return this;
    }
}
