package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class LogFunction extends NativeFunction {
    public String name = "log";
    public int argCount = 1;

    public LogFunction() {

    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        FeatureManager.logger.log((args[0] == null ? new AutoautoUndefined() : args[0]).getString());
        return new AutoautoUndefined();
    }
}