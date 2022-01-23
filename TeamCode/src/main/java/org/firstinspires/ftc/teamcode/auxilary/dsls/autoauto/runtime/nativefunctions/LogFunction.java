package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class LogFunction extends NativeFunction {
    public String name = "log";
    public int argCount = 1;

    public LogFunction() {

    }

    @Override
    public String[] getArgNames() {
        return new String[0];
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        for(AutoautoPrimitive a : args) FeatureManager.logger.log((a == null ? new AutoautoUndefined() : a).getString(), this.getLocation());
        return new AutoautoUndefined();
    }
}